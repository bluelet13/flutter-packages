import 'package:flutter/cupertino.dart';
import 'router.dart';

/// A modal route that replaces the entire screen with an iOS transition.
class CupertinoWebRoute extends WebRoute {
  /// Creates a web page route for use in an iOS designed app.
  ///
  /// The [definitions] arguments must not be null.
  CupertinoWebRoute(Map<String, RouterBuilder> definitions) : super(definitions);

  /// Creates a page route for use in an iOS designed app.
  Route<dynamic> build(RouteSettings settings) {
    final param = bestMatchNavigation(settings);
    return CupertinoPageRoute<Null>(
        builder: (context) => this.definitions[param.template](context, param), settings: settings);
  }
}
