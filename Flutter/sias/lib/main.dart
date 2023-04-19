import 'package:background_fetch/background_fetch.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
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
void _showNotification(){
  NotificationUtil.showNotification(0, "alarm", "alarm message");
}

void _setSchedule(){
  PeriodicScheduler ps = PeriodicScheduler();
  ps.setAllSchedule();
}



class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>  {

  String _status = 'unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // 初始化插件状态
  Future<void> initPlatformState() async {
    // 配置后台任务
    BackgroundFetch.configure(
      BackgroundFetchConfig(
        minimumFetchInterval: 15, // 指定任务执行间隔时间，单位为分钟
        stopOnTerminate: false, // 是否在应用程序终止时停止任务
        startOnBoot: true, // 是否在设备启动时启动任务
        enableHeadless: true, // 是否在应用程序进程终止时继续运行任务
      ),
          (String taskId) async {
        // 任务执行逻辑
        print('[BackgroundFetch] Task received: $taskId');
        setState(() {
          _status = 'Task received: $taskId';
        });
        BackgroundFetch.finish(taskId);
      },
    );

    // 获取当前任务状态
    var status = await BackgroundFetch.status;
    setState(() {
      _status = status.toString();
    });
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
