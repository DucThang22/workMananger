package com.adnet.workmanagernotification

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
//            if (MyApplication.downloadId == id) {
//               // installApk(context, id)
//               // MyApplication.downloadId = 0L // 重置
//            }
        } else if (intent.action == DownloadManager.ACTION_NOTIFICATION_CLICKED) {
            val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
            viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(viewDownloadIntent)
        }
    }
}
