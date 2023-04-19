import '../../exception/NoSuchTargetException.dart';
import '../../util/FileUtil.dart';
import '../NetworkService.dart';
import 'IndexFetcher.dart';

class TAIEXFetcher extends IndexFetcher{
  @override
  Future<String> getIndex(String code) async {
    NetworkService networkSvc = new NetworkService();
    String filePath = await networkSvc.downloadFile("https://openapi.twse.com.tw/v1/indicesReport/MI_5MINS_HIST", getJsonFileName('TAIEX'));
    final json = await FileUtil.loadJsonFromAsset(filePath);
    return json.last['ClosingIndex'];
  }
}