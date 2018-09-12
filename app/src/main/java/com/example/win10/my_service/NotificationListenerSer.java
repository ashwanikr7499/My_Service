package com.example.win10.my_service;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListenerSer extends NotificationListenerService
{

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
