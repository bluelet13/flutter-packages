import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'announce_controller.dart';
import 'announcement.dart';

typedef AnnounceFire = Future<void> Function(Announcement, bool);

///
// ignore: must_be_immutable
class AnnouncerView extends StatefulWidget {
  ///
  final AnnounceFire callback;

  ///
  final List<Announcement> announcements;

  ///
  final Widget child;

  ///
  AnnounceController controller;

  ///
  AnnouncerView({
    @required this.announcements,
    @required this.callback,
    @required this.child,
    this.controller,
  })  : assert(announcements != null && announcements.isNotEmpty),
        assert(callback != null),
        assert(child != null) {
    this.controller = this.controller ?? AnnounceController();
  }

  @override
  State<StatefulWidget> createState() => _AnnouncerViewState();
}

class _AnnouncerViewState extends State<AnnouncerView> {
  @override
  Widget build(BuildContext context) {
    _asyncCheckAnnounce(widget.announcements);
    return Container(child: widget.child);
  }

  void _asyncCheckAnnounce(List<Announcement> announce) {
    if (widget.controller.isLocked) return;
    widget.controller.lock();
    _callbackUnreadAnnounce(announce)
        .then(this._onValue, onError: this._onError);
  }

  Future<void> _callbackUnreadAnnounce(List<Announcement> announce) async {
    final preferences = await SharedPreferences.getInstance();
    for (var i = 0; i < announce.length; i++) {
      await _s(preferences, announce[i]);
    }
  }

  Future<void> _s(SharedPreferences preferences, Announcement announce) async {
    final key = "announce.${announce.id}";

    final isFirst = !(preferences.getBool(key) ?? false);
    await widget.callback(announce, isFirst);

    if (isFirst) {
      await preferences.setBool(key, true);
    }
  }

  Future<void> _onValue(dynamic value) {
    widget.controller.unlock();
    return Future.value();
  }

  Future<void> _onError(dynamic error) {
    widget.controller.unlock();
    return Future.value();
  }
}
