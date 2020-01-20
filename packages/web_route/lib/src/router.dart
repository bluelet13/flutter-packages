import 'package:uri/uri.dart';
import 'package:flutter/widgets.dart';

///
typedef Widget RouterBuilder(BuildContext context, RouteParam param);

/// A modal route that replaces the entire screen
class WebRoute {
  /// The definitions for route maps.
  final Map<String, RouterBuilder> definitions;

  /// Creates a web page route for use.
  ///
  /// The [definitions] arguments must not be null.
  WebRoute(this.definitions) : assert(null != definitions);

  /// Select optimal navigation from route settings
  ///
  /// See [RouteSettings] for details.
  RouteParam bestMatchNavigation(RouteSettings settings) {
    final paths = settings.name.split('?');
    final path = paths[0];
    final query = (paths.length > 1) ? paths[1] : '';

    final entries = this.definitions.entries;
    return entries
        .where((define) => _isMatchExpectUri(define.key, path))
        .map((define) => RouteParam.of(define.key, settings.name, path, query))
        .first;
  }

  bool _isMatchExpectUri(String expect, String actual) {
    return UriParser(UriTemplate(expect)).matches(Uri.parse(actual));
  }
}

/// A Model that parsed the route character
class RouteParam {
  /// route template, uri
  final String template, uri;

  /// Todo: Improve to immutable
  final Map<String, String> _pathParams, _queryParams;

  /// A map of path parameter
  Map<String, String> get pathParams => _pathParams;

  /// A map of query string parameter
  Map<String, String> get queryParams => _queryParams;

  RouteParam._internal(
    this.template,
    this.uri,
    this._pathParams,
    this._queryParams,
  )   : assert(_pathParams != null),
        assert(_queryParams != null);

  /// Create a parsed the route character
  static RouteParam of(String template, String uri, String path, String query) {
    // Check if path matches the route template
    var tmp = UriTemplate(template);
    var match = UriParser(tmp).match(Uri.parse(path));
    if (match == null) {
      return null;
    }
    var q = Uri.splitQueryString(query);
    return RouteParam._internal(template, uri, match.parameters, q);
  }
}
