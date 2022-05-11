package com.example.shareart.channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.shareart.R;
import com.example.shareart.activities.ArgitarapenBakarraActivity;
import com.example.shareart.activities.HomeActivity;
import com.example.shareart.activities.KomentarioakActivity;
import com.example.shareart.providers.NotificationProvider;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.example.shareart";
    private static final String CHANNEL_NAME = "ShareArt"; // El nombre del tipo de notificación

    private NotificationManager manager;

    private NotificationProvider notificationProvider;

    public NotificationHelper(Context context) {
        super(context);
        notificationProvider=new NotificationProvider();
        createChannels();
    }

    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getNotification(String title, String body) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_shareart)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));

        Intent notficationIntent = new Intent(this, ArgitarapenBakarraActivity.class);
        notficationIntent.putExtra("postId", notificationProvider.getPostId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notficationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        return notificationBuilder;
    }
}
