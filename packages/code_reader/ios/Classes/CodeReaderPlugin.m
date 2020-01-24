#import "CodeReaderPlugin.h"
#if __has_include(<code_reader/code_reader-Swift.h>)
#import <code_reader/code_reader-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "code_reader-Swift.h"
#endif

@implementation CodeReaderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCodeReaderPlugin registerWithRegistrar:registrar];
}
@end
