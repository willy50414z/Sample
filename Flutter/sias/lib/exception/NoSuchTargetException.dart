class NoSuchTargetException implements Exception {
  final String message;

  NoSuchTargetException(this.message);

  @override
  String toString() => 'NoSuchTargetException: $message';
}