//package cs371m.weatherwake;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.survivingwithandroid.weather.lib.WeatherClient;
//import com.survivingwithandroid.weather.lib.WeatherConfig;
//import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
//import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
//import com.survivingwithandroid.weather.lib.model.City;
//import com.survivingwithandroid.weather.lib.model.CurrentWeather;
//import com.survivingwithandroid.weather.lib.provider.forecastio.ForecastIOProviderType;
//import com.survivingwithandroid.weather.lib.request.WeatherRequest;
//
//import java.util.List;
//
///**
// * Created by KC on 4/2/2016.
// */
//public class GetWeather extends Activity {
//    private static final String TAG = "GetWeather";
//
//    private WeatherClient mWeatherClient;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Sample WeatherLib client init
//        try {
//
//            Log.d(TAG, "try");
//
//            WeatherConfig config = new WeatherConfig();
//            config.ApiKey = "f1b985918d777f3bd35ba73668be50c9";
//
//            mWeatherClient = (new WeatherClient.ClientBuilder()).attach(this)
//                    .httpClient(WeatherDefaultClient.class)
//                    .provider(new ForecastIOProviderType())
//                    .config(config)
//                    .build();
//
//            String austin = "Austin";
////            String cityID = search(austin);
//
//            mWeatherClient.searchCity(austin, new WeatherClient.CityEventListener() {
//                @Override
//                public void onCityListRetrieved(List<City> cities) {
//                    CityAdapter ca = new CityAdapter(GetWeather.this, cities);                          // MainActivity.this
//                }
//
//                @Override
//                public void onWeatherError(WeatherLibException e) {
//
//                }
//
//                @Override
//                public void onConnectionError(Throwable throwable) {
//
//                }
//            });
//
//
//            mWeatherClient.getCurrentCondition(new WeatherRequest("2988507"), new WeatherClient.WeatherEventListener() {
//                @Override
//                public void onWeatherRetrieved(CurrentWeather currentWeather) {
//                    float currentTemp = currentWeather.weather.temperature.getTemp();
//                    Log.d("WL", "City ["+currentWeather.weather.location.getCity()+"] Current temp ["+currentTemp+"]");
//                }
//
//                @Override
//                public void onWeatherError(WeatherLibException e) {
//                    Log.d("WL", "Weather Error - parsing data");
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onConnectionError(Throwable throwable) {
//                    Log.d("WL", "Connection error");
//                    throwable.printStackTrace();
//                }
//            });
//
//
//        }
//        catch (Throwable t) {
//            t.printStackTrace();
//        }
//
//    }
//
//    // This is the City Adapter used to fill the listview when user searches for the city
//    class CityAdapter extends ArrayAdapter<City> {
//
//        private List<City> cityList;
//        private Context ctx;
//
//        public CityAdapter(Context ctx, List<City> cityList) {
//            super(ctx, R.layout.city_row);
//            this.cityList = cityList;
//            this.ctx = ctx;
//        }
//
//        @Override
//        public City getItem(int position) {
//            if (cityList != null)
//                return cityList.get(position);
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            if (cityList == null)
//                return 0;
//
//            return cityList.size();
//        }
//
//        @Override
//        public long getItemId(int position) {
//            if (cityList == null)
//                return -1;
//
//            return cityList.get(position).hashCode();
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            View v = convertView;
//            if (v == null) {
//                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = inflater.inflate(R.layout.city_row, null, false);
//            }
//
//            TextView tv = (TextView) v.findViewById(R.id.descrCity);
//
//            tv.setText(cityList.get(position).getName() + "," + cityList.get(position).getCountry());
//
//            return v;
//        }
//    }
//
////    public GetWeather(Context context) {
////        super();
////        initialize();
////    }
////
////    public void initialize() {
////        WeatherClient mWeatherClient;
////    }
//
//
//
//
//}
