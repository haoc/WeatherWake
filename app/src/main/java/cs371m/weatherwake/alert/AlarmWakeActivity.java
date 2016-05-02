package cs371m.weatherwake.alert;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.R;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmWakeActivity extends Activity implements View.OnClickListener{

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private Vibrator vibrator;

    private boolean alarmActive;

    private RelativeLayout mAlarmScreenLayout;

    private TextView weatherTypeView;
    private TextView mDateTime;

    private Button snoozeButton;
    private Button turnOffAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.weather_wake_alarm_screen);

        setViewInfo();

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        this.setTitle(alarm.getAlarmName());

        weatherTypeView.setText("Overcast".toString());
        snoozeButton.setOnClickListener(this);
        turnOffAlarmButton.setOnClickListener(this);

//        ((Button) findViewById(R.id.snooze)).setOnClickListener(this);
//        ((Button) findViewById(R.id.turnOffAlarm)).setOnClickListener(this);

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

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: "
                                + incomingNumber);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {}
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {}
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        // Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();

        startAlarm();

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

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
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmMusic()));
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

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {}
        try {
            mediaPlayer.stop();
        } catch (Exception e) {}
        try {
            mediaPlayer.release();
        } catch (Exception e) {}
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (!alarmActive)
            return;
        String button = (String) v.getTag();
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (button.equalsIgnoreCase("ok")) {
            alarmActive = false;
            vibrator.cancel();
        } else if (button.equalsIgnoreCase("snooze")) {
            vibrator.cancel();
            mediaPlayer.stop();
        } else {

        }
    }

    private void setViewInfo() {
        mAlarmScreenLayout = (RelativeLayout) findViewById(R.id.alarmScreenLayout);
        weatherTypeView = (TextView) findViewById(R.id.weatherType);
        mDateTime = (TextView) findViewById(R.id.dateTime);
        snoozeButton = (Button) findViewById(R.id.snooze);
        turnOffAlarmButton = (Button) findViewById(R.id.turnOffAlarm);
    }
}


