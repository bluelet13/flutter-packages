#import "NativeLocalePlugin.h"
#if __has_include(<native_locale/native_locale-Swift.h>)
#import <native_locale/native_locale-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "native_locale-Swift.h"
#endif

@implementation NativeLocalePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftNativeLocalePlugin registerWithRegistrar:registrar];
}
@end
