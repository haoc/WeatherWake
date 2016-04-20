package cs371m.weatherwake.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cs371m.weatherwake.Alarm;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mathAlarmServiceIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);

        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        Intent alarmAlertActivityIntent;

        alarmAlertActivityIntent = new Intent(context, AlarmWakeActivity.class);

        alarmAlertActivityIntent.putExtra("alarm", alarm);

        alarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(alarmAlertActivityIntent);
    }

}
