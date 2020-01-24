import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:shared_preferences/shared_preferences.dart';

typedef AnnounceFire = Future<void> Function(Announcement);

///
class Sample extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final announce = [
      Announcement('aa'),
      Announcement('ab'),
      Announcement('ac'),
    ];
    return AnnouncerView(
      announcements: announce,
      callback: (c) {
        return showDialog(context: null);
      },
      child: Container(color: Colors.red),
    );
  }
}

///
class AnnouncerView extends StatefulWidget {
  ///
  final AnnounceFire callback;

  ///
  final List<Announcement> announcements;

  ///
  final Widget child;

  ///
  AnnouncerView({
    @required this.announcements,
    @required this.callback,
    @required this.child,
  });

  @override
  State<StatefulWidget> createState() => _AnnouncerViewState();
}

class _AnnouncerViewState extends State<AnnouncerView> {
  ///
  bool isLock = false;

  @override
  Widget build(BuildContext context) {
    _asyncCheckAnnounce(widget.announcements);
    return Container(child: widget.child);
  }

  /// Lock
  void lock() {
    this.isLock = true;
  }

  /// Unlock
  void unlock() {
    this.isLock = true;
  }

  String _announceKey(String id) {
    return "announce.$id";
  }

  void _asyncCheckAnnounce(List<Announcement> announce) {
    if (isLock) return;
    this.lock();
    _callbackUnreadAnnounce(announce)
        .then((value) => this.unlock())
        .catchError(() => this.unlock());
  }

  Future<void> _callbackUnreadAnnounce(List<Announcement> list) async {
    final preferences = await SharedPreferences.getInstance();
    final announce = list
        .where((element) => preferences.getBool(_announceKey(element.id)))
        .toList();

    for (var i = 0; i < announce.length; i++) {
      await widget.callback(announce[i]);
      await preferences.setBool(announce[i].id, false);
    }
  }
}

///
class Announcement {
  ///
  final String id;

  ///
  Announcement(this.id);
}
