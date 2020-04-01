package com.example.code_reader.readerView;

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.io.Serializable

class QRCodeReaderView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    interface OnScanCompleteListener : Serializable {
        /**
         * Invoked to provide access to the result of the QR scan.
         */
        fun onScanComplete(result: String)
    }

    private var imageReader: ImageReader? = null

    private lateinit var cameraId: String
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    private var backgroundHandler: Handler? = null
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var previewRequest: CaptureRequest? = null

    private var completeListener: OnScanCompleteListener? = null

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            this@QRCodeReaderView.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
            this@QRCodeReaderView.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            onDisconnected(cameraDevice)
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        this.holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holder.setFixedSize(300, 300)
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    fun addOnScanCompleteListener(listener: OnScanCompleteListener) {
        this.completeListener = listener
    }

    fun cameraStart() {
        handleCaptureException("Failed to start camera preview session") {
            val manager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            for (cameraId in manager.cameraIdList) {
                this.cameraId = cameraId
                manager.openCamera(cameraId, stateCallback, null)
                break
            }
        }
    }


    companion object {
        internal const val STATE_FIND_QRCODE = 0
        internal const val STATE_DECODE_PROGRESS = 1
        internal const val STATE_QRCODE_EXIST = 2

        internal const val MAX_PREVIEW_WIDTH = 786
        internal const val MAX_PREVIEW_HEIGHT = 786

        @Volatile
        internal var qrState: Int = 0
    }

    internal fun readImageSource(image: Image): PlanarYUVLuminanceSource? {
        return try {
            val plane = image.planes[0]
            val buffer = plane.buffer
            val data = ByteArray(buffer.remaining()).also { buffer.get(it) }
            val height = image.height
            val width = image.width
            val dataWidth = width + ((plane.rowStride - plane.pixelStride * width) / plane.pixelStride)
            PlanarYUVLuminanceSource(data, dataWidth, height, 0, 0, width, height, false)
        } catch (e: Exception) {
            Log.e("code/plugin", e.message, e)
            null
        }
    }

    internal class AsyncScanningTask(
            private val scanCompleteListener: OnScanCompleteListener?,
            private val multiFormatReader: MultiFormatReader = MultiFormatReader()
    ) : AsyncTask<BinaryBitmap, Void, Void>() {

        override fun doInBackground(vararg bitmaps: BinaryBitmap): Void? {
            return processImage(bitmaps[0])
        }

        private fun processImage(bitmap: BinaryBitmap): Void? {
            if (qrState != STATE_DECODE_PROGRESS) {
                return null
            }

            try {
                val rawResult = multiFormatReader.decodeWithState(bitmap)
                if (rawResult != null) {
                    scanCompleteListener?.onScanComplete(rawResult.text)
                    qrState = STATE_FIND_QRCODE
                }
            } catch (e: NotFoundException) {
                qrState = STATE_FIND_QRCODE
            } finally {
                multiFormatReader.reset()
            }

            return null
        }
    }


    private inner class ImageAvailableCallback : ImageReader.OnImageAvailableListener {
        private var image: Image? = null

        override fun onImageAvailable(reader: ImageReader?) {
            try {
                image = reader?.acquireNextImage()

                val availableImage = image
                if (availableImage != null) {
                    val source = readImageSource(availableImage)
                    if (source != null) {
                        val bitmap = BinaryBitmap(HybridBinarizer(source))
                        if (qrState == STATE_FIND_QRCODE) {
                            qrState = STATE_DECODE_PROGRESS
                            AsyncScanningTask(completeListener).execute(bitmap)
                        }
                    }
                }
            } finally {
                image?.close()
            }
        }
    }


    private fun createCameraPreviewSession() {
        imageReader = ImageReader.newInstance(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT, ImageFormat.YUV_420_888, 2).apply {
            setOnImageAvailableListener(ImageAvailableCallback(), backgroundHandler)

            handleCaptureException("Failed to create camera preview session") {

                cameraDevice?.let {
                    previewRequestBuilder = it.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                        addTarget(surface)
                        addTarget(holder.surface)
                    }

                    val captureCallback = object : CameraCaptureSession.CaptureCallback() {}
                    val stateCallback = object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            if (null == cameraDevice) return

                            previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                            previewRequest = previewRequestBuilder?.build()
                            captureSession = cameraCaptureSession

                            handleCaptureException("Failed to request capture") {
                                cameraCaptureSession.setRepeatingRequest(
                                        previewRequest as CaptureRequest,
                                        captureCallback,
                                        backgroundHandler
                                )
                            }
                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                            Log.e("code/plugin", "Failed to configure CameraCaptureSession")
                        }
                    }

                    it.createCaptureSession(listOf(holder.surface, surface), stateCallback, null)
                }
            }
        }


    }

    @Suppress("TooGenericExceptionCaught")
    private fun handleCaptureException(msg: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            when (e) {
                is CameraAccessException, is IllegalStateException -> {
                    Log.e("code/plugin", e.message, e)
                }
                else -> throw e
            }
        }
    }

}
