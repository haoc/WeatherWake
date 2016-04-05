package cs371m.weatherwake;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by KC on 3/27/2016.
 */
public class AlarmEditorActivity extends PreferenceActivity {
    private Preference buttons;
    private Button mConfirm;
    private Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("ttt_prefs");
        addPreferencesFromResource(R.xml.preferences);

        setButtonViewInfo();

//        buttons.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                Log.d("something", "to");
//                finish();
//                return true;
//            }
//        });
//
//        mConfirm.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("somethingconfirm", "to");
//
//                finish();
//            }
//        });
//
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("somethingcancel", "to");
//
//                finish();
//            }
//        });
//
    }

    private void setButtonViewInfo() {
        buttons = (Preference) findPreference(getString(R.string.confirm_cancel));
        mConfirm = (Button) findViewById(R.id.confirm);
        mCancel = (Button) findViewById(R.id.cancel);
    }
}
