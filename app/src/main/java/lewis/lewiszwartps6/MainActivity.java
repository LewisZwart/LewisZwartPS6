package lewis.lewiszwartps6;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Lewis Zwart
 * 10251057
 *
 * Shows the current weather (temperature and weather conditions) of any city the user types,
 * using the OpenWeatherMap API.
 *
 * Sometimes the image does not load the first time one prompts a city. I have not found out why;
 * in these cases the byte array is not empty, so that is not the cause.
 *
 * Created with help of the documentation provided by OpenWeatherMap.org.
 */

public class MainActivity extends AppCompatActivity {

    // global variables
    static EditText cityEditText;
    static Button getWeatherButton;
    static TextView placeTextView;
    static ImageView iconImageView;
    static TextView descriptionTextView;
    static TextView temperatureTextView;

    /*
     * Shows the user the weather in the previously typed city and allows user to type a new city.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign variables to the layout elements
        linkLayoutToVariables();

        // if GO is clicked, look up weather in given city
        setGOListener();

        // load current weather info of previously viewed city
        SharedPreferences sharedPrefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String previousPlace = sharedPrefs.getString("place", "");
        if (!previousPlace.equals("")) {
            GetWeatherAsyncTask task = new GetWeatherAsyncTask(this);
            task.execute(previousPlace);
        }
    }

    /*
     * Assigns variables to the layout elements.
     */
    public void linkLayoutToVariables() {
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        getWeatherButton = (Button) findViewById(R.id.getWeatherButton);
        placeTextView = (TextView) findViewById(R.id.placeTextView);
        iconImageView = (ImageView) findViewById(R.id.iconImageView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        temperatureTextView = (TextView) findViewById(R.id.tempTextView);
    }

    /*
     * If user has typed nonempty city and clicks GO, shows the current weather in that city.
     */
    public void setGOListener() {
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityInput = cityEditText.getText().toString();

                if (!cityInput.equals("")) {
                    GetWeatherAsyncTask task = new GetWeatherAsyncTask(getApplicationContext());
                    task.execute(cityInput);
                    cityEditText.setText("");
                }
            }
        });
    }

    /*
     * Displays the given weather conditions in its respective textviews and imageviews
     */
    public static void showWeather(WeatherConditions weatherConditions) {

        // extract weather properties
        String place = weatherConditions.getPlace();
        byte[] icon = weatherConditions.getIcon();
        String description = weatherConditions.getDescription();
        int temperature = weatherConditions.getTemperature();

        // display place, temperature and description
        placeTextView.setText(place);
        temperatureTextView.setText(temperature + "°C");
        descriptionTextView.setText(description);

        // translate byte array to bitmap
        Bitmap iconBMP = BitmapFactory.decodeByteArray(icon, 0, icon.length);

        // show bitmap in imageview
        iconImageView.setImageBitmap(iconBMP);
    }
}
