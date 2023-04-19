import 'dart:io';

import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../Constant.dart';

class DBUtil {
  static Future<Database> getDatabase() async {
    final databasePath = await getDatabasesPath();
    final path = join(databasePath, Constant.dbName);
    return openDatabase(
      path,
      version: 1,
    );
  }

  static Future<void> initializeDatabase() async {
    final prefs = await SharedPreferences.getInstance();
    bool hasInserted = prefs.getBool('hasInserted') ?? false;

    // final databasePath = await getDatabasesPath();
    // final path = join(databasePath, Constant.dbName);
    // await File(path).delete();

    if (!hasInserted) {
      final db = await getDatabase();

      //DDL
      for (String sql in Constant.initDDLSql) {
        await db.execute(sql);
      }

      //DML
      for (String sql in Constant.initDmlSql) {
        await db.execute(sql);
      }
      db.close();
    }
    await prefs.setBool('hasInserted', true);
  }
}
