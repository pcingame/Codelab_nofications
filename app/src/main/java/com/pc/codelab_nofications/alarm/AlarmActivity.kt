package com.pc.codelab_nofications.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import com.pc.codelab_nofications.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        setupAlarm()
        createNotificationChannel()
    }

    private fun setupAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val alarmUp = (PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE
        ) != null)
        binding.alarmToggle.isChecked = alarmUp
        val notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        binding.alarmToggle.setOnCheckedChangeListener { _, isChecked ->
            var msg = ""
            msg = if (isChecked) {
                //val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
                val repeatInterval = 1000L
                val triggerTime = SystemClock.elapsedRealtime() + repeatInterval
                alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    repeatInterval,
                    notifyPendingIntent
                )
                "Stand Up Alarm On !!!"
            } else {
                alarmManager.cancel(notifyPendingIntent)
                notificationManager.cancelAll()
                "Stand Up Alarm Off !!!"
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun createNotificationChannel() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true);
            notificationChannel.description = "Notifies every 15 minutes to stand up and walk "
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}
