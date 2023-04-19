import 'package:intl/intl.dart';

class DateUtil {
  static String formatDate(DateTime date, String format) {
    return DateFormat(format).format(date);
  }
  static String getDatetimeStr(String format) {
    return DateFormat(format).format(DateTime.now());
  }
  static String getCeDatetimeStr() {
    return (int.parse(DateFormat('yyyyMMdd').format(DateTime.now())) - 19110000).toString();
  }
}