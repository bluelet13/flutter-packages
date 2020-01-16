import 'package:flutter/widgets.dart';
import 'consumer_stream.dart';

/// TODO: add comment
class ConsumerStreamContainer<T, D> extends StatelessWidget {
  /// TODO: add comment
  final Stream<D> Function(BuildContext context, T value) stream;

  /// TODO: add comment
  final Widget Function(BuildContext context, T value) loading;

  /// TODO: add comment
  final Widget Function(BuildContext context, T value, D data) content;

  /// TODO: add comment
  final Widget Function(BuildContext context, T value) error;

  /// TODO: add comment
  ConsumerStreamContainer({
    @required this.stream,
    @required this.loading,
    @required this.error,
    @required this.content,
  });

  @override
  Widget build(BuildContext context) {
    return ConsumerStreamBuilder(
      streamBuilder: this.stream,
      widgetBuilder: (BuildContext context, T value, AsyncSnapshot<D> snapshot) {
        if (snapshot.hasError) return error(context, value);
        if (!snapshot.hasData) return loading(context, value);
        return content(context, value, snapshot.data);
      },
    );
  }
}
