package fr.uge.review.broadcastReceiver

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.uge.review.R

class NewAnswerReceiver : BroadcastReceiver() {

    private var idNotification : Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("RECEIVED", "OMGGGGG")
        NotificationUtils.showNotification(context, idNotification++)
    }
}

class NotificationUtils {
    companion object {
        const val CHANNEL_ID=  "Message"
        const val CHANNEL_NAME=  "MessageChannel"

        fun showNotification(
            context: Context,
            idNotification: Int
        ) {
            Log.i("BEFORE SENDIND", "WE ARE NEAR")
            notificationCard(context, idNotification)
        }
    }
}

@SuppressLint("ObsoleteSdkInt")
fun notificationCard(context: Context, idNotification: Int) {
    val notificationManager = NotificationManagerCompat.from(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            NotificationUtils.CHANNEL_ID,
            NotificationUtils.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    Log.i("NotifCreated", "WE DID IT")
    val notification = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
        .setContentTitle(context.getString(R.string.titleNotification))
        .setContentText(context.getString(R.string.messageNotification))
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    notificationManager.notify(idNotification, notification)
}