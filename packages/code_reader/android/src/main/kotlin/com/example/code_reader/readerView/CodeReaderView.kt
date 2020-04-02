package com.example.code_reader.readerView

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

class CodeReaderView(private val activity: Activity, context: Context, messenger: BinaryMessenger, id: Int) :
        PlatformView, MethodChannel.MethodCallHandler, QRCodeReaderView.OnScanCompleteListener {

    private val codeReaderView: QRCodeReaderView = QRCodeReaderView(context)
    private var channel = MethodChannel(messenger, "dev.bluelet13.code_reader_view_$id").also {
        it.setMethodCallHandler(this)
    }

    init {
        codeReaderView.addOnScanCompleteListener(this)
    }

    override fun getView(): View {
        return this.codeReaderView
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "startCamera" -> {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    invokePermission()
                    return
                }
                codeReaderView.cameraStart()
                invokePermission()
            }
            "stopCamera" -> {
            }
            else -> result.notImplemented()
        }
    }

    override fun dispose() {
    }

    override fun onScanComplete(result: String) {
        activity.runOnUiThread {
            Log.i("code/plugin", result)
            channel.invokeMethod("onReadCode", listOf(result))
        }
    }

    private fun invokePermission() {
        activity.runOnUiThread {
            channel.invokeMethod("permission", true)
        }
    }

}