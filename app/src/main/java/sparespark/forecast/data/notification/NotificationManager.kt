package sparespark.forecast.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import sparespark.forecast.R
import sparespark.forecast.ui.MainActivity

object NotificationManager {

    // FOREGROUND
    private const val FORE_NOTIFICATION_CHANNEL_ID = "winter.services"
    private const val FORE_CHANNEL_NAME = "Winter Services"

    // TEMP NOTIFY
    private const val CHANNEL_ID = "winter"
    private const val CHANNEL_NAME = "Dear Winter"
    private const val TEMP_NOTIFICATION_ID = 0

    @RequiresApi(Build.VERSION_CODES.O)
    fun startForegroundServiceNotification(context: Context): Notification {
        val notificationChannel = NotificationChannel(
            FORE_NOTIFICATION_CHANNEL_ID,
            FORE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_NONE
        )
        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(notificationChannel)
        val notificationBuilder =
            NotificationCompat.Builder(context, FORE_NOTIFICATION_CHANNEL_ID)
        return notificationBuilder.setOngoing(true)
            .setContentTitle("Winter App is on.")
            .setContentText("Tap to update service status.")
            .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setSmallIcon(R.drawable.ic_cloud)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showTempInfoNotification(
        context: Context, title: String, content: String, playSound: Boolean
    ) {
        val nBuilder = getBasicNotificationBuilder(
            context,
            CHANNEL_ID,
            playSound
        )
        nBuilder.setContentTitle(title)
            .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
            .setContentText(content)
        val nManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.createNotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            playSound
        )
        nManager.notify(TEMP_NOTIFICATION_ID, nBuilder.build())
    }

    private fun getBasicNotificationBuilder(
        context: Context,
        channelId: String,
        playSound: Boolean
    ): NotificationCompat.Builder {
        val notificationSound: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val nBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_umb)
            .setAutoCancel(true)
            .setDefaults(0)
        if (playSound) nBuilder.setSound(notificationSound)
        return nBuilder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationManager.createNotificationChannel(
        channelID: String,
        channelName: String,
        playSound: Boolean
    ) {
        val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
        else NotificationManager.IMPORTANCE_LOW
        val nChannel = NotificationChannel(channelID, channelName, channelImportance)
        nChannel.enableLights(true)
        nChannel.lightColor = Color.BLUE
        this.createNotificationChannel(nChannel)
    }

    private fun <T> getPendingIntentWithStack(
        context: Context, javaClass: Class<T>
    ): PendingIntent {
        val resultIntent = Intent(context, javaClass)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(javaClass)
        stackBuilder.addNextIntent(resultIntent)

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
