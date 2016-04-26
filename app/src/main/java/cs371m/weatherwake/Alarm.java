package cs371m.weatherwake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.NumberPicker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import cs371m.weatherwake.alert.AlarmAlertBroadcastReceiver;
import cs371m.weatherwake.preferences.AlarmEditorPreferenceActivity;

/**
 * Created by KC on 4/17/2016.
 */
public class Alarm implements Serializable {

    private final static String TAG = "Alarm";

    public enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;

        @Override
        public String toString() {
            switch(this.ordinal()) {
                case 0:
                    return "Monday";
                case 1:
                    return "Tuesday";
                case 2:
                    return "Wednesday";
                case 3:
                    return "Thursday";
                case 4:
                    return "Friday";
                case 5:
                    return "Saturday";
                case 6:
                    return "Sunday";
            }
            return super.toString();
        }
    }

    private static final long serialVersionUID = 1234567890;
    private int alarmId;
    private String alarmName = "Alarm Clock";
    private Boolean alarmActive = true;
    private Calendar alarmTime = Calendar.getInstance();
    private Day[] days = {Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY};
    private String alarmMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
//    private NumberPicker np = new NumberPicker(AlarmEditorPreferenceActivity.this);
//    private String alarmSnooze = "2";
    private Boolean alarmPairing = true;
    private Boolean alarmVibrate = true;

    public Alarm() {

    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int id) {
        this.alarmId = id;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public String getAlarmNameString() {
        return getAlarmName();
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public Boolean getAlarmActive() {
        return alarmActive;
    }

    public void setAlarmActive(Boolean alarmActive) {
        this.alarmActive = alarmActive;
    }

    // Better way to do this? Time might be wrong
    // Zone is America/Chicago; date is 1 day ahead, need to fix
    public Calendar getAlarmTime() {
        if (alarmTime.before(Calendar.getInstance())) {
            Log.d(TAG, "TestTime inside if");
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "TestTime alarmTime: " + alarmTime);
        return alarmTime;
    }

    // Might be buggy; time might be wrong
    public String getAlarmTimeString() {

        String time = "";
        String am_pm = "";
        if (alarmTime.get(Calendar.HOUR) > 12) {
            Log.d(TAG, "if: " + alarmTime.get(Calendar.HOUR));
            time += String.valueOf(alarmTime.get(Calendar.HOUR) - 12);
        } else {
            time += String.valueOf(alarmTime.get(Calendar.HOUR));
        }

        time += ":";
        time += String.valueOf(alarmTime.get(Calendar.MINUTE));


        if (alarmTime.get(Calendar.AM_PM) == 0) {
            Log.d(TAG, "AM");
            time += " AM";
        } else {
            Log.d(TAG, "PM");
            time += " PM";
        }

        Log.d(TAG, "getAlarmTimeString: " + time);

        Log.d(TAG, "AM_PM: " + alarmTime.get(Calendar.AM_PM));

        return time;

//        Date time = alarmTime.getTime();
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//        String localTime = dateFormat.format(time);
//
//        return localTime;
    }

    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }

    // Might be buggy; need to set SECOND?
    public void setAlarmTime(String alarmTime) {
        Log.d(TAG, "setAlarmTIme: " + alarmTime);
        String[] timeArray = alarmTime.split("[\\s:]");
        Calendar newAlarmTime = Calendar.getInstance();
        newAlarmTime.set(Calendar.HOUR, Integer.parseInt(timeArray[0]));                            // HOUR or HOUR_OF_DAY
        newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        newAlarmTime.set(Calendar.SECOND, 0);
        setAlarmTime(newAlarmTime);
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }
    // ?
    public void addDay(Day day) {
        boolean flag = false;
        for (Day d : getDays()) {
            if (d.equals(day)) {
                flag = true;
            }
        }
        if (!flag) {
            List<Day> result = new LinkedList<Day>();
            for( Day d : getDays()) {
                result.add(d);
            }
            result.add(day);
            setDays(result.toArray(new Day[result.size()]));
        }
    }
    // ?
    public void deleteDay(Day day) {
        List<Day> result = new LinkedList<Day>();
        for (Day d : getDays()) {
            if (!d.equals(day)) {
                result.add(d);
            }
        }
        setDays(result.toArray(new Day[result.size()]));
    }

    public String getAlarmMusic() {
        return alarmMusic;
    }

    public void setAlarmMusic(String alarmMusic) {
        this.alarmMusic = alarmMusic;
    }

//    public String getAlarmSnooze() {
//        return alarmSnooze;
//    }

//    public String getAlarmSnoozeString() {
//        Log.d(TAG, "getAlarmSnoozeString(): " + Integer.toString(alarmSnooze));
//        return Integer.toString(alarmSnooze);
//    }

//    public String getAlarmSnoozeString() {
//        return getAlarmSnooze();
//    }

//    public void setAlarmSnooze(String alarmSnooze) {
//        this.alarmSnooze = alarmSnooze;
//    }

    public Boolean getAlarmPairing() {
        return alarmPairing;
    }

    public void setAlarmPairing(Boolean alarmPairing) {
        this.alarmPairing = alarmPairing;
    }

    public Boolean getAlarmVibrate() {
        return alarmVibrate;
    }

    public void setAlarmVibrate(Boolean alarmVibrate) {
        this.alarmVibrate = alarmVibrate;
    }

    // ?
    public String getRepeatDaysString() {
        StringBuilder daysStringBuilder = new StringBuilder();
        if (getDays().length == Day.values().length) {
            daysStringBuilder.append("Every Day");
        } else {
            Arrays.sort(getDays(), new Comparator<Day>() {
                @Override
                public int compare(Day lhs, Day rhs) {
                    return lhs.ordinal() - rhs.ordinal();
                }
            });
            for (Day d : getDays()) {
                switch(d) {
                    case TUESDAY:
                    case THURSDAY:
                    default:
                        daysStringBuilder.append(d.toString().substring(0, 3));
                        break;
                }
                daysStringBuilder.append(',');
            }
            daysStringBuilder.setLength(daysStringBuilder.length() - 1);
        }

        return daysStringBuilder.toString();
    }
    // ?
    public void schedule(Context context) {
        setAlarmActive(true);
        Intent intent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        intent.putExtra("alarm", this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), pendingIntent);

    }
    // Might get incorrect time message; better way to do this?
    public String getTimeUntilNextAlarmMessage() {
        long timeDifference = getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
        long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
        long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
        String alert = "This alarm is set for ";
        if (days > 0) {
            alert += String.format(
                    "%d days, %d hours, %d minutes and %d seconds", days,
                    hours, minutes, seconds);
        } else {
            if (hours > 0) {
                alert += String.format("%d hours, %d minutes and %d seconds",
                        hours, minutes, seconds);
            } else {
                if (minutes > 0) {
                    alert += String.format("%d minutes, %d seconds", minutes,
                            seconds);
                } else {
                    alert += String.format("%d seconds", seconds);
                }
            }
        }
        return alert;
    }

}
