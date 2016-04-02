//package cs371m.weatherwake;
//
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.widget.Toast;
//import android.content.Context;
//
///**
// * Created by KC on 4/2/2016.
// */
//public class MyLocationListener implements LocationListener {
//
//    public void onLocationChanged(Location location) {
//        String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
//    }
//
//    public void onStatusChanged(String s, int i, Bundle b) {
//        Toast.makeText(getApplicationContext(), "Provider status changed", Toast.LENGTH_LONG).show();
//    }
//
//    public void onProviderDisabled(String s) {
//        Toast.makeText(WeatherWakeMainActivity.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();
//    }
//
//    public void onProviderEnabled(String s) {
//        Toast.makeText(WeatherWakeMainActivity.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();
//    }
//
//}
