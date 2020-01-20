import 'package:flutter/cupertino.dart';
import 'router.dart';

class CupertinoWebRoute extends WebRoute {
  CupertinoWebRoute(Map<String, RouterBuilder> definitions) : super(definitions);

  Route<dynamic> build(RouteSettings settings) {
    final param = mm(settings);
    return CupertinoPageRoute<Null>(
        builder: (context) => this.definitions[param.route](context, param), settings: settings);
  }
}
