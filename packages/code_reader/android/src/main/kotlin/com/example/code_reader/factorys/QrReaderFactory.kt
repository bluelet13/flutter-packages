package com.example.code_reader.factorys

import android.app.Activity
import android.content.Context
import com.example.code_reader.readerView.CodeReaderView
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.flutter.plugin.common.BinaryMessenger

class QrReaderFactory(private val activity: Activity, private val messenger: BinaryMessenger) :
        PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context?, id: Int, args: Any?): PlatformView {
        return CodeReaderView(activity, context!!, messenger, id)
    }

}