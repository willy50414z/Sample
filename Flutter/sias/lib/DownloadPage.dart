import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

class DownloadPage extends StatefulWidget {
  const DownloadPage({Key? key}) : super(key: key);

  @override
  DownloadPageState createState() => DownloadPageState();
}

class DownloadPageState extends State<DownloadPage> {
  final _urlController = TextEditingController();
  String? _errorMessage;

  void downloadFile(final String url, final String fileName) async {
    if (url.isEmpty) {
      setState(() {
        _errorMessage = 'Please enter a valid URL';
      });
      return;
    }

    try {
      final response = await http.get(Uri.parse(url));
      final bytes = response.bodyBytes;
      final savePath = await _getSavePath(fileName);
      final file = File(savePath);
      await file.writeAsBytes(bytes);

      setState(() {
        _errorMessage = null;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('File downloaded to: $savePath'),
        ),
      );
    } catch (e) {
      setState(() {
        _errorMessage = 'Failed to download file: $e';
      });
    }
  }

  void _checkTargetInvestAmount() {}

  Future<String> _getSavePath(String fileName) async {
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

      return '${downloadDirectory.path}/$fileName';
    }
    throw Exception('Failed to get download directory');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Download Page'),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          TextField(
            controller: _urlController,
            decoration: const InputDecoration(
              labelText: 'URL',
              hintText: 'Enter a URL to download a file',
            ),
          ),
          if (_errorMessage != null)
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red),
              ),
            ),
          ElevatedButton(
            onPressed: _checkTargetInvestAmount,
            child: const Text('Download'),
          ),
        ],
      ),
    );
  }
}
