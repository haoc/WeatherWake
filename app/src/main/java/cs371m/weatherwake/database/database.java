package cs371m.weatherwake.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import cs371m.weatherwake.Alarm;

/**
 * Created by KC on 4/16/2016.
 */
public class database extends SQLiteOpenHelper {

    private final static String TAG = "database";

    static database instance = null;
    static SQLiteDatabase database = null;

    public static final String DATABASE_NAME = "Alarms.db";
    public static final int DATABASE_VERSION = 1;

    public static final String ALARMS_TABLE = "alarms";
    public static final String ALARMS_COLUMN_ID = "_id";
    public static final String ALARMS_COLUMN_NAME = "alarm_name";
    public static final String ALARMS_COLUMN_ACTIVE = "alarm_active";
    public static final String ALARMS_COLUMN_TIME = "alarm_time";
    public static final String ALARMS_COLUMN_DAYS = "alarm_days";
    public static final String ALARMS_COLUMN_MUSIC = "alarm_music";
    public static final String ALARMS_COLUMN_SNOOZE = "alarm_snooze";
    public static final String ALARMS_COLUMN_PAIRING = "alarm_pairing";
    // Additional feature
    public static final String ALARMS_COLUMN_VIBRATE = "alarm_vibrate";

    public database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARMS_TABLE + " ( "
                        + ALARMS_COLUMN_ID + " INTEGER primary key autoincrement, "
                        + ALARMS_COLUMN_NAME + " TEXT NOT NULL, "
                        + ALARMS_COLUMN_ACTIVE + " INTEGER NOT NULL, "
                        + ALARMS_COLUMN_TIME + " TEXT NOT NULL, "
                        + ALARMS_COLUMN_DAYS + " BLOB NOT NULL, "
                        + ALARMS_COLUMN_MUSIC + " TEXT NOT NULL, "
                        + ALARMS_COLUMN_SNOOZE + " TEXT NOT NULL, "
                        + ALARMS_COLUMN_PAIRING + " INTEGER NOT NULL, "
                        + ALARMS_COLUMN_VIBRATE + " INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE);
        onCreate(db);
    }

    public static void init(Context context) {
        if (null == instance) {
            instance = new database(context);
        }
    }

    public static SQLiteDatabase getDatabase() {
        if (database == null) {
            // create and/or open a database
            database = instance.getWritableDatabase();
        }
        return database;
    }

    public static long create(Alarm alarm) {
        // insert values into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARMS_COLUMN_NAME, alarm.getAlarmName());
        Log.d(TAG, "Alarm Name: " + alarm.getAlarmName());
        contentValues.put(ALARMS_COLUMN_ACTIVE, alarm.getAlarmActive());
        contentValues.put(ALARMS_COLUMN_TIME, alarm.getAlarmTimeString());
        Log.d(TAG, "ALARMS_COLUMN_TIME" + alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(alarm.getDays());
            byte[] alarmDays = byteArrayOutputStream.toByteArray();

            contentValues.put(ALARMS_COLUMN_DAYS, alarmDays);


        } catch (Exception e) {
            e.printStackTrace();
        }

        contentValues.put(ALARMS_COLUMN_MUSIC, alarm.getAlarmMusic());
        Log.d(TAG, "DebugSnooze: ALARMS_COLUMN_SNOOZE");
        contentValues.put(ALARMS_COLUMN_SNOOZE, alarm.getAlarmSnooze());
        contentValues.put(ALARMS_COLUMN_PAIRING, alarm.getAlarmPairing());
//        Log.d(TAG, "ALARMS_COLUMN_PAIRING " + alarm.getAlarmPairing() );
        contentValues.put(ALARMS_COLUMN_VIBRATE, alarm.getAlarmVibrate());

        return getDatabase().insert(ALARMS_TABLE, null, contentValues);
    }

    public static int update(Alarm alarm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARMS_COLUMN_NAME, alarm.getAlarmName());
        contentValues.put(ALARMS_COLUMN_ACTIVE, alarm.getAlarmActive());
        contentValues.put(ALARMS_COLUMN_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(alarm.getDays());
            byte[] alarmDays = byteArrayOutputStream.toByteArray();

            contentValues.put(ALARMS_COLUMN_DAYS, alarmDays);


        } catch (Exception e) {
            e.printStackTrace();
        }

        contentValues.put(ALARMS_COLUMN_MUSIC, alarm.getAlarmMusic());
        contentValues.put(ALARMS_COLUMN_SNOOZE, alarm.getAlarmSnooze());
        contentValues.put(ALARMS_COLUMN_PAIRING, alarm.getAlarmPairing());
        contentValues.put(ALARMS_COLUMN_VIBRATE, alarm.getAlarmVibrate());

        return getDatabase().update(ALARMS_TABLE, contentValues, "_id= " + alarm.getAlarmId(), null);
    }

    public static int deleteAlarm(Alarm alarm) {
        return deleteAlarm(alarm.getAlarmId());
    }

    public static int deleteAlarm(int id) {
        return getDatabase().delete(ALARMS_TABLE, ALARMS_COLUMN_ID + "=" + id, null);
    }

    public static int deleteAll() {
        return getDatabase().delete(ALARMS_TABLE, "1", null);
    }

    public static void deactivate() {
        if (null != database && database.isOpen()) {
            database.close();
        }
        database = null;
        instance = null;
    }

    public static Alarm getAlarm(int id) {
        String[] columns = new String[]{
                ALARMS_COLUMN_ID,
                ALARMS_COLUMN_NAME,
                ALARMS_COLUMN_ACTIVE,
                ALARMS_COLUMN_TIME,
                ALARMS_COLUMN_DAYS,
                ALARMS_COLUMN_MUSIC,
                ALARMS_COLUMN_SNOOZE,
                ALARMS_COLUMN_PAIRING,
                ALARMS_COLUMN_VIBRATE
        };


        Cursor cursor = getDatabase().query(ALARMS_TABLE, columns, ALARMS_COLUMN_ID + "=" + id, null, null, null, null);
        Alarm alarm = null;
        // if query doesn't returned empty; moves cursor to first result
        if(cursor.moveToNext()) {
            alarm = new Alarm();
            alarm.setAlarmId(cursor.getInt(1));
            alarm.setAlarmName(cursor.getString(2));
            alarm.setAlarmActive(cursor.getInt(3) == 1);
            alarm.setAlarmTime(cursor.getString(4));
            byte[] repeatDaysInBytes = cursor.getBlob(5);
            // read repeatDays
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysInBytes);

            try {
                // read(deserialize) byteArrayInputStream
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                Alarm.Day[] repeatDays;
                Object object = objectInputStream.readObject();
                if (object instanceof Alarm.Day[]) {
                    repeatDays = (Alarm.Day[]) object;
                    alarm.setDays(repeatDays);
                }

            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // without snooze
//            alarm.setAlarmMusic(cursor.getString(6));
//            alarm.setAlarmPairing(cursor.getInt(7) == 1);
//            alarm.setAlarmVibrate(cursor.getInt(8) == 1);
            // with snooze and pairing
            alarm.setAlarmMusic(cursor.getString(6));
            alarm.setAlarmSnooze(cursor.getString(7));                                             // don't set it?
            alarm.setAlarmPairing(cursor.getInt(8) == 1);
            alarm.setAlarmVibrate(cursor.getInt(9) == 1);
            // original
//            alarm.setAlarmVibrate(cursor.getInt(7) == 1);
        }
        cursor.close();
        return alarm;
    }

    public static List<Alarm> getAllAlarms() {
        List<Alarm> array_list = new ArrayList<Alarm>();
        // return result from a database query

        String[] columns = new String[] {
                ALARMS_COLUMN_ID,
                ALARMS_COLUMN_NAME,
                ALARMS_COLUMN_ACTIVE,
                ALARMS_COLUMN_TIME,
                ALARMS_COLUMN_DAYS,
                ALARMS_COLUMN_MUSIC,
                ALARMS_COLUMN_SNOOZE,
                ALARMS_COLUMN_PAIRING,
                ALARMS_COLUMN_VIBRATE
        };

//        return getDatabase().query(ALARMS_TABLE, columns, null, null, null, null, null);


//        Cursor cursor = database.getCursor();
        String orderBy = ALARMS_COLUMN_TIME + " ASC";
        Cursor cursor = getDatabase().query(ALARMS_TABLE, columns, null, null, null, null, orderBy);
        // if query doesn't returned empty; moves cursor to first result
        if(cursor.moveToFirst()) {

            do {
                // init alarm
                Alarm alarm = new Alarm();
                alarm.setAlarmId(cursor.getInt(0));
                alarm.setAlarmName(cursor.getString(1));
                alarm.setAlarmActive(cursor.getInt(2) == 1);
                alarm.setAlarmTime(cursor.getString(3));
                byte[] repeatDaysInBytes = cursor.getBlob(4);
                // read repeatDays
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysInBytes);

                try {
                    // read(deserialize) byteArrayInputStream
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }

                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // without snooze
//                alarm.setAlarmMusic(cursor.getString(5));
//                alarm.setAlarmPairing(cursor.getInt(6) == 1);
//                alarm.setAlarmVibrate(cursor.getInt(7) == 1);
                // with snooze and pairing
                alarm.setAlarmMusic(cursor.getString(5));
                alarm.setAlarmSnooze(cursor.getString(6));                                           // don't set it? getInt or getString
                alarm.setAlarmPairing(cursor.getInt(7) == 1);
                alarm.setAlarmVibrate(cursor.getInt(8) == 1);
                // original?
//                alarm.setAlarmVibrate(cursor.getInt(8) == 1);

                array_list.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return array_list;
    }
}
