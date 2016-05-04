package cs371m.weatherwake.alert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.R;
import cs371m.weatherwake.service.AlarmServiceBroadcastReceiver;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmWakeActivity extends Activity implements View.OnClickListener{

    private final static String TAG = "AlarmWakeActivity";

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private Vibrator vibrator;

    private boolean alarmActive;

    private RelativeLayout mAlarmScreenLayout;

    private TextView weatherTypeView;
    private TextView mDateTime;

    private Button snoozeButton;
    private Button turnOffAlarmButton;

    private TextView alarmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        // show wake screen even when screen is locked
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        // keep screen on when the alarm is going off
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.weather_wake_alarm_screen);

        setViewInfo();

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        this.setTitle(alarm.getAlarmName());

        // need to use weather api to get icon for the weather
        weatherTypeView = (TextView) findViewById(R.id.weatherType);
        weatherTypeView.setText("Overcast".toString());

        snoozeButton = (Button) findViewById(R.id.snooze);

        weatherTypeView.setText("Overcast".toString());

        snoozeButton.setOnClickListener(this);
        turnOffAlarmButton.setOnClickListener(this);

        // get music title
        Uri uri = Uri.parse(alarm.getAlarmMusic());
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);

        alarmMusic = (TextView) findViewById(R.id.alarmSong);
        String musicText = "Music playing: ";

        musicText += ringtone.getTitle(this);
        alarmMusic.setText(musicText);


        // provide information about the telephony services and determine the phone state
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);


        Calendar startUp = Calendar.getInstance();
        if(startUp.get(Calendar.HOUR_OF_DAY) >= 7 && startUp.get(Calendar.HOUR_OF_DAY) <= 19) {
            mAlarmScreenLayout.setBackgroundResource(R.drawable.morning_sky5);
        }
        else {
            mAlarmScreenLayout.setBackgroundResource(R.drawable.night_sky2);
        }


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while(!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Get day
                                String weekDay;
                                SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.US);

                                Calendar calendar = Calendar.getInstance();
                                weekDay = dayFormat.format(calendar.getTime());

                                // Get time
                                Date currentTime = calendar.getTime();
                                Log.d("somthing", currentTime.toString());
                                DateFormat date = new SimpleDateFormat("hh:mm a");
                                String localTime = date.format(currentTime);

                                mDateTime.setText(weekDay + ", " + localTime);
                            }
                        });
                    }
                } catch(InterruptedException e) {}
            }
        };
        t.start();

        // listen and see if phone has incoming call or idle
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    // if incoming call, pause music
                    case TelephonyManager.CALL_STATE_RINGING:
                        try {
                            mediaPlayer.pause();

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        break;
                    // resume music when phone state is back to idle
                    case TelephonyManager.CALL_STATE_IDLE:
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        startAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    // check if music should be playing and vibrate
    private void startAlarm() {
        if (alarm.getAlarmMusic() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.getAlarmVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = { 1000, 200, 200, 200 };
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this, Uri.parse(alarm.getAlarmMusic()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }
    }

    // allow exit of AlarmWakeScreen if "Ok" button is pressed
    @Override
    public void onBackPressed() {
        if (!alarmActive) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release wakelock
        WakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null) {
                vibrator.cancel();
            }
        } catch (Exception e) {
            vibrator.cancel();
            e.printStackTrace();
        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {
            mediaPlayer.stop();
            e.printStackTrace();
        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {
            mediaPlayer.release();
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (!alarmActive) {
            return;
        }
        String button = (String) view.getTag();
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        // Button Ok
        if (button.equalsIgnoreCase("ok")) {
            Log.d(TAG, "Ok Button is pressed");
            alarmActive = false;
            vibrator.cancel();
            mediaPlayer.stop();
        }
        // Button Snooze
        if (button.equalsIgnoreCase("snooze")) {
//            database.init(getApplicationContext());
            Log.d(TAG, "DebugSnooze: Snooze Button is pressed");
            Log.d(TAG, "DebugSnooze: alarmId: " + alarm.getAlarmId());
            vibrator.cancel();
            mediaPlayer.stop();

            // update the database
            // current time + snooze time
            Calendar newAlarmTime = Calendar.getInstance();
            Log.d(TAG, "DebugSnooze: getAlarmTImeString: " + alarm.getAlarmTimeString());

            String[] timeArray = alarm.getAlarmTimeString().split("[\\s:]");
            int hour = Integer.parseInt(timeArray[0]);
            Log.d(TAG, "DebugSnooze: hour: " + hour);
            int minute = Integer.parseInt(timeArray[1]);
            Log.d(TAG, "DebugSnooze: minute: " + minute);
            String am_pm = timeArray[2];
            Log.d(TAG, "DebugSnooze: am_pm: " + am_pm);
            int snooze = Integer.parseInt(alarm.getAlarmSnooze());
            Log.d(TAG, "DebugSnooze: snooze: " + snooze);
            if (minute + snooze >= 60) {
                hour += 1;
                minute = minute + snooze - 60;

                // set HOUR_OF_DAY
                if (am_pm.equalsIgnoreCase("am")) {
                    newAlarmTime.set(Calendar.HOUR_OF_DAY, hour + 1);
                } else {
                    newAlarmTime.set(Calendar.HOUR_OF_DAY, hour + 13);
                }
            } else {
                minute += snooze;
            }

            newAlarmTime.set(Calendar.HOUR, hour);
            newAlarmTime.set(Calendar.MINUTE, minute);
            Log.d(TAG, "DebugSnooze: BeforeSnoozeTime: " + alarm.getAlarmTime());
            alarm.setAlarmTime(newAlarmTime);
            alarm.setAlarmVibrate(true);
            alarm.setAlarmActive(true);
            alarm.setAlarmMusic(alarm.getAlarmMusic());
            Log.d(TAG, "DebugSnooze: AfterSnoozeTime: " + alarm.getAlarmTime());

            callAlarmScheduleService();

            // might not need to call this again
            Toast.makeText(AlarmWakeActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            // update database? don't think so, since you want the original alarms to be the same without adding snooze to min
            // database.update(alarm);

        }
//        finish();
    }

    protected void callAlarmScheduleService() {
        Intent alarmServiceIntent = new Intent(this, AlarmServiceBroadcastReceiver.class);
        sendBroadcast(alarmServiceIntent, null);
    }

    private void setViewInfo() {
        mAlarmScreenLayout = (RelativeLayout) findViewById(R.id.alarmScreenLayout);
        weatherTypeView = (TextView) findViewById(R.id.weatherType);
        mDateTime = (TextView) findViewById(R.id.dateTime);
        snoozeButton = (Button) findViewById(R.id.snooze);
        turnOffAlarmButton = (Button) findViewById(R.id.turnOffAlarm);
    }
}


