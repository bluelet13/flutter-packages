import Flutter
import UIKit

public class SwiftCodeReaderPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    
    let viewFactory = CodeReaderViewFactory(messenger: registrar.messenger() as! (NSObject & FlutterBinaryMessenger))
    registrar.register(viewFactory, withId: "dev.bluelet13.code_reader_view")
    
//     let channel = FlutterMethodChannel(name: "dev.bluelet13.code_reader", binaryMessenger: registrar.messenger())
//     let instance = SwiftCodeReaderPlugin()
//     registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
  }
}
