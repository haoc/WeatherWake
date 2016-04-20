package cs371m.weatherwake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KC on 3/27/2016.
 */
public class WeatherSettingsActivity extends Activity {

    private final static String TAG = "WeatherSettingsActivity";
    private TextView mWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.weather_wake_conditions);

        setTextViewInfo();

        mWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepConditionsEditorActivity();
            }
        });


    }

    private void setTextViewInfo() {
        mWeather = (TextView) findViewById(R.id.pairing1);
    }

    private void prepConditionsEditorActivity(){
        Intent intent = new Intent(this, ConditionsEditorActivity.class);
        startActivity(intent);
    }
}
