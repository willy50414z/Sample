import 'package:background_fetch/background_fetch.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:intl/intl.dart';
import 'package:sias/scheduler/PeriodicScheduler.dart';
import 'package:sias/util/DBUtil.dart';
import 'package:sias/util/NotificationUtil.dart';
import 'CheckInvestAmtPage.dart';

import 'DownloadPage.dart';
import 'InvestAmtCaculatorPage.dart';
import 'NotificationTestPage.dart';
import 'SqlQueryPage.dart';

FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await DBUtil.initializeDatabase();
  WidgetsFlutterBinding.ensureInitialized();

  await BackgroundFetch.configure(
    BackgroundFetchConfig(
      minimumFetchInterval: 1,
      stopOnTerminate: false,
      enableHeadless: true,
      requiresBatteryNotLow: false,
      requiresCharging: false,
      requiresDeviceIdle: false,
      requiresStorageNotLow: false,
    ),
        (String taskId) async {
          final DateFormat formatter = DateFormat('yyyy-MM-dd HH:mm:ss');
          final String formatted = formatter.format(DateTime.now());

          NotificationUtil.showNotification(1, "Schedule Notify", "Current time is: $formatted, taskId: $taskId");

          BackgroundFetch.finish(taskId);
    },
  );
  // initBackgroundFetch();
  runApp(MaterialApp(
    home: MyHomePage(),
  ));
  // print('000');
  // runApp(Center(child:Text('first App')))

  // final url = 'https://www.tpex.org.tw/openapi/v1/tpex_mainboard_quotes';
  // final savePath = 'D:/test.json';
  // final file = await downloadFile(url, savePath);
  // print('File downloaded to: ${file.path}');
}


class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>  {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("My Page"),
        leading: Builder(
          builder: (BuildContext context) {
            return IconButton(
              icon: Icon(Icons.menu),
              onPressed: () => Scaffold.of(context).openDrawer(),
            );
          },
        ),
      ),
      drawer: Drawer(
        child: ListView(
          children: [
            ExpansionTile(
              title: Text("Invest"),
              children: [
                ListTile(
                  title: Text("標的投資金額"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => CheckInvestAmtPage()),
                    );
                  },
                ),
                ListTile(
                  title: Text("標的投資金額設定"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => InvestAmtCaculatorPage()),
                    );
                  },
                ),
              ],
            ),
            ExpansionTile(
              title: Text("Practice"),
              children: [
                ListTile(
                  title: Text("下載檔案"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => DownloadPage()),
                    );
                  },
                ),
                ListTile(
                  title: Text("SQL Editor"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const SqlQueryPage()),
                    );
                  },
                ),
                ListTile(
                  title: Text("Notification test"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => NotificationTestPage()),
                    );
                  },
                ),
              ],
            ),
            ExpansionTile(
              title: Text("Util"),
              children: [
                ListTile(
                  title: Text("Sub A"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => CheckInvestAmtPage()),
                    );
                  },
                ),
                ListTile(
                  title: Text("Sub B"),
                  onTap: () {
                    Navigator.pop(context);
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => CheckInvestAmtPage()),
                    );
                  },
                ),
              ],
            ),
          ],
        ),
      ),
      body: Container(
        child: Center(
          child: Text("This is my page!"),
        ),
      ),
    );
  }
}
