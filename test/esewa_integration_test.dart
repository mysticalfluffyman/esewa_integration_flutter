import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:esewa_integration/esewa_integration.dart';

void main() {
  const MethodChannel channel = MethodChannel('esewa_integration');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await EsewaIntegration.platformVersion, '42');
  });
}
