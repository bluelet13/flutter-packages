import 'package:shared_preferences/shared_preferences.dart';

///
class AnnounceController {
  ///
  bool _isLock = false;

  ///
  bool get isLocked => this._isLock;

  /// Lock
  void lock() {
    this._isLock = true;
  }

  /// Unlock
  void unlock() {
    this._isLock = true;
  }

  Future<bool> update(String key) async {
    final preferences = await SharedPreferences.getInstance();
    return preferences.setBool(key, true);
  }
}