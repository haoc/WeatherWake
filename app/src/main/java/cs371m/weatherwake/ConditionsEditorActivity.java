package cs371m.weatherwake;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by KC on 3/27/2016.
 */
public class ConditionsEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_wake_condition_editor);

        // Setup the new range seek bar
        RangeSeekBar<Integer> tempRSB = new RangeSeekBar<Integer>(this);

        // Set the range
        tempRSB.setRangeValues(-30, 120);
        tempRSB.setSelectedMinValue(85);
        tempRSB.setSelectedMaxValue(120);

        // Add to layout
        LinearLayout temp_layout = (LinearLayout) findViewById(R.id.tempRSB);
        temp_layout.addView(tempRSB);

        // Setup the new range seek bar
        RangeSeekBar<Integer> precipitationRSB = new RangeSeekBar<Integer>(this);
        // Set the range
        precipitationRSB.setRangeValues(0, 100);
        precipitationRSB.setSelectedMinValue(0);
        precipitationRSB.setSelectedMaxValue(1);

        // Add to layout
        LinearLayout precipitation_layout = (LinearLayout) findViewById(R.id.precipitationRSB);
        precipitation_layout.addView(precipitationRSB);

        // Setup the new range seek bar
        RangeSeekBar<Integer> windRSB = new RangeSeekBar<Integer>(this);
        // Set the range
        windRSB.setRangeValues(0, 50);
        windRSB.setSelectedMinValue(0);
        windRSB.setSelectedMaxValue(5);

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.windRSB);
        layout.addView(windRSB);

    }

}
