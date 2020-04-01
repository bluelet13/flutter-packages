package com.example.code_reader

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.example.code_reader.factorys.QrReaderFactory
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File

/** CodeReaderPlugin */
//public class CodeReaderPlugin: FlutterPlugin, MethodCallHandler {
//  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
//    val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "code_reader")
//    channel.setMethodCallHandler(CodeReaderPlugin());
//  }
//
//  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
//  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
//  // plugin registration via this function while apps migrate to use the new Android APIs
//  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
//  //
//  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
//  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
//  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
//  // in the same class.
//  companion object {
//    @JvmStatic
//    fun registerWith(registrar: Registrar) {
//      val channel = MethodChannel(registrar.messenger(), "code_reader")
//      channel.setMethodCallHandler(CodeReaderPlugin())
//    }
//  }
//
//  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
//    if (call.method == "getPlatformVersion") {
//      result.success("Android ${android.os.Build.VERSION.RELEASE}")
//    } else {
//      result.notImplemented()
//    }
//  }
//
//  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//  }
//}


open class FlutterQrReaderPlugin internal constructor(registrar: Registrar) : MethodCallHandler {
    private val registrar: Registrar
    //    @Override
    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "setTorchMode" -> (call.arguments as HashMap<String, *>)["enabled"]?.let {
                setTorchMode(it as Boolean, result)
            }
            else -> result.notImplemented()
        }

        if (call.method.equals("imgQrCode")) {
            imgQrCode(call, result)
        } else {
            result.notImplemented()
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun imgQrCode(call: MethodCall, result: Result) {
        val filePath: String? = call.argument("file")
        if (filePath == null) {
            result.error("Not found data", null, null)
            return
        }
        val file = File(filePath)
        if (!file.exists()) {
            result.error("File not found", null, null)
        }
        object : AsyncTask<String?, Int?, String?>() {
            override fun doInBackground(vararg params: String?): String? { // QrReaderFactory解析二维码/条码
                return QRCodeDecoder.syncDecodeQRCode(filePath)
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                if (null == s) {
                    result.error("not data", null, null)
                } else {
                    result.success(s)
                }
            }
        }.execute(filePath)
    }

    companion object {
        private const val CHANNEL_NAME = "com.example.code_reader.code_reader"
        private const val CHANNEL_VIEW_NAME = "com.example.code_reader.code_reader.reader_view"

        /** Plugin registration.  */
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), CHANNEL_NAME)
            registrar.platformViewRegistry().registerViewFactory(CHANNEL_VIEW_NAME, QrReaderFactory(registrar))
            val instance = FlutterQrReaderPlugin(registrar)
            channel.setMethodCallHandler(instance)
        }
    }

    init {
        this.registrar = registrar
    }
}
