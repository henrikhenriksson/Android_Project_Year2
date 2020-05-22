package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: NewBathingSiteActivity.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds the new BathingSite Activity for the bathingsite app project
 * Used to hold the fragment that creates new bathing sites.
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-12
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.preference.PreferenceManager;

import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

// main parts of this class fetched from https://www.youtube.com/watch?v=veOZTvAdzJ8
public class NewBathingSiteActivity extends AppCompatActivity {
    private TextInputLayout textinputName;
    private TextInputLayout textinputDescription;
    private TextInputLayout textinputAddress;
    private EditText textinputLatitude;
    private EditText textinputLongitude;
    private TextInputLayout textinputWaterTemp;
    private TextInputLayout textinputWaterTempDate;
    private RatingBar ratingBar;

    private CoordinatorLayout coordinatorLayout;
    private ProgressBar loadWeatherProgressBar;
    private BathingSite bathingSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bathing_site);
        Toolbar toolbar = findViewById(R.id.new_bathing_site_tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = findViewById(R.id.new_bathingsite_coordinator);
        findViewsById();
        setTodaysDate();
    }

    @Override
    protected void onResume() {
        // https://stackoverflow.com/questions/2795833/check-orientation-on-android-phone
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setNrOfBathingSites();
        }
        new showRandomBathingSite().execute();
        super.onResume();
    }

    //  https://stackoverflow.com/questions/50399194/how-to-get-size-of-room-list-in-oncreate-on-main-thread
    private void setNrOfBathingSites() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int nrOfBathingSites;
                nrOfBathingSites = AppDataBase.getDataBase(getApplicationContext()).bathingSiteDao().getDataCount();
                BathingSitesView bathingSitesView = findViewById(R.id.bathingSitesView);
                bathingSitesView.setBathSiteCounter(nrOfBathingSites);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_bathing_site_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                confirmInput();
                break;
            case R.id.action_clear:
                clearInput();
                break;
            case R.id.action_weather:
                showWeatherInfo();
                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewsById() {
        textinputName = findViewById(R.id.text_input_bathing_site_name);
        textinputDescription = findViewById(R.id.text_input_bathing_site_description);
        textinputAddress = findViewById(R.id.text_input_bathing_site_address);
        textinputLatitude = findViewById(R.id.text_input_bathing_site_latidude);
        textinputLongitude = findViewById(R.id.text_input_bathing_site_longitude);
        textinputWaterTemp = findViewById(R.id.text_input_bathing_site_water_temp);
        textinputWaterTempDate = findViewById(R.id.text_input_bathing_site_temperature_date);

        // Inspiration by https://www.youtube.com/watch?v=LpNJhJF3gW8
        ratingBar = findViewById(R.id.bathing_site_rating_bar);

        loadWeatherProgressBar = findViewById(R.id.weather_progress_bar);

    }

    private boolean hasCoordinates() {
        return !textinputLongitude.getText().toString().isEmpty()
                && !textinputLongitude.getText().toString().isEmpty();
    }

    private boolean hasAddress() {
        return !textinputAddress.getEditText().getText().toString().trim().isEmpty();
    }

    private void setTodaysDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = simpleDateFormat.format(new Date());

        textinputWaterTempDate.getEditText().setText(todaysDate);
    }


    private boolean validateName() {
        String nameInput = textinputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textinputName.setError("This Field is Required");
            return false;
        } else {
            textinputName.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {

        if (!hasAddress()) {
            if (!validateLatLng()) {
                textinputAddress.setError("Address or coordinates are required. ");
                textinputLatitude.setError("Address or coordinates are required. ");
                textinputLongitude.setError("Address or coordinates are required. ");
                return false;
            }
        }
        textinputAddress.setError(null);
        return true;
    }

    // Lat and long are required if there is no address. If there is an address, Lat and long are optional
    private boolean validateLatLng() {

        boolean isValidLatLng = false;

        if (hasCoordinates()) {
            double inputLat = Double.parseDouble(textinputLatitude.getText().toString());
            double inputLong = Double.parseDouble(textinputLongitude.getText().toString());
            isValidLatLng = isInRange(inputLat, inputLong);
        }
        return isValidLatLng;
    }

    private boolean isInRange(double lati, double longi) {
        return lati >= -90 && lati <= 90 && longi >= -180 && longi <= 180;
    }

    // only one pipe to ensure all methods are called. A locical "OR" would not call the following
    // method call if the previous returned false
    public void confirmInput() {
        if (!validateName() | !validateAddress()) {
            return;
        }
        if (hasCoordinates()) {
            if (!validateLatLng()) {
                textinputLongitude.setError("Invalid latitude/longitude");
                textinputLatitude.setError("Invalid latitude/longitude");
                return;
            }
        }

        new saveBathingSite().execute();

    }

//    private void saveValidData() {
//        // Local variables as we only wish to save them if they are valid.
//        String inputName = textinputName.getEditText().getText().toString();
//        String inputDesc = textinputDescription.getEditText().getText().toString();
//        String inputAddress = textinputAddress.getEditText().getText().toString();
//        String inputLat = textinputLatitude.getText().toString();
//        String inputLong = textinputLongitude.getText().toString();
//        String waterTemp = textinputWaterTemp.getEditText().getText().toString();
//        String waterTempDate = textinputWaterTempDate.getEditText().getText().toString();
//        String rating = String.valueOf(ratingBar.getRating());
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("Name: " + inputName + "\n")
//                .append("Description: " + inputDesc + "\n")
//                .append("Address: " + inputAddress + "\n")
//                .append("Latitude: " + inputLat + " ")
//                .append("Longitude: " + inputLong + "\n")
//                .append("Rating: " + rating + "\n")
//                .append("Water Temp: " + waterTemp + "\n")
//                .append("Date For WaterTemp: " + waterTempDate);
//
//        // https://stackoverflow.com/questions/32228300/how-to-prevent-my-snackbar-text-from-being-truncated-on-android
//        Snackbar snackbar = Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_LONG);
//        View snackbarview = snackbar.getView();
//        TextView snackTextView = snackbarview.findViewById(com.google.android.material.R.id.snackbar_text);
//        snackTextView.setMaxLines(30);
//        snackbar.show();
//    }

    private Map<String, String> getValidData() {
        Map<String, String> inputData = new LinkedHashMap<String, String>();
        inputData.put("name", textinputName.getEditText().getText().toString().trim());
        inputData.put("description", textinputDescription.getEditText().getText().toString().trim());
        inputData.put("address", textinputAddress.getEditText().getText().toString().trim());
        inputData.put("latitude", textinputLatitude.getText().toString().trim());
        inputData.put("longitude", textinputLongitude.getText().toString().trim());
        inputData.put("waterTemp", textinputWaterTemp.getEditText().getText().toString().trim());
        inputData.put("waterTempDate", textinputWaterTempDate.getEditText().getText().toString().trim());
        inputData.put("rating", String.valueOf(ratingBar.getRating()));
        return inputData;
    }

    // https://android--code.blogspot.com/2015/08/android-edittext-clear.html
    private void clearInput() {
        textinputName.getEditText().getText().clear();
        textinputDescription.getEditText().getText().clear();
        textinputAddress.getEditText().getText().clear();
        textinputLatitude.getText().clear();
        textinputLongitude.getText().clear();
        textinputWaterTemp.getEditText().getText().clear();
        textinputWaterTempDate.getEditText().getText().clear();
        ratingBar.setRating(0);
        Toast.makeText(this, "All input has been cleared", Toast.LENGTH_SHORT).show();
    }

    private void showWeatherInfo() {

        String latitude;
        String longitude;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NewBathingSiteActivity.this);
        String weatherUrl = preferences.getString("url_weather", "null");

        String bathingSiteLocation = "";

        if (hasCoordinates()) {
            if (validateLatLng()) {
                latitude = textinputLatitude.getText().toString().trim();
                longitude = textinputLongitude.getText().toString().trim();
                bathingSiteLocation = "?lat=" + latitude + "&lon=" + longitude;
            }
        } else if (hasAddress()) {
            bathingSiteLocation = "?q=" + textinputAddress.getEditText().getText().toString().trim();
        } else {
            Toast.makeText(this, "No address or coordinates specified.", Toast.LENGTH_SHORT).show();
            return;
        }
        new getWeatherInformation().execute(weatherUrl, bathingSiteLocation);

    }

    // Source: https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    // Soucre https://square.github.io/okhttp/
    private class getWeatherInformation extends AsyncTask<String, String, Map<String, String>> {
        //TODO Add weatherURL and IconImgUrl to sharedprefs.
        private String iconImgUrl;
        private Drawable iconImg;
        private Map<String, String> fetchedWeatherData;

        @Override
        protected void onPreExecute() {

            loadWeatherProgressBar.setMax(100);
            loadWeatherProgressBar.setProgress(0);
            loadWeatherProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {

            iconImgUrl = "https://openweathermap.org/img/w/";

            OkHttpClient client = new OkHttpClient();

            HttpURLConnection connection = null;
            InputStream is = null;

            fetchedWeatherData = new LinkedHashMap<String, String>();
            URL url = null;
            try {
                url = new URL(strings[0] + strings[1]);
            } catch (MalformedURLException e) {
                return null;
            }

            Request request = new Request.Builder().url(url).build();

            try {
                // create a response that data can be fetched from to JSONobjects.
                // We want to catch any errors in the URL here:
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (UnknownHostException e) {
                    return null;
                }


                // get the body of the response
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject mainObj = jsonObject.getJSONObject("main");
                // The tag weather is an array with 1 entry at index 0
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObj = (JSONObject) weatherArray.get(0);

                // Objects to fetch from the Json Object
                String description = weatherArray.getJSONObject(0).getString("description");
                String icon = weatherArray.getJSONObject(0).getString("icon") + ".png";
                String temperature = mainObj.getString("temp");
                String location = jsonObject.getString("name");
                fetchedWeatherData.put("description", description);
                fetchedWeatherData.put("temperature", temperature);
                fetchedWeatherData.put("location", location);

                URL iconUrl = new URL(iconImgUrl + icon);
                connection = (HttpURLConnection) iconUrl.openConnection();
                connection.connect();
                is = connection.getInputStream();
                iconImg = Drawable.createFromStream(is, null);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return fetchedWeatherData;
        }


        @Override
        protected void onPostExecute(Map<String, String> data) {

            loadWeatherProgressBar.setVisibility(View.INVISIBLE);

            if (data == null) {
                Toast.makeText(NewBathingSiteActivity.this, "No Weather data was found", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(data.get("location") + "\n")
                    .append(data.get("description")).append("\n")
                    .append(data.get("temperature") + " degrees");

            AlertDialog.Builder adb = new AlertDialog.Builder(NewBathingSiteActivity.this);
            adb.setTitle("Current Weather");
            adb.setMessage(sb.toString());
            adb.setIcon(iconImg);

            adb.setCancelable(true);
            adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog weatherDialog = adb.create();

            weatherDialog.show();

            super.onPostExecute(data);
        }
    }

    private class saveBathingSite extends AsyncTask<String, String, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            bathingSite = null;
            super.onPreExecute();
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {

            Map<String, String> inputData = getValidData();

            Double longitude;
            Double latitude;
            Double grade;
            Double waterTemp;

            if (hasCoordinates()) {
                longitude = Double.parseDouble(Objects.requireNonNull(inputData.get("longitude")));
                latitude = Double.parseDouble(Objects.requireNonNull(inputData.get("latitude")));
            } else {
                longitude = null;
                latitude = null;
            }
            if (!inputData.get("rating").equals("")) {
                grade = Double.parseDouble(Objects.requireNonNull(inputData.get("rating")));
            } else {
                grade = null;
            }

            if (!Objects.equals(inputData.get("waterTemp"), "")) {
                waterTemp = Double.parseDouble((inputData.get("waterTemp")));
            } else {
                waterTemp = null;
            }

            bathingSite = new BathingSite(inputData.get("name"),
                    inputData.get("description"),
                    inputData.get("address"), longitude, latitude, grade, waterTemp, inputData.get("waterTempDate"));
            try {
                AppDataBase.getDataBase(NewBathingSiteActivity.this).bathingSiteDao().addBathingSite(bathingSite);
            } catch (SQLiteConstraintException e) {
                return null;
            }

            return inputData;
        }

        @Override
        protected void onPostExecute(Map<String, String> inputData) {

            // Not the best catch method, but works for now.
            if (inputData == null) {
                Toast.makeText(NewBathingSiteActivity.this, "A duplicate Bathing Site with the same coordinates was found.", Toast.LENGTH_SHORT).show();
                return;
            }

            String successMessage = "New BathingSite location " + bathingSite.getBathingSiteName() + " was added to the data base.";
            AppDataBase.destroyInstance();
            clearInput();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(NewBathingSiteActivity.this, successMessage, Toast.LENGTH_LONG).show();
            startActivity(i);
            super.onPostExecute(inputData);
        }
    }

    private class showRandomBathingSite extends AsyncTask<String, String, BathingSite> {
        @Override
        protected BathingSite doInBackground(String... strings) {

            int nrOfBathingSites = AppDataBase.getDataBase(getApplicationContext()).bathingSiteDao().getDataCount();
            BathingSite randomBathingSite = null;

            if (nrOfBathingSites != 0) {
                List<BathingSite> bathingSites = AppDataBase.getDataBase(getApplicationContext()).bathingSiteDao().getAll();
                int random = ThreadLocalRandom.current().nextInt(0, nrOfBathingSites);
                randomBathingSite = bathingSites.get(random);
            }

            return randomBathingSite;
        }

        @Override
        protected void onPostExecute(BathingSite randomBathingSite) {

            if (randomBathingSite == null) {
                return;
            }

            AlertDialog.Builder adb = new AlertDialog.Builder(NewBathingSiteActivity.this);

            adb.setTitle("Random Bathing Site");
            adb.setMessage(randomBathingSite.toString());
            adb.setCancelable(true);

            adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog aboutDialog = adb.create();
            aboutDialog.show();

            super.onPostExecute(bathingSite);
        }
    }
}
