import '../../exception/NoSuchTargetException.dart';
import '../../util/FileUtil.dart';
import '../NetworkService.dart';
import 'IndexFetcher.dart';

class ACEFetcher extends IndexFetcher{
  @override
  Future<String> getIndex(String code) async {
    NetworkService networkSvc = new NetworkService();
    final json = await networkSvc.getJson("https://ace.io/polarisex/oapi/v2/list/tradePrice");
    return json[code]['last_price'];
  }
}