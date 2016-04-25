package cs371m.weatherwake.alert;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by KC on 4/19/2016.
 */
public class StaticWakeLock {

    private static PowerManager.WakeLock wakeLock = null;

    public static void lockOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //Object flags;
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ALARM");
        }
        wakeLock.acquire();
    }

    public static void lockOff(Context context) {
        try {
            if (wakeLock != null) {
                wakeLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
