package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.StudyTimerActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PrefUtil.setTimerState(StudyTimerActivity.TimerStatus.STOPPED, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}