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
//    private NumberPicker np = new NumberPicker();
    private String alarmSnooze = "2";
    private Boolean alarmPairing = true;
    private Boolean alarmVibrate = true;

    public Alarm() {

    }

    // ALARM_ID
    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int id) {
        this.alarmId = id;
    }

    // ALARM_NAME
    public String getAlarmName() {
        return alarmName;
    }

    public String getAlarmNameString() {
        return getAlarmName();
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    // ALARM_ACTIVE
    public Boolean getAlarmActive() {
        return alarmActive;
    }

    public void setAlarmActive(Boolean alarmActive) {
        this.alarmActive = alarmActive;
    }

    // ALARM_TIME
    // Better way to do this? Time might be wrong
    // Zone is America/Chicago; date is 1 day ahead, need to fix
    public Calendar getAlarmTime() {
        if (alarmTime.before(Calendar.getInstance())) {
//            Log.d(TAG, "DebugTime: getAlarmTime(): inside if: " + alarmTime);
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        while(!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK) - 1])) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        return alarmTime;
    }

    public String getAlarmTimeString() {

        String time = "";
        String am_pm = "";

        if (alarmTime.get(Calendar.HOUR_OF_DAY) > 12) {
            time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY) - 12);
            am_pm = " PM";
        } else {
            time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY));
            am_pm = " AM";
        }

        time += ":";
        if (alarmTime.get(Calendar.MINUTE) <= 9) {
            time += "0";
        }
        time += String.valueOf(alarmTime.get(Calendar.MINUTE));
        time += am_pm;

        return time;
    }

    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        String[] timeArray = alarmTime.split("[\\s:]");
        int am_pm;
        Log.d(TAG, "DebugTime: alarmTime: " + alarmTime);
        Calendar newAlarmTime = Calendar.getInstance();
        newAlarmTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        newAlarmTime.set(Calendar.HOUR, Integer.parseInt(timeArray[0]));
        newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));

        Log.d(TAG, "DebugTime: timeArray[2]: " + timeArray[2]);
        if (timeArray[2].equalsIgnoreCase("am")) {
            Log.d(TAG, "DebugTime: 2 == AM");
            am_pm = 0;
        } else {
            Log.d(TAG, "DebugTime: 2 == PM");
            am_pm = 1;
        }
        Log.d(TAG, "DebugTime: am_pm: " + am_pm);
        newAlarmTime.set(Calendar.AM_PM, am_pm);
        newAlarmTime.set(Calendar.SECOND, 0);
        setAlarmTime(newAlarmTime);
    }

    // ALARM_DAY
    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }

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

    public void deleteDay(Day day) {
        List<Day> result = new LinkedList<Day>();
        for (Day d : getDays()) {
            if (!d.equals(day)) {
                result.add(d);
            }
        }
        setDays(result.toArray(new Day[result.size()]));
    }

    // ALARM_MUSIC
    public String getAlarmMusic() {
        return alarmMusic;
    }

    public void setAlarmMusic(String alarmMusic) {
        this.alarmMusic = alarmMusic;
    }

    // ALARM_SNOOZE
    public String getAlarmSnooze() {
        Log.d(TAG, "DebugSnooze: getAlarmSnooze: " + alarmSnooze);
        return alarmSnooze;
    }

    public String getAlarmSnoozeString() {
        Log.d(TAG, "DebugSnooze: getAlarmSnoozeString: " + getAlarmSnooze());
        return getAlarmSnooze();
    }

    public void setAlarmSnooze(String alarmSnooze) {
        Log.d(TAG, "DebugSnooze: setAlarmSnooze: " + alarmSnooze);
        this.alarmSnooze = alarmSnooze;
    }

    // ALARM_PAIRING
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
            daysStringBuilder.append("Everyday");
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
                daysStringBuilder.append(' ');
            }
            daysStringBuilder.setLength(daysStringBuilder.length() - 1);
        }

        return daysStringBuilder.toString();
    }

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
