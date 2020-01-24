import Flutter
import AVFoundation

let channelNameFormat = "dev.bluelet13.code_reader_view_%lld"

typealias PermissionCheckHandler = (Bool) -> Void

class CodeReaderView: UIView, FlutterPlatformView {
    fileprivate var viewId: Int64!
    fileprivate var channel: FlutterMethodChannel!
    
    fileprivate var captureSession: AVCaptureSession?
    fileprivate var captureDevice: AVCaptureDevice?
    fileprivate var videoPreviewLayer: AVCaptureVideoPreviewLayer?
    
  let label = UILabel()

  func view() -> UIView {
    return self
  }

  required init?(coder aDecoder: NSCoder) {
    fatalError("not implemented")
  }

  init(frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, binaryMessenger: FlutterBinaryMessenger) {
    super.init(frame: frame)
    
    self.backgroundColor = UIColor.black
    
    self.viewId = viewId
    self.channel = FlutterMethodChannel(name: "dev.bluelet13.code_reader_view_\(viewId)", binaryMessenger: binaryMessenger)
    
    self.channel.setMethodCallHandler({
        [weak self]
        (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
        if let this = self {
            this.onMethodCall(call: call, result: result)
        }
    })
  }
    
    
    func onMethodCall(call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "startCamera":
            self.startCamera()
        case "stopCamera":
            self.stopCamera()
        default:
            fatalError("not implemented")
        }
    }
    
    func startCamera() -> Void {
        self.permission { [weak self] (permission)  in
            guard let this = self, let session = this.session(), let layer = this.layer(session) else {
                return
            }
            this.layer.addSublayer(layer)
            session.startRunning()
        }
    }
    
    func session() -> AVCaptureSession? {
        self.captureSession = AVCaptureSession()
        self.captureDevice = AVCaptureDevice.default(for: .video) ?? nil
        
        guard let session = self.captureSession,
            let device = self.captureDevice,
            let input = try? AVCaptureDeviceInput.init(device: device) else {
                return nil
        }
            
        let output = AVCaptureMetadataOutput.init()
        
        session.addInput(input)
        session.addOutput(output)
        
        output.metadataObjectTypes = [AVMetadataObject.ObjectType.qr, AVMetadataObject.ObjectType.pdf417]
        output.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
        return session
    }
    
    func layer(_ session: AVCaptureSession) -> AVCaptureVideoPreviewLayer? {
        self.videoPreviewLayer = AVCaptureVideoPreviewLayer(session: session)
        guard let previewLayer = self.videoPreviewLayer else {
            return nil
        }
        previewLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        return previewLayer
    }
    

    func stopCamera() -> Void {
        self.captureSession?.stopRunning()
        self.captureSession = nil
        self.videoPreviewLayer?.removeFromSuperlayer()
    }
    
    func permission(_ result: @escaping PermissionCheckHandler) {
        if AVCaptureDevice.authorizationStatus(for: .video) == .authorized {
            result(true)
            self.channel.invokeMethod("permission", arguments: true)
        } else {
            AVCaptureDevice.requestAccess(for: .video) { [weak self] (granted: Bool) in
                result(granted)
                self?.channel.invokeMethod("permission", arguments: granted)
            }
        }
    }
    
    func s() {
        
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        self.videoPreviewLayer?.frame = self.layer.bounds
        
        if let connection = self.videoPreviewLayer?.connection, connection.isVideoOrientationSupported {
            connection.videoOrientation = self.avCaptureVideoOrientaion()
        }
    }
    
    private func avCaptureVideoOrientaion() -> AVCaptureVideoOrientation {
        switch UIDevice.current.orientation {
        case .portrait:
            return .portrait
        case .landscapeRight:
            return .landscapeLeft
        case .landscapeLeft:
            return .landscapeRight
        case .portraitUpsideDown:
            return .portraitUpsideDown
        default:
            return .portrait
        }
    }
    
}

extension CodeReaderView: AVCaptureMetadataOutputObjectsDelegate {
    
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        if metadataObjects.count == 0 {
            return
        }
        
        metadataObjects.forEach { (object) in
            var objects: Array<String> = []
            if let i = object as? AVMetadataMachineReadableCodeObject {
                objects.append(objectsS(i)["text"]!)
            }
            self.channel.invokeMethod("onReadCode", arguments: objects)
        }
    }
    
    private func objectsS(_ metadataObject: AVMetadataMachineReadableCodeObject) -> Dictionary<String, String> {
        return [
            "type" : metadataObject.type.rawValue,
            "text" : metadataObject.stringValue ?? ""
        ]
    }
    
}
