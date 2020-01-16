import 'package:flutter/widgets.dart';

/// TODO: add comment
typedef ProviderWidgetBuilder<T, D> = Widget Function(BuildContext context, T value, AsyncSnapshot<D> sanpshot);
