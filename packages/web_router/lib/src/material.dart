import 'package:flutter/material.dart';
import 'router.dart';

class MaterialWebRoute extends WebRoute {
  MaterialWebRoute(Map<String, RouterBuilder> definitions) : super(definitions);

  Route<dynamic> build(RouteSettings settings) {
    final param = mm(settings);
    return MaterialPageRoute<Null>(
        builder: (context) => this.definitions[param.route](context, param), settings: settings);
  }
}
