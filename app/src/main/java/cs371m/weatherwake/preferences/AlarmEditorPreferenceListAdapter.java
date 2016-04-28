package cs371m.weatherwake.preferences;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cs371m.weatherwake.Alarm;
import cs371m.weatherwake.R;

/**
 * Created by KC on 4/18/2016.
 */
public class AlarmEditorPreferenceListAdapter extends BaseAdapter implements Serializable {

    private static final String TAG = "AlarmEditorPreferenceListAdapter";

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private List<AlarmEditorPreference> alarmEditorPreferenceList = new ArrayList<AlarmEditorPreference>();
    private Alarm alarm;
    private Context context;
    private String[] alarmMusic;
    private String[] alarmMusicPaths;

    public AlarmEditorPreferenceListAdapter(Context context, Alarm alarm) {
        this.context = context;

//        Log.d(TAG, "Loading Ringtone");
        RingtoneManager ringtoneManager = new RingtoneManager(getContext());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);

        Cursor alarmsCursor = ringtoneManager.getCursor();

        alarmMusic = new String[alarmsCursor.getCount() + 1];
        alarmMusic[0] = "Silent";
        alarmMusicPaths = new String[alarmsCursor.getCount() + 1];
        alarmMusicPaths[0] = "";

        if (alarmsCursor.moveToFirst()) {
            do {
                alarmMusic[alarmsCursor.getPosition() + 1] = ringtoneManager.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
                alarmMusicPaths[alarmsCursor.getPosition() + 1] = ringtoneManager.getRingtoneUri(alarmsCursor.getPosition()).toString();
            } while (alarmsCursor.moveToNext());
        }

//        Log.d(TAG, "Finished loading " + alarmMusic.length);
        alarmsCursor.close();

        setAlarm(alarm);
    }

    @Override
    public Object getItem(int position) {
        return alarmEditorPreferenceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return alarmEditorPreferenceList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        AlarmEditorPreference alarmEditorPreference = (AlarmEditorPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        switch (alarmEditorPreference.getType()) {
            case BOOLEAN:
                view = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);
                CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(android.R.id.text1);
                checkedTextView.setText(alarmEditorPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmEditorPreference.getValue());
                break;
            default:
                view = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                title.setTextSize(18);
                title.setText(alarmEditorPreference.getTitle());
                TextView summary = (TextView) view.findViewById(android.R.id.text2);
                summary.setText(alarmEditorPreference.getSummary());
                break;
        }
        return view;
    }

    public Alarm getAlarm() {
        // get the value for each preference items
        for (AlarmEditorPreference alarmEditorPreference : alarmEditorPreferenceList) {
            switch(alarmEditorPreference.getKey()) {
                case ALARM_ACTIVE:
                    alarm.setAlarmActive((Boolean) alarmEditorPreference.getValue());
                    break;
                case ALARM_NAME:
                    alarm.setAlarmName((String) alarmEditorPreference.getValue());
                    break;
                case ALARM_TIME:
                    alarm.setAlarmTime((String) alarmEditorPreference.getValue());
                    break;
                case ALARM_MUSIC:
                    alarm.setAlarmMusic((String) alarmEditorPreference.getValue());
                    break;
                case ALARM_SNOOZE:
                    Log.d(TAG, "DebugSnooze: ALARM_SNOOZE");
                    alarm.setAlarmSnooze((String) alarmEditorPreference.getValue());              // Change to String? Integer?
                    break;
                case ALARM_PAIRING:
                    Log.d(TAG, "ALARM_PAIRING");
                    alarm.setAlarmPairing((Boolean) alarmEditorPreference.getValue());
                case ALARM_VIBRATE:
                    alarm.setAlarmVibrate((Boolean) alarmEditorPreference.getValue());
                    break;
                case ALARM_REPEAT:
                    alarm.setDays((Alarm.Day[]) alarmEditorPreference.getValue());
                    break;
            }
        }
        return alarm;
    }
    // public AlarmEditorPreference(Key key, Object value, Type type, String title, String summary, String[] options)
    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
        alarmEditorPreferenceList.clear();
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_NAME, alarm.getAlarmName(), AlarmEditorPreference.Type.STRING, "Alarm Name", alarm.getAlarmNameString(), null));
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_ACTIVE, alarm.getAlarmActive(), AlarmEditorPreference.Type.BOOLEAN, "Active", null, null));
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_TIME, alarm.getAlarmTime(), AlarmEditorPreference.Type.TIME, "Set Time", alarm.getAlarmTimeString(), null));
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_REPEAT, alarm.getDays(), AlarmEditorPreference.Type.LISTS, "Repeat", alarm.getRepeatDaysString(), days));
        Log.d(TAG, "DebugSnooze: setAlarm: " + alarm.getAlarmSnooze());
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_SNOOZE, alarm.getAlarmSnooze(), AlarmEditorPreference.Type.INTEGER, "Snooze Duration", alarm.getAlarmSnoozeString(), null));
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_PAIRING, alarm.getAlarmPairing(), AlarmEditorPreference.Type.BOOLEAN, "Alarm Pairing", null, null));
        alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_VIBRATE, alarm.getAlarmVibrate(), AlarmEditorPreference.Type.BOOLEAN, "Vibrate", null, null));

        Uri uri = Uri.parse(alarm.getAlarmMusic());
        Ringtone ringtone = RingtoneManager.getRingtone(getContext(), uri);

        if(ringtone instanceof Ringtone && !alarm.getAlarmMusic().equalsIgnoreCase("")) {
            alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_MUSIC, alarm.getAlarmMusic(),
                    AlarmEditorPreference.Type.LIST, "Music", ringtone.getTitle(getContext()), alarmMusic));
        } else {
            alarmEditorPreferenceList.add(new AlarmEditorPreference(AlarmEditorPreference.Key.ALARM_MUSIC, null,
                    AlarmEditorPreference.Type.LIST, "Music", getAlarmMusic()[0], alarmMusic));
        }

    }

    public Context getContext( ) {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getAlarmMusic() {
        return alarmMusic;
    }

    public String[] getAlarmMusicPaths() {
        return alarmMusicPaths;
    }

    public String[] getDays() {
        return days;
    }



}
