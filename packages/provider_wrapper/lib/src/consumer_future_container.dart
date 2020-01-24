import 'package:flutter/widgets.dart';
import 'consumer_future.dart';

/// A Display "Widget" defined according to multiple states of Future
/// in "ConsumerFutureBuilder"
class ConsumerFutureContainer<T, D> extends StatelessWidget {
  /// The asynchronous computation callback to which this builder
  /// is currently connected
  final Future<D> Function(BuildContext context, T value) future;

  /// Widget used when the Future snapshot not contains a data value
  /// and error value.
  final Widget Function(BuildContext context, T value) loading;

  /// Widget used when the Future snapshot contains non-null data value.
  final Widget Function(BuildContext context, T value, D data) content;

  /// Widget used when the Future snapshot contains a non-null error value.
  final Widget Function(BuildContext context, T value) error;

  /// Creates a widget that display "Widget" defined according to
  /// multiple states of Future in "ConsumerFutureBuilder"
  ConsumerFutureContainer({
    @required this.future,
    @required this.loading,
    @required this.error,
    @required this.content,
  });

  @override
  Widget build(BuildContext context) {
    return ConsumerFutureBuilder(
      futureBuilder: this.future,
      widgetBuilder: (BuildContext context, T value, AsyncSnapshot<D> snapshot) {
        if (snapshot.hasError) return error(context, value);
        if (!snapshot.hasData) return loading(context, value);
        return content(context, value, snapshot.data);
      },
    );
  }
}