import 'dart:io';

import 'package:path_provider/path_provider.dart';
import 'dart:async' show Future;
import 'dart:convert' show json;
import 'package:flutter/services.dart' show rootBundle;

class FileUtil {
  static Future<String?> getDownloadDirectoryPath() async {
    Directory? directory;
    if (Platform.isAndroid) {
      directory = await getExternalStorageDirectory();
    } else if (Platform.isIOS) {
      directory = await getApplicationDocumentsDirectory();
    }
    if (directory != null) {
      final downloadDirectory = Directory('${directory.path}/Download');
      if (!await downloadDirectory.exists()) {
        await downloadDirectory.create();
      }

      return downloadDirectory.path;
    }
  }

  static Future<List<dynamic>> loadJsonFromAsset(
      String path) async {
    final file = File(path);
    String jsonStr = await file.readAsString();
    List<dynamic> jsonResponse = json.decode(jsonStr);
    return jsonResponse;
  }
}
