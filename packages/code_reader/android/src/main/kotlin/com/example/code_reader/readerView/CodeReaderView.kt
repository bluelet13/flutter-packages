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
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

class CodeReaderView(activity: Activity, context: Context, messenger: BinaryMessenger, id: Int) :
        PlatformView, MethodChannel.MethodCallHandler, QRCodeReaderView.OnScanCompleteListener {

    private val codeReaderView: QRCodeReaderView = QRCodeReaderView(context)
    private var channel = MethodChannel(messenger, "dev.bluelet13.code_reader_view_$id").also {
        it.setMethodCallHandler(this)
    }

    init {
        codeReaderView.addOnScanCompleteListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = context.checkSelfPermission(Manifest.permission.CAMERA)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 200)
            }
        }
    }

    override fun getView(): View {
        return this.codeReaderView
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "setText" -> {
                result.success(null)
            }
            "startCamera" -> {
                codeReaderView.cameraStart()
                Log.i("code/plugin", "startCamera on CodeReaderView")
            }
            else -> result.notImplemented()
        }
    }

    override fun dispose() {
    }

    override fun onScanComplete(result: String) {
        Log.i("code/plugin", result)
    }

}