import 'dart:async';

import 'package:sias/util/NotificationUtil.dart';

class PeriodicScheduler{
  void setAllSchedule() {
    _setCheckInvestAmtSchedule();
  }

  void _setCheckInvestAmtSchedule() {
    Timer.periodic(Duration(days: 1), (timer) {
        final now = DateTime.now();
        if (now.hour == 15 && now.minute == 3) {
          // 在每天晚上8點觸發通知
          NotificationUtil.showNotification(1, "schedule", "notify");
        }
      });
  }
}