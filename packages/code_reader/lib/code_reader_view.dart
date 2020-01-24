import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef ReadChangeBack = void Function(List<String>);
typedef CodeReaderCameraPermission = void Function(bool);

///
class CodeReaderView extends StatefulWidget {
  /// ViewType
  final String viewType = "dev.bluelet13.code_reader_view";


  final ReadChangeBack onScan;

  ///
  final Function(CodeReaderViewController) callback;

  ///
  final Widget noPermission;

  ///
  final Widget noSupportPlatform;

  ///
  CodeReaderView({
    @required this.onScan,
    this.callback,
    this.noPermission,
    this.noSupportPlatform,
  });

  @override
  State<StatefulWidget> createState() => CodeReaderViewState();
}

///
class CodeReaderViewState extends State<CodeReaderView> {
  /// camera premission
  /// default true
  bool cameraPermission = true;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    if (!cameraPermission)
      return widget.noPermission ?? DefaultNoPermissionView();
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      return _provideIosPlatformView();
    } else {
      return widget.noSupportPlatform ?? DefaultNoSupportPlatformView();
    }
  }

  @override
  void dispose() {
    super.dispose();
  }

  /// Provide to iOS Platform view
  Widget _provideIosPlatformView() {
    return UiKitView(
      viewType: widget.viewType,
      creationParams: {},
      creationParamsCodec: const StandardMessageCodec(),
      onPlatformViewCreated: _onPlatformViewCreated,
    );
  }

  void _onPlatformViewCreated(int id) {
    final controller =
    CodeReaderViewController(
      id, widget.onScan, this._onUpdateCameraPermission,);
    widget.callback(controller);
  }

  void _onUpdateCameraPermission(bool permission) {
    print("camera" + permission.toString());
    setState(() => this.cameraPermission = permission);
  }

}

///
class DefaultNoPermissionView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(color: Colors.indigoAccent);
  }
}

/// sa
class DefaultNoSupportPlatformView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(color: Colors.green);
  }
}

///
class CodeReaderViewController {
  final int id;
  final MethodChannel channel;
  ReadChangeBack onQrBack;

  /// Camra
  CodeReaderCameraPermission cameraPermission;
  bool isLock = false;

  ///
  CodeReaderViewController(this.id,
      this.onQrBack,
      this.cameraPermission,)
      : channel = MethodChannel('dev.bluelet13.code_reader_view_$id') {
    channel.setMethodCallHandler(_handleMessages);
  }

  Future _handleMessages(MethodCall call) async {
    switch (call.method) {
      case "onReadCode":
        if (this.isLock) return;
        this.lock();
        this.onQrBack(call.arguments as List<String>);
        break;
      case "permission":
        this.cameraPermission(call.arguments);
        break;
      default:
        break;
    }
  }

  ///
  void unlock() async {
    this.isLock = false;
  }

  ///
  void lock() async {
    this.isLock = true;
  }

  /// 打开手电筒
  Future<bool> setFlashlight() async {
    return channel.invokeMethod("flashlight");
  }

  /// Start camera
  Future startCamera() async {
    return channel.invokeMethod("startCamera");
  }

  /// 结束扫码
  Future stopCamera() async {
    return channel.invokeMethod("stopCamera");
  }
}
