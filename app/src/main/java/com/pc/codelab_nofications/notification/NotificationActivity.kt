package com.pc.codelab_nofications.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.pc.codelab_nofications.R
import com.pc.codelab_nofications.databinding.ActivityMainBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mReceiver = NotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNotificationButtonState(
            isNotifyEnable = true,
            isUpdateEnable = false,
            isCancelEnable = false
        )
        binding.btnNotify.setOnClickListener {
            sendNotification()
        }
        binding.btnUpdate.setOnClickListener {
            updateNotification()
        }
        binding.btnCancel.setOnClickListener {
            cancelNotification()
        }
        createNotificationChannel()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
    }

    private fun sendNotification() {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            updateIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )
        val notifyBuilder: NotificationCompat.Builder = getNotificationBuilder()
        notifyBuilder.addAction(R.drawable.ic_action_name, "Update Notification :3", pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(
            isNotifyEnable = false,
            isUpdateEnable = true,
            isCancelEnable = true
        )
    }

    private lateinit var notificationManager: NotificationManager

    private fun createNotificationChannel() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Mascot Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Android Notification"
        notificationManager.createNotificationChannel(notificationChannel)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, NotificationActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You have been notified !!!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notifyBuilder: NotificationCompat.Builder = getNotificationBuilder()
        notifyBuilder.setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(androidImage)
                .setBigContentTitle("Notification updated!")
        )
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(
            isNotifyEnable = false,
            isUpdateEnable = false,
            isCancelEnable = true
        )
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(
            isNotifyEnable = true,
            isUpdateEnable = false,
            isCancelEnable = false
        )
    }

    @Suppress("SameParameterValue")
    private fun setNotificationButtonState(
        isNotifyEnable: Boolean,
        isUpdateEnable: Boolean,
        isCancelEnable: Boolean
    ) {
        binding.btnNotify.isEnabled = isNotifyEnable
        binding.btnUpdate.isEnabled = isUpdateEnable
        binding.btnCancel.isEnabled = isCancelEnable
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    companion object {
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 0
        const val ACTION_UPDATE_NOTIFICATION = "ACTION_UPDATE_NOTIFICATION"
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }
    }
}
