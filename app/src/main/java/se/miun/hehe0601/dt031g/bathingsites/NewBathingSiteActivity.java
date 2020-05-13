package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: NewBathingSiteActivity.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds the new BathingSite Activity for the bathingsite app project
 * Used to hold the fragment that creates new bathing sites.
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-12
 */

import android.location.Address;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bathing_site);
        Toolbar toolbar = findViewById(R.id.new_bathing_site_tool_bar);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.new_bathingsite_coordinator);
        findViewsById();
        setTodaysDate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_bathing_site_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                confirmInput();
                break;
            case R.id.action_clear:
                clearInput();
                break;
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

//        textinputLatitude.setFilters(new MinMaxFilter[]{new MinMaxFilter((-90), 90)});
//        textinputLongitude.setFilters(new MinMaxFilter[]{new MinMaxFilter((-90), 180)});
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

        String addressInput = textinputAddress.getEditText().getText().toString().trim();
        if (addressInput.isEmpty()) {
            textinputAddress.setError("This Field is Required. ");
            return false;
        } else {
            textinputAddress.setError(null);
            return true;
        }


    }

    private boolean validateLatLng() {
        String inputLat = textinputLatitude.getText().toString();
        String inputLong = textinputLongitude.getText().toString();

        if (inputLat.isEmpty()) {
            textinputLatitude.setError("This Field is Required. ");
            return false;
        }

        if (inputLong.isEmpty()) {
            textinputLongitude.setError("This Field is Required. ");
            return false;
        }
        return true;
    }

    // only one pipe to ensure all methods are called. A locical "OR" would not call the following
    // method call if the previous returned false
    public void confirmInput() {
        if (!validateName() | !validateAddress() | !validateLatLng()) {
            return;
        }
        saveValidData();
    }

    private void saveValidData() {
        // Local variables as we only wish to save them if they are valid.
        String inputName = textinputName.getEditText().getText().toString();
        String inputDesc = textinputDescription.getEditText().getText().toString();
        String inputAddress = textinputAddress.getEditText().getText().toString();
        Double inputLat = Double.parseDouble(textinputLatitude.getText().toString());
        Double inputLong = Double.parseDouble(textinputLongitude.getText().toString());
        String waterTemp = textinputWaterTemp.getEditText().getText().toString();
        String waterTempDate = textinputWaterTempDate.getEditText().getText().toString();
        String rating = String.valueOf(ratingBar.getRating());

        StringBuilder sb = new StringBuilder();

        sb.append("Name: " + inputName + "\n")
                .append("Description: " + inputDesc + "\n")
                .append("Address: " + inputAddress + "\n")
                .append("Latitude: " + inputLat + " ")
                .append("Longitude: " + inputLong + "\n")
                .append("Rating: " + rating + "\n")
                .append("Water Temp: " + waterTemp + "\n")
                .append("Date For WaterTemp: " + waterTempDate);

        // https://stackoverflow.com/questions/32228300/how-to-prevent-my-snackbar-text-from-being-truncated-on-android
        Snackbar snackbar = Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_INDEFINITE);
        View snackbarview = snackbar.getView();
        TextView snackTextView = snackbarview.findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setMaxLines(30);
        snackbar.show();
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
        Toast.makeText(this, "All input has been cleared", Toast.LENGTH_SHORT).show();
    }

}
