package lewis.lewiszwartps6;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Extends AsyncTask, so that the result of the http request is shown in the main activity.
 */
public class GetWeatherAsyncTask extends AsyncTask<String, Integer, JSONObject> {

    private Context context;

    /*
     * Registers the context of the current activity.
     */
    public GetWeatherAsyncTask(Context contextArg) {
        super();
        context = contextArg;
    }

    /*
     * Gets the weather data through the HelperAPI class.
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        return HelperAPI.getWeatherData(context, params[0]);
    }

    /*
     * After completing the request, shows the results.
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        // alert user if nothing was found
        if (result == null) {
            Toast.makeText(context, R.string.noDataError, Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                // extract interpreted location and temperature
                String city = result.getString("name");
                String country = result.getJSONObject("sys").getString("country");
                int temperature = result.getJSONObject("main").getInt("temp");

                // extract the description and graphical representation code of the local weather
                JSONObject weather = result.getJSONArray("weather").getJSONObject(0);
                String description = weather.getString("description");
                String iconID = weather.getString("icon");
                byte[] icon = HelperAPI.getImage(iconID);

                // combine obtained weather conditions in one object
                WeatherConditions conditions = new WeatherConditions(context, temperature,
                        description, city, country, icon);

                // show local weather in main activity
                MainActivity.showWeather(conditions);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
