package cs371m.weatherwake;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.*;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by Toth on 5/3/2016.
 */

public class WeatherApiActivity {

    private String cityName;
    private String location_url_pre_city = "api.openweathermap.org/data/2.5/weather?q=";
    private String APIkey = "d70d09d0946a42aa3488afe1f295e061";
    WeatherWakeMainActivity WWMA = new WeatherWakeMainActivity();

    // values to be set and used on front page and alarm screen
    public int temperature;
    public int dayHigh;
    public int dayLow;
    public String dayConditions;
    public String conditionDescription;

    // Values corresponding to xml values
    private TextView temperature_text;

    protected void onCreate(Bundle savedInstanceState) {

        WeatherBundle();

    }

    public void WeatherBundle(){
        cityName = WWMA.city;
        JSONObject weather_json = getWeatherJson();
        setDailyWeather(weather_json);
    }

    public void setDailyWeather(JSONObject weather_json){
        try {
            JSONArray w_array = weather_json.getJSONArray("weather");
            JSONObject w_obj = w_array.getJSONObject(0);
            dayConditions = w_obj.getString("main");
            conditionDescription = w_obj.getString("description");
            JSONObject main_obj = weather_json.getJSONObject("main");
            String temp_str = main_obj.getString("temp");
            String temp_min_str = main_obj.getString("temp_min");
            String temp_max_str = main_obj.getString("temp_max");
            temperature = (int) (Double.parseDouble(temp_str) * (9/5) - 459.67);
            dayLow = (int) (Double.parseDouble(temp_min_str) * (9/5) - 459.67);
            dayHigh = (int) (Double.parseDouble(temp_max_str) * (9/5) - 459.67);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject getWeatherJson(){

        HttpURLConnection urlConnection = null;
        URL api_url = null;
        StringBuilder json_string = new StringBuilder();
        JSONObject wJson = null;
        InputStream inStream = null;

        if (cityName == null){
            cityName = "Austin"; // locateCity();
        }
        try {
            api_url = new URL(location_url_pre_city + cityName + "&mode=json&appid=" + APIkey);
            urlConnection = (HttpURLConnection) api_url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                json_string.append(temp + "\n");
            }
            wJson = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return wJson;

    }

//    public String locateCity(){
//        /*----------to get City-Name from coordinates ------------- */
//        String city = null;
//        Geocoder gcd = new Geocoder(getBaseContext(),
//                Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(), location
//                    .getLongitude(), 1);
//            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
//            city =addresses.get(0).getLocality();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return city;
//    }


}
