import 'package:flutter/material.dart';
import 'router.dart';

/// A modal route that replaces the entire screen with an Android transition.
class MaterialWebRoute extends WebRoute {
  /// Creates a web page route for use in an iOS designed app.
  ///
  /// The [definitions] arguments must not be null.
  MaterialWebRoute(Map<String, RouterBuilder> definitions) : super(definitions);

  /// Creates a page route for use in an Android designed app.
  Route<dynamic> build(RouteSettings settings) {
    final param = bestMatchNavigation(settings);
    return MaterialPageRoute<Null>(
        builder: (context) => this.definitions[param.template](context, param), settings: settings);
  }
}
