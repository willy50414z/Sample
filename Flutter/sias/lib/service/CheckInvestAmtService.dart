import 'package:sias/service/fetchIndex/TWSEFetcher.dart';
import 'package:sqflite/sqflite.dart';

import '../util/DBUtil.dart';
import '../util/DateUtil.dart';
import '../util/NotificationUtil.dart';
import 'fetchIndex/ACEFetcher.dart';
import 'fetchIndex/IndexFetcher.dart';
import 'fetchIndex/TAIEXFetcher.dart';
import 'fetchIndex/TPEXFetcher.dart';

class CheckInvestAmtService {
  Future<void> checkAllTargetAmt() async {
    Database db = await DBUtil.getDatabase();
    List<Map<String, Object?>> trackTargetList = await db.query('TRACK_TARGET');

    for (Map<String, Object?> data in trackTargetList) {
      String index = await _getIndex(
          data["TARGET_PROC"].toString(), data["TARGET_PROC_ARGS"].toString());
      int newInvestAmt = await _getInvestAmt(db, data['ID'].toString(), index);
      _updateInvestAmt(db, data['ID'].toString(), index, newInvestAmt);


      if (newInvestAmt != int.parse(data['INVEST_AMT'].toString())) {
        await NotificationUtil.showNotification(
            int.parse(data['ID'].toString()),
            '投資金額異動通知',
            "[${data['TARGET_NAME']}]投資金額異動，更新前[${data['INVEST_AMT']}]更新後[$newInvestAmt]");
      }
    }
    // db.close();
  }

  void _updateInvestAmt(Database db, String id, String index, int amt) {
    db.rawUpdate(
        'UPDATE TRACK_TARGET SET LAST_CHECK_DATE=?, LAST_CHECK_INDEX=?, INVEST_AMT=? WHERE ID=?',
        [DateUtil.getDatetimeStr('yyyy-MM-dd HH:mm:ss'), index, amt, id]);
  }

  Future<int> _getInvestAmt(Database db, String id, String? index) async {
    List<Map<String, dynamic>> results = await db.rawQuery(
      'SELECT AMT FROM TRACK_LIST WHERE TRACK_TARGET=? AND UP_LIMIT>? AND DN_LIMIT<?',
      [id, index, index],
    );
    if (results.isEmpty) {
      return -1;
    }
    return results[0]['AMT'];
  }

  Future<String> _getIndex(String market, String code) async {
    IndexFetcher? indexFetcher;
    if (market != null) {
      if ("TWSE" == market) {
        indexFetcher = TWSEFetcher();
      } else if ("TPEX" == market) {
        indexFetcher = TPEXFetcher();
      } else if ("ACE" == market) {
        indexFetcher = ACEFetcher();
      } else if ("TAIEX" == market) {
        indexFetcher = TAIEXFetcher();
        code = DateUtil.getCeDatetimeStr();
      } else {
        indexFetcher = null;
      }
    }
    if (indexFetcher != null) {
      return await indexFetcher.getIndex(code);
    }
    return "-1";
  }
}
