import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';
import 'typedef.dart';

/// A widget using "StreamBuilder" in "Consumer"
class ConsumerStreamBuilder<T, D> extends StatelessWidget {
  /// Build a stream on the value from [Consumer<T>]
  final Stream<D> Function(BuildContext context, T value) streamBuilder;

  /// Build a widget tree based on the value from [AsyncWidgetBuilder<D>] and [Consumer<T>]
  final ProviderWidgetBuilder<T, D> widgetBuilder;

  /// Wrapping StreamBuilder with Consumer
  ConsumerStreamBuilder({
    Key key,
    @required this.streamBuilder,
    @required this.widgetBuilder,
  })  : assert(streamBuilder != null),
        assert(widgetBuilder != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<T>(
      builder: (context, value, child) {
        return StreamBuilder<D>(
          stream: this.streamBuilder(context, value),
          builder: (BuildContext context, AsyncSnapshot<D> snapshot) {
            return this.widgetBuilder(context, value, snapshot);
          },
        );
      },
    );
  }
}
