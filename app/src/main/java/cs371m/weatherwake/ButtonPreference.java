package cs371m.weatherwake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Michael on 4/25/2016.
 */
public class ButtonPreference extends Preference {
    private Button mConfirm;
    private Button mCancel;

    public ButtonPreference(Context context, AttributeSet attrs){
        super(context, attrs);
        setWidgetLayoutResource(R.layout.confirm_cancel_preference);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mConfirm = (Button) view.findViewById(R.id.confirm);
        mCancel = (Button) view.findViewById(R.id.cancel);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("somethingconfirm", "confirm");
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("somethingcancel", "cancel");
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        });
    }
}
