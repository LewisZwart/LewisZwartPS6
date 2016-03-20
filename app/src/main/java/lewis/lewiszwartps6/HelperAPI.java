package lewis.lewiszwartps6;

import android.content.Context;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Handles the HTTP requests for obtaining current weather conditions in a given city,
 * using the API from OpenWeatherMap.org.
 */
public class HelperAPI {

    // constant url parts
    private static String URL1, URL2, URL_IMAGE1, URL_IMAGE2;

    /*
     * Given a city, returns a JSON object representing the local weather, using OpenWeatherMaps.org.
     */
    public static JSONObject getWeatherData(Context context, String city){

        // load urls from string resources
        URL1 = context.getString(R.string.url1);
        URL2 = context.getString(R.string.url2);
        URL_IMAGE1 = context.getString(R.string.url_image1);
        URL_IMAGE2 = context.getString(R.string.url_image2);

        // convert whitespaces to %20
        String URLCity = convertInputToHTTP(context, city);

        try {
            // set up a connection
            URL url = new URL(URL1 + URLCity + URL2 + context.getString(R.string.key));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // check whether connection was succesfull
            Integer response = connection.getResponseCode();
            if (200 <= response && response <= 299) {

                // open inputstream and reader
                BufferedReader reader = new BufferedReader( new InputStreamReader(
                        connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();

                // read JSON per line
                String line = "";
                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                // close reader
                reader.close();

                // convert buffer to JSON before returning
                return new JSONObject(buffer.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Returns byte array of the image corresponding to the given code, using OpenWeatherMaps.org.
     */
    public static byte[] getImage(String code) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // construct OpenWeatherMaps url using icon code
            URL url = new URL(URL_IMAGE1 + code + URL_IMAGE2);

            // open connection and inputstream
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();

            // copy image data on the web into a byte array
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ( inputStream.read(buffer) != -1)
                outputStream.write(buffer);

            return outputStream.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            }
            catch(Throwable t) {}
            try {
                connection.disconnect();
            }
            catch(Throwable t) {}
        }
        return null;
    }

    /*
     * Converts all spaces in input into %20 in order to use them in http request.
     */
    public static String convertInputToHTTP(Context context, String input) {

        int length = input.length();
        String httpString = "";

        // replace normal spaces by http request space
        for (int i = 0; i < length; i++) {
            char letter = input.charAt(i);
            if (letter == ' ') {
                httpString += context.getString(R.string.space);
            }
            else {
                httpString += letter;
            }
        }
        return httpString;
    }
}
