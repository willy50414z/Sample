import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

class NotificationTestPage extends StatefulWidget {
  const NotificationTestPage({Key? key}) : super(key: key);

  @override
  _NotificationPageState createState() => _NotificationPageState();
}

class _NotificationPageState extends State<NotificationTestPage> {
  late FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin;

  @override
  void initState() {
    super.initState();

    // 初始化本地通知插件
    flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();

    // 配置本地通知的初始化设置
    const AndroidInitializationSettings initializationSettingsAndroid =
    AndroidInitializationSettings('ic_launcher');
    final DarwinInitializationSettings initializationSettingsIOS =
    DarwinInitializationSettings();
    final InitializationSettings initializationSettings =
    InitializationSettings(
        android: initializationSettingsAndroid,
        iOS: initializationSettingsIOS);
    flutterLocalNotificationsPlugin.initialize(initializationSettings);
  }

  Future<void> _showNotification() async {
    const AndroidNotificationDetails androidPlatformChannelSpecifics =
    AndroidNotificationDetails(
      'channel_id',
      'channel_name',
      importance: Importance.max,
      priority: Priority.high,
    );
    const DarwinNotificationDetails iosNotificationDetails = DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
      subtitle: 'This is a subtitle',
      sound: 'sound.mp3',
      badgeNumber: 1,
      threadIdentifier: 'thread_id',
      categoryIdentifier: 'category_id',
      attachments: [
        // IOSNotificationAttachment(
        //   identifier: 'image',
        //   filePath: 'assets/images/image.png',
        // ),
      ],
    );
    const NotificationDetails platformChannelSpecifics = NotificationDetails(
        android: androidPlatformChannelSpecifics,
        iOS: iosNotificationDetails);

    await flutterLocalNotificationsPlugin.show(
        0, '通知标题', '通知内容', platformChannelSpecifics);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('本地通知示例'),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: _showNotification,
          child: const Text('发送通知'),
        ),
      ),
    );
  }
}
