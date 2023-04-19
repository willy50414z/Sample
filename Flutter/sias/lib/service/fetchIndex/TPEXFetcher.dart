import '../../exception/NoSuchTargetException.dart';
import '../../util/FileUtil.dart';
import '../NetworkService.dart';
import 'IndexFetcher.dart';

class TPEXFetcher extends IndexFetcher{
  @override
  Future<String> getIndex(String code) async {
    NetworkService networkSvc = new NetworkService();
    String filePath = await networkSvc.downloadFile("https://www.tpex.org.tw/openapi/v1/tpex_mainboard_quotes", getJsonFileName('TPEX'));
    final json = await FileUtil.loadJsonFromAsset(filePath);
    for (final item in json) {
      if (item['SecuritiesCompanyCode'] == code) {
        return item['Close'];
      }
    }
    throw NoSuchTargetException('Market[TPEX]Code[${code}]');
  }
}