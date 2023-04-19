import 'package:flutter/material.dart';
import 'package:sias/util/DBUtil.dart';
import 'package:sqflite/sqflite.dart';

class SqlQueryPage extends StatefulWidget {
  const SqlQueryPage({Key? key}) : super(key: key);

  @override
  _SqlQueryPageState createState() => _SqlQueryPageState();
}

class _SqlQueryPageState extends State<SqlQueryPage> {
  TextEditingController _sqlController = TextEditingController();
  List<Map<String, dynamic>> _queryResult = [];

  @override
  void dispose() {
    _sqlController.dispose();
    super.dispose();
  }

  Future<void> _executeSqlQuery() async {
    String sql = _sqlController.text.trim();
    if (sql.isNotEmpty) {
      Database db = await DBUtil.getDatabase();

      List<Map<String, dynamic>> result = await db.rawQuery(sql);

      setState(() {
        _queryResult = result;
      });
      db.close();
    }

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('SQL Query'),
      ),
      body: Column(
        children: [
          Row(children: [
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                controller: _sqlController,
                maxLines: null,
                decoration: InputDecoration(
                  hintText: 'Enter your SQL query here',
                  border: OutlineInputBorder(),
                ),
              ),
            ),
          ),
          ElevatedButton(
            onPressed: _executeSqlQuery,
            child: Text('Execute'),
          )]),
          Expanded(
    child: SingleChildScrollView(
    scrollDirection: Axis.vertical,
            child: SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: _queryResult.isNotEmpty // 確保查詢結果不為空
                  ? DataTable(
                columns: _queryResult.first.keys
                    .map((String key) => DataColumn(label: Text(key)))
                    .toList(),
                rows: _queryResult.map((Map<String, dynamic> row) {
                  return DataRow(
                    cells: row.keys.map((String key) {
                      return DataCell(Text(row[key].toString()));
                    }).toList(),
                  );
                }).toList(),
              )
                  : Container(),
            ),
    ),
          ),
        ],
      ),
    );
  }
}