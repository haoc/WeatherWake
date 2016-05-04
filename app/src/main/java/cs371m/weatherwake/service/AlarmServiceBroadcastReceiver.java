package cs371m.weatherwake.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmServiceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmServiceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "DebugSnooze: ");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }
}
