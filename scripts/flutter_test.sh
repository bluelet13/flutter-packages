set -e

cd $1

flutter packages get
flutter format --set-exit-if-changed --line-length 120 lib test
flutter analyze --no-current-package lib test/

cd - # resets to the original state