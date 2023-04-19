import '../../exception/NoSuchTargetException.dart';
import '../../util/FileUtil.dart';
import '../NetworkService.dart';
import 'IndexFetcher.dart';

class TWSEFetcher extends IndexFetcher{
  @override
  Future<String> getIndex(String code) async {
    NetworkService networkSvc = new NetworkService();
    String filePath = await networkSvc.downloadFile("https://openapi.twse.com.tw/v1/exchangeReport/STOCK_DAY_AVG_ALL", getJsonFileName('TWSE'));
    final json = await FileUtil.loadJsonFromAsset(filePath);
    for (final item in json) {
      if (item['Code'] == code) {
        return item['ClosingPrice'];
      }
    }
    throw NoSuchTargetException('Market[TWSE]Code[${code}]');
  }
}