import 'package:flutter_local_notifications/flutter_local_notifications.dart';

import '../main.dart';

class NotificationUtil {
  static late FlutterLocalNotificationsPlugin _flutterLocalNotificationsPlugin;
  static NotificationDetails? _platformChannelSpecifics = null;

  static _init() {
    // 初始化本地通知插件
    _flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();

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
    _platformChannelSpecifics = NotificationDetails(
        android: androidPlatformChannelSpecifics,
        iOS: iosNotificationDetails);
  }
  static Future<void> showNotification(int id,String title, String body) async {
    if(_platformChannelSpecifics == null) {
      _init();
    }
    await _flutterLocalNotificationsPlugin.show(
        id, title, body, _platformChannelSpecifics);
  }
}