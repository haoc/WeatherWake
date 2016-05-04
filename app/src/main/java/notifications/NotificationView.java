package notifications;

import android.app.Activity;
import android.os.Bundle;

import cs371m.weatherwake.R;

/**
 * Created by KC on 5/4/2016.
 */
public class NotificationView extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}
