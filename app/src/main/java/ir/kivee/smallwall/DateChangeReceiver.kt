package ir.kivee.smallwall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by payam on 8/8/17.
 */
class DateChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_DATE_CHANGED)  ||
                intent.action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            var netUtils = NetUtils(context)
            netUtils.mainStack()
        }
    }
}