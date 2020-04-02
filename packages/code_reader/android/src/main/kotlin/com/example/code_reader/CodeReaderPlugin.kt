package com.example.code_reader

import android.app.Activity
import android.os.Build
import com.example.code_reader.factorys.QrReaderFactory
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.PluginRegistry.Registrar

open class CodeReaderPlugin : FlutterPlugin, ActivityAware {

    private var flutterPluginBinding: FlutterPluginBinding? = null

    companion object {
        private const val CHANNEL_NAME = "dev.bluelet13.code_reader_view"

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val plugin = CodeReaderPlugin()
            plugin.maybeStartListening(registrar.activity(), registrar.messenger())
        }
    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        flutterPluginBinding = binding
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        flutterPluginBinding = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        flutterPluginBinding?.let {
            maybeStartListening(binding.activity, it.binaryMessenger)
        }
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    private fun maybeStartListening(activity: Activity, messenger: BinaryMessenger) {
        flutterPluginBinding?.let {
            val factory = QrReaderFactory(activity, messenger)
            it.platformViewRegistry.registerViewFactory(CHANNEL_NAME, factory)
        }
    }


}
