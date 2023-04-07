package com.pc.codelab_nofications.job_scheduler

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pc.codelab_nofications.databinding.ActivityJobSchedulerBinding


class JobSchedulerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobSchedulerBinding
    private var mScheduler: JobScheduler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobSchedulerBinding.inflate(layoutInflater)
        mScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        setContentView(binding.root)
        scheduleJob()
        setupSeekBar()
    }

    private fun setupSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    binding.seekBarProgress.text = "$progress s"
                } else {
                    binding.seekBarProgress.text = "Not Set"
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun scheduleJob() {
        val selectedNetworkID = binding.networkOptions.checkedRadioButtonId

        val seekBarInteger = binding.seekBar.progress
        val seekBarSet = seekBarInteger > 0

        val selectedNetworkOption = when (selectedNetworkID) {
            binding.noNetwork.id -> JobInfo.NETWORK_TYPE_NONE
            binding.anyNetwork.id -> JobInfo.NETWORK_TYPE_ANY
            else -> JobInfo.NETWORK_TYPE_UNMETERED
        }

        val mDeviceIdleSwitch = binding.idleSwitch
        val mDeviceChargingSwitch = binding.chargingSwitch

        val serviceName = ComponentName(packageName, NotificationJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(selectedNetworkID)
            .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked)
            .setRequiresCharging(mDeviceChargingSwitch.isChecked)

        if (seekBarSet) {
            builder.setOverrideDeadline(seekBarInteger * 1000L)
        }

        val constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE)
            || mDeviceIdleSwitch.isChecked
            || mDeviceChargingSwitch.isChecked
            || seekBarSet

        if (constraintSet) {
            val myJobInfo: JobInfo = builder.build()
            mScheduler?.schedule(myJobInfo)
            Toast.makeText(
                this,
                "Job Scheduled, job will run when the constraints are met",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_SHORT).show()
        }
    }

    fun cancelJob() {
        mScheduler?.cancelAll()
        mScheduler = null
        Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show()

    }

    companion object {
        const val JOB_ID = 0
    }
}
