import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';
import 'typedef.dart';

/// A widget using "FutureBuilder" in "Consumer"
class ConsumerFutureBuilder<T, D> extends StatelessWidget {
  /// Build a future on the value from [Consumer<T>]
  final Future<D> Function(BuildContext context, T value) futureBuilder;

  /// Build a widget tree based on the value from [AsyncWidgetBuilder<D>] and [Consumer<T>]
  final ProviderWidgetBuilder<T, D> widgetBuilder;

  /// Wrapping FutureBuilder with Consumer
  ConsumerFutureBuilder({
    Key key,
    @required this.futureBuilder,
    @required this.widgetBuilder,
  })  : assert(futureBuilder != null),
        assert(widgetBuilder != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<T>(
      builder: (context, value, child) {
        return FutureBuilder<D>(
          future: this.futureBuilder(context, value),
          builder: (BuildContext context, AsyncSnapshot<D> snapshot) {
            return this.widgetBuilder(context, value, snapshot);
          },
        );
      },
    );
  }
}