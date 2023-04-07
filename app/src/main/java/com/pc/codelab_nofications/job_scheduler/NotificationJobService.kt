package com.pc.codelab_nofications.job_scheduler

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat


class NotificationJobService() : JobService() {
    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    private lateinit var notifyManager: NotificationManager

    private fun createNotificationChannel() {
        notifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Notification channels are only available in OREO and higher.
        // So, add a check on the SDK version.
        // Notification channels are only available in OREO and higher.
        // So, add a check on the SDK version.

        // Create the NotificationChannel with all the parameters.
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Job Service notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Notifications from Job Service"
        notifyManager.createNotificationChannel(notificationChannel)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        createNotificationChannel()
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, JobSchedulerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Your Job ran to completion!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_menu_more)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)

        notifyManager.notify(0, builder.build())
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}
