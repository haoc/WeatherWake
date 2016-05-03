package cs371m.weatherwake.service;

import cs371m.weatherwake.database.database;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.alert.AlarmAlertBroadcastReceiver;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmService extends Service {

    private static final String TAG = "AlarmService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "DebugSnooze: onCreate");
        Log.d(this.getClass().getSimpleName(), "onCreate()");
        super.onCreate();
    }

    private Alarm getNext(){
        Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                int result = 0;
                long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();
                if (diff > 0){
                    return 1;
                } else if (diff < 0){
                    return -1;
                }
                return result;
            }
        });

        database.init(getApplicationContext());
        List<Alarm> alarms = database.getAllAlarms();

        for (Alarm alarm : alarms){
            if(alarm.getAlarmActive())
                alarmQueue.add(alarm);
        }
        if (alarmQueue.iterator().hasNext()){
            return alarmQueue.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        database.deactivate();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(),"onStartCommand()");
        Log.d(TAG, "DebugSnooze: onStartCommand");
        Alarm alarm = getNext();
        Log.d(TAG, "DebugSnooze: alarm getNext(): " + alarm);
        if (alarm != null){
            Log.d(TAG, "DebugSnooze: alarm != null");
            alarm.schedule(getApplicationContext());
            Log.d(this.getClass().getSimpleName(), alarm.getTimeUntilNextAlarmMessage());

        } else {
            Log.d(TAG, "DebugSnooze: alarm == null");
            Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
            myIntent.putExtra("alarm", new Alarm());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
        }
        return START_NOT_STICKY;
    }
}
