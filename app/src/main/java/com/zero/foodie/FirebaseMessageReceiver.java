package com.zero.foodie;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    public void
    onMessageReceived(RemoteMessage remoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.logo);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {

        Intent intent
                = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);



        builder = builder.setContent(
                getCustomDesign(title, message));

        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
            final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        notificationManager.notify(0, builder.build());
    }
}
