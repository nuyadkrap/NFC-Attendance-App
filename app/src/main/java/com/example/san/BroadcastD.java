package com.example.san;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    String channelId = "channel";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    Notification Notifi;
    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.createNotificationChannel(mChannel);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(R.drawable.on).setTicker("HETT").setWhen(System.currentTimeMillis())
//                .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
//
//        notificationmanager.notify(1, builder.build());

        Notifi = new Notification.Builder(context, channelId)
                .setContentTitle("5분 후 수업시작입니다")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.on) //아이콘을 제대로 설정하지 않으면 오류메시지 없이 작동하지 않으니 주의할 것!!!
                .setTicker("알림!!!")
                .setContentIntent(pendingIntent)
                .build();

        Notifi.defaults = Notification.DEFAULT_VIBRATE;

        //알림 소리를 한번만 내도록
        Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        //확인하면 자동으로 알림이 제거 되도록
        Notifi.flags = Notification.FLAG_AUTO_CANCEL;

        notificationmanager.notify( 777 , Notifi);
    }
}
