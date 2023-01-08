package com.example.hilass

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            val notification = remoteMessage.notification!!
            val customContentView = RemoteViews("com.example.hilass", R.layout.notification)
            customContentView.setTextViewText(R.id.tvTitle, notification.title)
            customContentView.setTextViewText(R.id.tvMessage, notification.body)
            customContentView.setImageViewResource(R.id.ivAppLogo, R.drawable.ic_notification)

            val customBigContentView = RemoteViews(packageName, R.layout.notification)
            customBigContentView.setTextViewText(R.id.tvTitle, notification.title)
            customBigContentView.setTextViewText(R.id.tvMessage, notification.body)
            customBigContentView.setImageViewResource(R.id.ivAppLogo, R.drawable.ic_notification)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(this, MenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setCustomContentView(customContentView)
                .setCustomBigContentView(customBigContentView)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setLights(Color.GRAY, 1000, 5000)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}
