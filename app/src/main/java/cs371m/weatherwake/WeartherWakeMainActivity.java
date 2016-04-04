package cs371m.weatherwake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WeartherWakeMainActivity extends AppCompatActivity {

    private TextView mLocation;
    private TextView mDateTime;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearther_wake_main);

        setBasicViewInfo();
        setAlarmViewInfo();
        setButtonViewInfo();

        mStartAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        mEditAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prepAlarmEditorActivity();
            }
        });

        mAddAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prepAlarmEditorActivity();
            }
        });

        mAddWeatherSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
       mLocation = (TextView) findViewById(R.id.location);
       mDateTime = (TextView) findViewById(R.id.dateTime);
       mTemp = (TextView) findViewById(R.id.temp);
       mArrowUp = (ImageView) findViewById(R.id.arrow_up);
       mHighTemp = (TextView) findViewById(R.id.highTemp);
       mArrowDown = (ImageView) findViewById(R.id.arrow_down);
       mLowTemp = (TextView) findViewById(R.id. lowTemp);
       mWeatherType = (TextView) findViewById(R.id.weatherType);
       mWeatherImage = (ImageView) findViewById(R.id.weatherImg);
       mDivider = (View) findViewById(R.id.divider);
    }

    private void setAlarmViewInfo() {
        mAlarm = (TextView) findViewById(R.id.alarm);
        mAlarmName = (TextView) findViewById(R.id.alarmName);
        mStartAlarm = (ImageView) findViewById(R.id.start);
        mEditAlarm = (ImageView) findViewById(R.id.editAlarm);
    }

    private void setButtonViewInfo() {
        mAddAlarm = (Button) findViewById(R.id.addAlarm);
        mAddWeatherSetting = (Button) findViewById(R.id.addWeatherSetting);
    }

    private void prepAlarmEditorActivity(){
        Intent intent = new Intent(this, ALarmEditorActivity.class);
        startActivity(intent);
    }
}
