package cs371m.weatherwake;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KC on 4/17/2016.
 */

public class AlarmListAdapter extends BaseAdapter{

    private final static String TAG = "AlarmListAAdapter";
    private WeatherWakeMainActivity weatherWakeMainActivity;
    private List<Alarm> alarms = new ArrayList<Alarm>();

    public AlarmListAdapter(WeatherWakeMainActivity weatherWakeMainActivity) {
        this.weatherWakeMainActivity = weatherWakeMainActivity;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    // view of list of alarms on the home screen
    @Override
    public View getView(int position, View view, ViewGroup parent) {
//        Log.d(TAG, "getting view...");
        // if view is null(first time the view has been displayed),
        // obtain the LayoutInflater from weatherWakeMainActivity and inflate/render a new view hierarchy from alarm_list.xml;
        if (view == null) {
            view = LayoutInflater.from(weatherWakeMainActivity).inflate(R.layout.alarm_list, null);
        }
        // if view isn't null, reuse it from the recycler
        Alarm alarm = (Alarm) getItem(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.alarmActiveCheckBox);
        checkBox.setChecked(alarm.getAlarmActive());
        checkBox.setTag(position);
        checkBox.setOnClickListener(weatherWakeMainActivity);

        TextView alarmNameTextView = (TextView) view.findViewById(R.id.alarmNameTextView);
        alarmNameTextView.setText(alarm.getAlarmNameString());

        TextView alarmTimeView = (TextView) view.findViewById(R.id.alarmTimeTextView);
        alarmTimeView.setText(alarm.getAlarmTimeString());

        TextView alarmDaysView = (TextView) view.findViewById(R.id.alarmDaysTextView);
        alarmDaysView.setText(alarm.getRepeatDaysString());

        return view;

    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

}
