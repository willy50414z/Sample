import '../../util/DateUtil.dart';

abstract class IndexFetcher {

  Future<String> getIndex(String code);
  String getJsonFileName(String market) {
    return '${market}_${DateUtil.getDatetimeStr('yyyyMMdd')}.json';
  }
}