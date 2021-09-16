#import "EsewaIntegrationPlugin.h"
#if __has_include(<esewa_integration/esewa_integration-Swift.h>)
#import <esewa_integration/esewa_integration-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "esewa_integration-Swift.h"
#endif

@implementation EsewaIntegrationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftEsewaIntegrationPlugin registerWithRegistrar:registrar];
}
@end
