package cs371m.weatherwake;


import cs371m.weatherwake.database.database;
import cs371m.weatherwake.preferences.AlarmEditorPreferenceActivity;
import cs371m.weatherwake.service.AlarmServiceBroadcastReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherWakeMainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "WeatherWakeMainActivity";

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // 1000

    protected LocationManager mLocationManager;
    protected LocationListener mLocationListener;

    protected Button retrieveLocationButton;
    private Button addAlarmButton;

    private TextView mDateTime;

    private ProgressBar mProgressBar;
    private Boolean flag = false;

    private View.OnClickListener mAddAlarmListener;
    
    private TextView mTemp;
    private ImageView mArrowUp;
    private TextView mHighTemp;
    private ImageView mArrowDown;
    private TextView mLowTemp;
    private TextView mWeatherType;
    private ImageView mWeatherImage;
    private View mDivider;

    private TextView mAlarm;
    private TextView mAlarmName;
    private ImageView mStartAlarm;
    private ImageView mEditAlarm;

    private Button mAddAlarm;
    private Button mAddWeatherSetting;

    private ListView alarmListView ;
    private AlarmListAdapter alarmListAdapter;

    private database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_wake_main);
    
        setBasicViewInfo();
        setAlarmViewInfo();
        setButtonViewInfo();

        retrieveLocationButton = (Button) findViewById(R.id.locationButton);
        retrieveLocationButton.setOnClickListener(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
//        mStartAlarm.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });
        
//        mEditAlarm.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                prepAlarmEditorActivity();
//            }
//        });

        mAddAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prepAlarmEditorActivity();
            }
        });

        mAddWeatherSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepWeatherSettingActivity();
            }
        });
    }

    @Override
    protected void onPause() {
        database.deactivate();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAlarmList();
    }

    @Override
    public void onClick(View v) {
        mLocationListener = new MyLocationListener();
//        mLocationListener = new GetCurrentLocation();
        flag = displayGpsStatus();
        if(flag) {
            Log.d(TAG, "GPS IS ON");
//            mLocationListener = new MyLocationListener();
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MINIMUM_TIME_BETWEEN_UPDATES,
                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                         mLocationListener);
                Log.d(TAG, "requestLocationUpdates");

            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
        } else {
            Toast.makeText(WeatherWakeMainActivity.this, "Please turn on your GPS!", Toast.LENGTH_LONG).show();
        }

//        mAddAlarmListener = new

        // Check if alarm is active
        if (v.getId() == R.id.alarmActiveCheckBox) {
            CheckBox checkBox = (CheckBox) v;
            Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
            alarm.setAlarmActive(checkBox.isChecked());
            database.update(alarm);
            WeatherWakeMainActivity.this.callAlarmScheduleService();
            if (checkBox.isChecked()) {
                Toast.makeText(WeatherWakeMainActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);

        if(gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged");
            mProgressBar.setVisibility(View.INVISIBLE);

            String longitude = "Longitude: " +location.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " +location.getLatitude();
            Log.v(TAG, latitude);

    /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = cityName;
            retrieveLocationButton.setText(s);

//            mGetWeather = new GetWeather();
//            mGetweather.getCurrentCondition(new WeatherRequest("2988507"), new WeatherClient.WeatherEventListener(){
//
//            }

        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(WeatherWakeMainActivity.this, "Provider status changed", Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(WeatherWakeMainActivity.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
//            Toast.makeText(WeatherWakeMainActivity.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wearther_wake_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void setBasicViewInfo() {
       mDateTime = (TextView) findViewById(R.id.dateTime);
       mTemp = (TextView) findViewById(R.id.temp);
       mArrowUp = (ImageView) findViewById(R.id.arrow_up);
       mHighTemp = (TextView) findViewById(R.id.highTemp);
       mArrowDown = (ImageView) findViewById(R.id.arrow_down);
       mLowTemp = (TextView) findViewById(R.id. lowTemp);
       mWeatherType = (TextView) findViewById(R.id.weatherType);
       mWeatherImage = (ImageView) findViewById(R.id.weatherImg);
       mDivider = (View) findViewById(R.id.divider);
       mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
       mProgressBar.setVisibility(View.INVISIBLE);
    }
    
    private void setAlarmViewInfo() {
//        mAlarm = (TextView) findViewById(R.id.alarm);
//        mAlarmName = (TextView) findViewById(R.id.alarmName);
        mStartAlarm = (ImageView) findViewById(R.id.start);
//        mEditAlarm = (ImageView) findViewById(R.id.editAlarm);

        Log.d(TAG, "setAlarmViewInfo()");

        alarmListView = (ListView) findViewById(R.id.list);
        alarmListView.setLongClickable(true);
        alarmListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick");
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherWakeMainActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Delete this alarm?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.init(WeatherWakeMainActivity.this);
                        database.deleteAlarm(alarm);
                        WeatherWakeMainActivity.this.callAlarmScheduleService();

                        updateAlarmList();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                return true;
            }
        });

        callAlarmScheduleService();

        alarmListAdapter = new AlarmListAdapter(this);
        this.alarmListView.setAdapter(alarmListAdapter);
        alarmListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick");
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                Intent intent = new Intent(WeatherWakeMainActivity.this, AlarmEditorPreferenceActivity.class);
                intent.putExtra("alarm", alarm);
                startActivity(intent);
            }
        });
    }

    protected void callAlarmScheduleService() {
        Intent alarmServiceIntent = new Intent(this, AlarmServiceBroadcastReceiver.class);
        sendBroadcast(alarmServiceIntent, null);
    }

    private void setButtonViewInfo() {
        mAddAlarm = (Button) findViewById(R.id.addAlarm);
        mAddWeatherSetting = (Button) findViewById(R.id.addWeatherSetting);
    }

    private void prepAlarmEditorActivity(){
//        Intent intent = new Intent(this, AlarmEditorActivity.class);
        Log.d(TAG, "Add Alarm");
        Intent intent = new Intent(this, AlarmEditorPreferenceActivity.class);
        startActivity(intent);
    }

    private void prepWeatherSettingActivity(){
        Intent intent = new Intent(this, WeatherSettingsActivity.class);
        startActivity(intent);
    }

    private void updateAlarmList() {
        database.init(WeatherWakeMainActivity.this);
        final List<Alarm> alarms = database.getAllAlarms();
        Log.d(TAG, "alarms: " + alarms);
        alarmListAdapter.setAlarms(alarms);

        runOnUiThread(new Runnable() {
            public void run() {
                WeatherWakeMainActivity.this.alarmListAdapter.notifyDataSetChanged();
                if(alarms.size() > 0) {
                    Log.d(TAG, "alarm.size() > 0");
                    findViewById(R.id.noAlarms).setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "No Alarms set");
                    findViewById(R.id.noAlarms).setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
