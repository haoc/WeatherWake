package cs371m.weatherwake;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by KC on 3/27/2016.
 */
public class ALarmEditorActivity  extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("ttt_prefs");
        addPreferencesFromResource(R.xml.preferences);

    }
}
