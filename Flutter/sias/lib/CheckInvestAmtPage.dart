import 'package:flutter/material.dart';
import 'package:sias/service/CheckInvestAmtService.dart';
import 'package:sias/util/DBUtil.dart';


class CheckInvestAmtPage extends StatefulWidget {
  const CheckInvestAmtPage({Key? key}) : super(key: key);

  @override
  CheckInvestAmtPageState createState() => CheckInvestAmtPageState();
}

class CheckInvestAmtPageState extends State<CheckInvestAmtPage> {
  List<Map<String, dynamic>> _data = [];

  @override
  void initState() {
    super.initState();
    _initDataTable();
  }

  Future<void> _refreshDataTable() async {
    await CheckInvestAmtService().checkAllTargetAmt();
    await _initDataTable();
  }

  Future<void> _initDataTable() async {
    final db = await DBUtil.getDatabase();
    final data = await db.query('TRACK_TARGET');
    //db.close();
    setState(() {
      _data = data;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('My Page'),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ElevatedButton(
            onPressed: _refreshDataTable,
            child: const Text('更新標的投資金額'),
          ),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.black,
                  width: 1.0,
                ),
              ),
              child: DataTable(
                columns: const [
                  DataColumn(label: Text('Target')),
                  DataColumn(label: Text('lastCheckDate')),
                  DataColumn(label: Text('lastIndex')),
                  DataColumn(label: Text('InvestAmt')),
                ],
                rows: _data
                    .map((e) => DataRow(cells: [
                          DataCell(Text(e['TARGET_NAME'])),
                          DataCell(Text(e['LAST_CHECK_DATE'].toString().substring(0, 10))),
                          DataCell(Text(e['LAST_CHECK_INDEX'].toString())),
                          DataCell(Text(e['INVEST_AMT'].toString())),
                        ]))
                    .toList(),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
