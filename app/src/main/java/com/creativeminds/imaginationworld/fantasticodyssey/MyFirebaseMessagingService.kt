package com.creativeminds.imaginationworld.fantasticodyssey

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.creativeminds.imaginationworld.fantasticodyssey.view.SplashFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

const val channelId = "notification_channel"
const val channelName = "com.creativeminds.imaginationworld.fantasticodyssey"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val data: Map<String, String> = remoteMessage.data
        val notification: RemoteMessage.Notification? = remoteMessage.notification
        val image = waitForBitmap(notification?.imageUrl.toString())

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationChannel.description = ""
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.lockscreenVisibility = View.VISIBLE
            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(
            this,
            channelId
        )

        val notificationIntent = Intent(this, MainActivity::class.java)

        if (data.containsKey("link")) {
            notificationIntent.putExtra("link", data["link"].toString())
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentTitle(notification?.title.toString())
            .setContentText(notification?.body.toString())
            .setLargeIcon(image)
            .setContentIntent(contentIntent)
            .setColorized(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(0, 1000, 500, 1000))

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun waitForBitmap(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
