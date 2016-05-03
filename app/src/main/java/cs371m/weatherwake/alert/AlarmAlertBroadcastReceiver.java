package cs371m.weatherwake.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.service.AlarmServiceBroadcastReceiver;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = "AlarmAlertBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(context, AlarmServiceBroadcastReceiver.class);
        context.sendBroadcast(alarmServiceIntent, null);

        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        Intent alarmAlertActivityIntent;

        alarmAlertActivityIntent = new Intent(context, AlarmWakeActivity.class);

        alarmAlertActivityIntent.putExtra("alarm", alarm);

        alarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(alarmAlertActivityIntent);
        Log.d(TAG, "startActivity(alarmAlertActivityIntent)" + "TestTest");
    }

}
