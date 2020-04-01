package com.example.code_reader.factorys

import android.content.Context
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import me.hetian.flutter_qr_reader.views.QrReaderView

class QrReaderFactory(registrar: PluginRegistry.Registrar) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val registrar: PluginRegistry.Registrar
//    @Override
    override fun create(context: Context?, id: Int, args: Object?): PlatformView {
        return QrReaderView(context, registrar, id, args)
    }

    init {
        this.registrar = registrar
    }
}