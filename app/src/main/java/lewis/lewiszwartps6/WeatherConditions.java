package lewis.lewiszwartps6;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by Lewis on 17-3-2016.
 */
public class WeatherConditions implements Serializable{

    // weather properties
    private Context context;
    private int temperature;
    private String description;
    private String cityName;
    private String countryName;
    private byte[] icon;

    /*
     * Construct a new object, and save the location to use it the next time application is opened.
     */
    public WeatherConditions(Context contextArg, int tempArg, String descriptionArg, String cityArg,
                             String countryArg, byte[] iconArg) {
        // set the properties of the local weather
        context = contextArg;
        temperature = tempArg;
        description = descriptionArg;
        cityName = cityArg;
        countryName = countryArg;
        icon = iconArg;

        // save the location in SharedPreferences
        SharedPreferences sharedPref = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("place", cityName);
        editor.commit();
    }

    /*
     * Returns the current temperature.
     */
    public int getTemperature() {
        return temperature;
    }

    /*
    * Returns a description in words of the current weather conditions.
    */
    public String getDescription() {
        return description;
    }

    /*
     * Returns the place and country of the current request.
     */
    public String getPlace() {
        return cityName + ", " + countryName;
    }

    /*
     * Returns the code of the icon representing the local weather.
     */
    public byte[] getIcon() {
        return icon;
    }

}
