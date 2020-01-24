import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';

class NativeLocale {
  final String base;

  NativeLocale({this.base = window.locale.languageCode}) : assert(base != null);

  static const MethodChannel _channel = const MethodChannel('bluelet13/native_locale');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
