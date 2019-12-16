@file:Suppress("DEPRECATION")

package com.adnet.workmanagernotification

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class NotifyWork(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    private var downloadBroadcastReceiver: DownloadReceiver? = null
    private val url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private val title = "Hientest"
    private val desc = "下载"

    override fun doWork(): Result {

        //val downloadManager = getSystemService(applicationContext.DOWNLOAD_SERVICE) as DownloadManager
//        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
//        sendNotification(id, 0, "Tai xuong")
//        try {
//            val outputFile =
//                File(Environment.getExternalStorageDirectory(), "inducesmile.mp4")
//            Log.d("Hien", outputFile.toString())
//            val url =
//                URL("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
//            val urlConnection: URLConnection = url.openConnection()
//            urlConnection.connect()
//            val fileLength: Int = urlConnection.contentLength
//            val fos = FileOutputStream(outputFile)
//            val inputStream: InputStream = urlConnection.getInputStream()
//            val buffer = ByteArray(1024)
//            var len: Int
//            var total: Long = 0
//            while (inputStream.read(buffer).also { len = it } > 0) {
//                total += len.toLong()
//                val percentage = (total * 100 / fileLength).toInt()
//                EventBus.getDefault().post(MyEvent(percentage))
//                if (percentage == 100) {
//                    sendNotification(id, 100, "The end game")
//                }
//                fos.write(buffer, 0, len)
//            }
//            fos.close()
//            inputStream.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d("Hien", Result.failure().toString())
//            return Result.failure()
//        }


        return success()
    }


    private fun sendNotification(id: Int, per: Int, s: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //val titleNotification = applicationContext.getString(R.string.notification_title)
        // val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val pendingIntent =
            getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(s).setContentText("Downing")
            //  .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent).setAutoCancel(false)

        notification.priority = PRIORITY_MAX
        if (per == 0) {
            notification.setProgress(0, 100, true)
        }

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            // val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            // channel.enableVibration(true)
            // channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            // channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }
}
