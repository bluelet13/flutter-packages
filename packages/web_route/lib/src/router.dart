import 'package:uri/uri.dart';
import 'package:flutter/widgets.dart';

///
typedef Widget RouterBuilder(BuildContext context, NavigationParam param);

/// Router
class WebRoute {
  /// Router
  final Map<String, RouterBuilder> definitions;

  ///
  WebRoute(this.definitions) : assert(null != definitions);

  NavigationParam mm(RouteSettings settings) {
    final paths = settings.name.split('?');
    final path = paths[0];
    final query = (paths.length > 1) ? paths[1] : '';

    final entries = this.definitions.entries;
    final dd = entries
        .where((define) => _isMatchUri(define.key, path))
        .map((define) => NavigationParam.of(define.key, path, query));
    return dd.first;
  }

  bool _isMatchUri(String expect, String actual) {
    return UriParser(UriTemplate(expect)).matches(Uri.parse(actual));
  }
}

class NavigationParam {
  /// TODO: Add document
  final String route;

  /// TODO: Add document
  final UriMatch match;

  /// TODO: Add document
  final Map<String, String> pathParams;

  /// TODO: Add document
  final Map<String, String> queryParams;

  NavigationParam._internal(
    this.route,
    this.match,
    this.pathParams,
    this.queryParams,
  );

  static NavigationParam of(String route, String path, String query) {
    final qu = Uri.splitQueryString(query);
    final match = UriParser(UriTemplate(route)).match(Uri.parse(path));
    return NavigationParam._internal(route, match, match.parameters, qu);
  }
}
