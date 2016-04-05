//package cs371m.weatherwake;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
////public class GetCurrentLocation extends Activity implements LocationListener{
//public class GetCurrentLocation extends Activity{
//
//    private LocationManager locationMangaer=null;
//    private LocationListener locationListener=null;
//
//    private Button btnGetLocation = null;
//    private EditText editLocation = null;
//    private ProgressBar pb =null;
//
//    private static final String TAG = "Debug";
//    private Boolean flag = false;
//
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.main);
////
////
////        //if you want to lock screen for always Portrait mode
////        setRequestedOrientation(ActivityInfo
////                .SCREEN_ORIENTATION_PORTRAIT);
////
////        pb = (ProgressBar) findViewById(R.id.progressBar1);
////        pb.setVisibility(View.INVISIBLE);
////
////        editLocation = (EditText) findViewById(R.id.editTextLocation);
////
////        btnGetLocation = (Button) findViewById(R.id.btnLocation);
////        btnGetLocation.setOnClickListener(this);
////
////        locationMangaer = (LocationManager)
////                getSystemService(Context.LOCATION_SERVICE);
////
////    }
//
//
//
////    @Override
////    public void onClick(View v) {
////        flag = displayGpsStatus();
////        if(flag) {
////            Log.d(TAG, "GPS IS ON");
////            mLocationListener = new MyLocationListener();
////            mProgressBar.setVisibility(View.VISIBLE);
////            try {
////                mLocationManager.requestLocationUpdates(
////                        LocationManager.GPS_PROVIDER,
////                        MINIMUM_TIME_BETWEEN_UPDATES,
////                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
////                        mLocationListener);
////                Log.d(TAG, "requestLocationUpdates");
////
////            } catch (SecurityException e) {
////                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
////            }
////        } else {
////            Toast.makeText(WeatherWakeMainActivity.this, "Please turn on your GPS!", Toast.LENGTH_LONG).show();
////        }
////    }
//
////    public Boolean displayGpsStatus() {
//////        ContentResolver contentResolver = getBaseContext().getContentResolver();
//////        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
////
////        boolean gpsStatus = false;
////        LocationManager locationManager;
////        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////
////        if(gpsStatus) {
////            return true;
////        } else {
////            return false;
////        }
////    }
//
//    public class MyLocationListener implements LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.d(TAG, "onLocationChanged");
////            mProgressBar.setVisibility(View.INVISIBLE);
//
//            String longitude = "Longitude: " +location.getLongitude();
//            Log.v(TAG, longitude);
//            String latitude = "Latitude: " +location.getLatitude();
//            Log.v(TAG, latitude);
//
//    /*----------to get City-Name from coordinates ------------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getBaseContext(),
//                    Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(location.getLatitude(), location
//                        .getLongitude(), 1);
//                if (addresses.size() > 0)
//                    System.out.println(addresses.get(0).getLocality());
//                cityName=addresses.get(0).getLocality();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            String s = cityName;
////            retrieveLocationButton.setText(s);
//        }
//
//        public void onStatusChanged(String s, int i, Bundle b) {
////            Toast.makeText(WeatherWakeMainActivity.this, "Provider status changed", Toast.LENGTH_LONG).show();
//        }
//
//        public void onProviderDisabled(String s) {
////            Toast.makeText(WeatherWakeMainActivity.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();
//        }
//
//        public void onProviderEnabled(String s) {
////            Toast.makeText(WeatherWakeMainActivity.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();
//        }
//
//    }
//}