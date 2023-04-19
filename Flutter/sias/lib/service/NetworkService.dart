import 'dart:io';
import 'dart:convert';
import 'package:http/http.dart' as http;

import '../util/FileUtil.dart';


class NetworkService {
  Future<String> downloadFile(String url, String fileName) async {
    if (url.isEmpty) {
      throw ArgumentError('url is empty');
    }

    try {
      final response = await http.get(Uri.parse(url));
      final bytes = response.bodyBytes;
      final savePath = '${await FileUtil.getDownloadDirectoryPath()}/$fileName';
      final file = File(savePath);
      await file.writeAsBytes(bytes);
      return savePath;
    } catch (e) {
      rethrow;
    }
  }

  Future<dynamic> getJson(String url) async {
    http.Response response = await http.get(Uri.parse(url));
    return jsonDecode(response.body);
  }
}