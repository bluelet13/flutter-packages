import Flutter

class CodeReaderViewFactory: NSObject, FlutterPlatformViewFactory {
    
//    private weak var registrar: FlutterPluginRegistrar?
    var messenger: FlutterBinaryMessenger!
    
    init(messenger: (NSObject & FlutterBinaryMessenger)?) {
        super.init()
        self.messenger = messenger
    }
    
  func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
    return CodeReaderView(frame: frame, viewIdentifier: viewId, arguments: args, binaryMessenger: messenger)
  }

  func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
    return FlutterStandardMessageCodec.sharedInstance()
  }
}
