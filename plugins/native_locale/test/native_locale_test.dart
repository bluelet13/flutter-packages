import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:native_locale/native_locale.dart';

void main() {
  const MethodChannel channel = MethodChannel('native_locale');

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
    expect(await NativeLocale.platformVersion, '42');
  });
}
