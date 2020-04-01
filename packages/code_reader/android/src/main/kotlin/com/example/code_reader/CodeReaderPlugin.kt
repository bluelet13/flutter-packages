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
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener
import io.flutter.view.TextureRegistry


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


open class CodeReaderPlugin : FlutterPlugin, ActivityAware {

    private var flutterPluginBinding: FlutterPluginBinding? = null

    internal interface PermissionsRegistry {
        fun addListener(handler: RequestPermissionsResultListener?)
    }

    companion object {
        private const val CHANNEL_NAME = "dev.bluelet13.code_reader_view"

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val plugin = CodeReaderPlugin()
            plugin.maybeStartListening(registrar.activity(), registrar.messenger())


//            CodeReaderPlugin.registrar = registrar
//            val activity = registrar.activity()
//            val messenger = registrar.messenger()
//
//            registrar.platformViewRegistry()
//                    .registerViewFactory(CHANNEL_NAME, QrReaderFactory(activity, messenger))

//            val channel = MethodChannel(registrar.messenger(), CHANNEL_NAME)
//            registrar.platformViewRegistry().registerViewFactory(CHANNEL_VIEW_NAME, QrReaderFactory(registrar.messenger()))
//            val instance = FlutterQrReaderPlugin(registrar)
//            channel.setMethodCallHandler(instance)
        }
    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        flutterPluginBinding = binding
//        val activity = binding. .activity()
//        val messenger = binding.binaryMessenger
//
//        binding.platformViewRegistry
//                .registerViewFactory(CHANNEL_NAME, QrReaderFactory(activity, messenger))
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        flutterPluginBinding = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        maybeStartListening(binding.activity, flutterPluginBinding!!.binaryMessenger)
//        binding.platformViewRegistry
//                .registerViewFactory(CHANNEL_NAME, QrReaderFactory(activity, messenger))
//        maybeStartListening(
//                binding.activity,
//                flutterPluginBinding!!.binaryMessenger,
//                flutterPluginBinding!!.flutterEngine.renderer);
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    private fun maybeStartListening(
            activity: Activity,
            messenger: BinaryMessenger) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { // If the sdk is less than 21 (min sdk for Camera2) we don't register the plugin.
            return
        }

        flutterPluginBinding!!.platformViewRegistry
                .registerViewFactory(CHANNEL_NAME, QrReaderFactory(activity, messenger))
//        methodCallHandler = MethodCallHandlerImpl(
//                activity, messenger, CameraPermissions(), permissionsRegistry, textureRegistry)
    }

    //  @TargetApi(Build.VERSION_CODES.M)
//  private void checkPermissions(final PermissionsResult result) {
//    if (!(registrar.activity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
//      registrar.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
//        @Override
//        public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//          if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
//            for (int i = 0; i < permissions.length; i++) {
//              String permission = permissions[i];
//              int grantResult = grantResults[i];
//
//              if (permission.equals(Manifest.permission.CAMERA)) {
//                if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                  result.onSuccess();
//                } else {
//                  result.onError();
//                }
//              }
//            }
//          }
//          return false;
//        }
//      });
//      registrar.activity().requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
//    } else {
//      result.onSuccess();
//    }
//  }


}
