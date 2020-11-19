package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.StudyTimerActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.AppConstants
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.NotificationUtil
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                Log.d("masuk stop", "masuk stop")
                StudyTimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(StudyTimerActivity.TimerStatus.STOPPED, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                PrefUtil.setTimerState(StudyTimerActivity.TimerStatus.STARTED, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context)
            }
        }
    }
}