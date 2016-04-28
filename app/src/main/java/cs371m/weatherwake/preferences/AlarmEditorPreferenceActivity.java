package cs371m.weatherwake.preferences;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.database.database;
import cs371m.weatherwake.R;
import cs371m.weatherwake.service.AlarmServiceBroadcastReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by KC on 4/19/2016.
 */
public class AlarmEditorPreferenceActivity extends Activity {                                       //!!!!!!!!!!!!!!

    private final static String TAG = "AlarmEditorPreferenceActivity";
    ImageButton deleteButton;
    TextView okButton;
    TextView cancelButton;
    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private ListAdapter listAdapter;
    private ListView listView;

    private Button saveAlarm;
    private Button deleteAlarm;
    private Boolean alarmActive = true;
    private Boolean alarmPairing = true;
    private Boolean alarmCheck2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.alarm_preference);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("alarm")) {
            setAlarm((Alarm) bundle.getSerializable("alarm"));
        } else {
            setAlarm(new Alarm());
        }
        if (bundle != null && bundle.containsKey("adapter")) {
            setListAdapter((AlarmEditorPreferenceListAdapter) bundle.getSerializable("adapter"));
        } else {
            setListAdapter(new AlarmEditorPreferenceListAdapter(this, getAlarm()));
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View view, int position, long id) {
                final AlarmEditorPreferenceListAdapter alarmEditorPreferenceListAdapter = (AlarmEditorPreferenceListAdapter) getListAdapter();
                final AlarmEditorPreference alarmEditorPreference = (AlarmEditorPreference) alarmEditorPreferenceListAdapter.getItem(position);

                AlertDialog.Builder alert;
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                switch (alarmEditorPreference.getType()) {
                    case BOOLEAN:
                        CheckedTextView checkedTextView = (CheckedTextView) view;
                        boolean checked = !checkedTextView.isChecked();

                        ((CheckedTextView) view).setChecked(checked);
                        switch (alarmEditorPreference.getKey()) {
                            case ALARM_ACTIVE:
                                Log.d(TAG, "ALARM_ACTIVE");
                                alarm.setAlarmActive(checked);
                                alarmActive = checked;
                                break;
                            case ALARM_PAIRING:
                                Log.d(TAG, "ALARM_PAIRING");
                                alarm.setAlarmPairing(checked);
                                alarmPairing = checked;
                                break;
                            case ALARM_VIBRATE:
                                alarm.setAlarmVibrate(checked);
                                if (checked) {
                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                }
                                break;
                        }
                        alarmEditorPreference.setValue(checked);
                        break;
                    case INTEGER:
                        Log.d(TAG, "DebugSnooze: CASE INTEGER");
//                        TextView numberPickerView = (TextView) findViewById(R.id.numberPickerView);
                        final NumberPicker numberPicker = new NumberPicker(AlarmEditorPreferenceActivity.this);
                        int newAlarm2 = alarm.getAlarmId();

                        alert = new AlertDialog.Builder(AlarmEditorPreferenceActivity.this);
                        alert.setView(numberPicker);

                        numberPicker.setMaxValue(60);
                        numberPicker.setMinValue(1);

                        if (newAlarm2 == 0) {
                            Log.d(TAG, "DebugSnooze: newAlarm2: " + newAlarm2);
                            numberPicker.setValue(2);
                        } else {
                            Log.d(TAG, "DebugSnooze: edit newAlarm2: " + newAlarm2);
                            numberPicker.setValue(Integer.parseInt(alarm.getAlarmSnooze()));
                        }

                        numberPicker.setWrapSelectorWheel(true);
                        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            }
                        });
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "DebugSnooze: onClick:  " + numberPicker.getValue());
                                alarmEditorPreference.setValue(numberPicker.getValue());
                                alarm.setAlarmSnooze(alarmEditorPreference.getValue().toString());

                                alarmEditorPreferenceListAdapter.setAlarm(getAlarm());
                                alarmEditorPreferenceListAdapter.notifyDataSetChanged();
                            }
                        });
                        alert.show();
                        break;

                    case STRING:
                        alert = new AlertDialog.Builder(AlarmEditorPreferenceActivity.this);
                        alert.setTitle(alarmEditorPreference.getTitle());

                        // Set an EditText view to get user input
                        final EditText editText = new EditText(AlarmEditorPreferenceActivity.this);
                        editText.setText(alarmEditorPreference.getValue().toString());
                        editText.setSelection(editText.getText().length());

                        alert.setView(editText);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int whichButton) {

                                alarmEditorPreference.setValue(editText.getText().toString());
                                alarm.setAlarmName(alarmEditorPreference.getValue().toString());

                                alarmEditorPreferenceListAdapter.setAlarm(getAlarm());
                                alarmEditorPreferenceListAdapter.notifyDataSetChanged();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                        break;

                    case LIST:
                        alert = new AlertDialog.Builder(AlarmEditorPreferenceActivity.this);

                        alert.setTitle(alarmEditorPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] items = new CharSequence[alarmEditorPreference.getOptions().length];
                        for (int i = 0; i < items.length; i++)
                            items[i] = alarmEditorPreference.getOptions()[i];

                        alert.setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (alarmEditorPreference.getKey()) {
                                    case ALARM_MUSIC:
                                        alarm.setAlarmMusic(alarmEditorPreferenceListAdapter.getAlarmMusicPaths()[which]);
                                        if (alarm.getAlarmMusic() != null) {
                                            if (mediaPlayer == null) {
                                                mediaPlayer = new MediaPlayer();
                                            } else {
                                                if (mediaPlayer.isPlaying())
                                                    mediaPlayer.stop();
                                                mediaPlayer.reset();
                                            }
                                            try {
                                                // mediaPlayer.setVolume(1.0f, 1.0f);
                                                mediaPlayer.setVolume(0.2f, 0.2f);
                                                mediaPlayer.setDataSource(AlarmEditorPreferenceActivity.this, Uri.parse(alarm.getAlarmMusic()));
                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                                mediaPlayer.setLooping(false);
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();

                                                // Force the mediaPlayer to stop after 3
                                                // seconds...
                                                if (alarmToneTimer != null)
                                                    alarmToneTimer.cancel();
                                                alarmToneTimer = new CountDownTimer(3000, 3000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        try {
                                                            if (mediaPlayer.isPlaying())
                                                                mediaPlayer.stop();
                                                        } catch (Exception e) {

                                                        }
                                                    }
                                                };
                                                alarmToneTimer.start();
                                            } catch (Exception e) {
                                                try {
                                                    if (mediaPlayer.isPlaying())
                                                        mediaPlayer.stop();
                                                } catch (Exception e2) {

                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                alarmEditorPreferenceListAdapter.setAlarm(getAlarm());
                                alarmEditorPreferenceListAdapter.notifyDataSetChanged();
                            }

                        });
                        alert.show();
                        break;

                    case LISTS:
                        alert = new AlertDialog.Builder(AlarmEditorPreferenceActivity.this);
                        alert.setTitle(alarmEditorPreference.getTitle());

                        CharSequence[] multiListItems = new CharSequence[alarmEditorPreference.getOptions().length];
                        for (int i = 0; i < multiListItems.length; i++) {
                            multiListItems[i] = alarmEditorPreference.getOptions()[i];
                        }

                        boolean[] checkedItems = new boolean[multiListItems.length];
                        for (Alarm.Day day : getAlarm().getDays()) {
                            checkedItems[day.ordinal()] = true;
                        }
                        alert.setMultiChoiceItems(multiListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which, boolean isChecked) {
                                Alarm.Day thisDay = Alarm.Day.values()[which];

                                if (isChecked) {
                                    alarm.addDay(thisDay);
                                } else {
                                    // Only remove the day if there are more than 1 selected
                                    if (alarm.getDays().length > 1) {
                                        alarm.deleteDay(thisDay);
                                    } else {
                                        // If the last day was unchecked, re-check it
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                                    }
                                }
                            }
                        });

                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int which) {
                                alarmEditorPreferenceListAdapter.setAlarm(getAlarm());
                                alarmEditorPreferenceListAdapter.notifyDataSetChanged();

                            }
                        });
                        alert.show();
                        break;

                    case TIME:
                        final int hour;
                        final int minute;
                        int newAlarm = alarm.getAlarmId();

                        if (newAlarm == 0) {
                            hour = 12;
                            minute = 00;
                        } else {
                            hour = alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY);
                            minute = alarm.getAlarmTime().get(Calendar.MINUTE);
                        }

                        // set initial alarm clock time; need to make sure preference "Set time" title is the same***
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmEditorPreferenceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                Calendar newAlarmTime = Calendar.getInstance();
                                int amPM;
                                int hour;

                                if (hourOfDay > 12) {
                                    hour = hourOfDay - 12;
                                    amPM = 1;
                                } else {
                                    hour = hourOfDay;
                                    amPM = 0;
                                }

                                // HOUR_OF_DAY = 24-hour clock
                                newAlarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                newAlarmTime.set(Calendar.HOUR, hour);
                                newAlarmTime.set(Calendar.MINUTE, minute);
                                newAlarmTime.set(Calendar.SECOND, 0);
                                newAlarmTime.set(Calendar.AM_PM, amPM);
                                alarm.setAlarmTime(newAlarmTime);
                                alarmEditorPreferenceListAdapter.setAlarm(getAlarm());
                                alarmEditorPreferenceListAdapter.notifyDataSetChanged();
                            }
//                        }, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY), alarm.getAlarmTime().get(Calendar.MINUTE), true);
                        }, hour, minute, false);
                        timePickerDialog.setTitle(alarmEditorPreference.getTitle());
//                        Log.d(TAG, "DebugTime: timePickerDialog.setTitle() " + alarmEditorPreference.getTitle());
                        timePickerDialog.show();
                    default:
                        break;
                }
            }
        });

        saveAlarm = (Button) findViewById(R.id.saveAlarm);

        saveAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "DebugToast: saveAlarm pressed");
//                alarmCheck2 =
                addAlarm();
            }
        });

        deleteAlarm = (Button) findViewById(R.id.deleteAlarm);

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "deleteAlarm pressed");
                deleteAlarm();
            }
        });

    }

    private CountDownTimer alarmToneTimer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", getAlarm());
        outState.putSerializable("adapter", (AlarmEditorPreferenceListAdapter) getListAdapter());
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();
        } catch (Exception e) {
        }
        // setListAdapter(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        getListView().setAdapter(listAdapter);

    }

    public ListView getListView() {

        if (listView == null) {
            listView = (ListView) findViewById(R.id.list);
        }

        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    private void addAlarm() {
        database.init(getApplicationContext());

        Boolean alarmActive = alarm.getAlarmActive();

        if (getAlarm().getAlarmId() < 1) {
            Log.d(TAG, "Alarm " + getAlarm().getAlarmId() + " added");
            database.create(getAlarm());
        } else {
            Log.d(TAG, "Alarm " + getAlarm().getAlarmId() + " updated");
            database.update(getAlarm());
        }
        callAlarmScheduleService();

        // only display toast if alarm is active
        if (alarmActive) {
            Toast.makeText(AlarmEditorPreferenceActivity.this, getAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void deleteAlarm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmEditorPreferenceActivity.this);
        dialog.setTitle("Delete alarm?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.init(getApplicationContext());
                if (getAlarm().getAlarmId() < 1) {
                    Log.d(TAG, "Alarm not saved");
                } else {
                    database.deleteAlarm(alarm);
                    callAlarmScheduleService();
                }
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void callAlarmScheduleService() {
        Intent alarmServiceIntent = new Intent(this, AlarmServiceBroadcastReceiver.class);
        sendBroadcast(alarmServiceIntent, null);
    }
}
