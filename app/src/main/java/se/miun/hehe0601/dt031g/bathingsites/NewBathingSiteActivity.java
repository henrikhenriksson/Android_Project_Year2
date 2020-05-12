package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: NewBathingSiteActivity.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds the new BathingSite Activity for the bathingsite app project
 * Used to hold the fragment that creates new bathing sites.
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-12
 */

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewBathingSiteActivity extends AppCompatActivity {
    private TextInputLayout textinputName;
    private TextInputLayout textinputDescription;
    private TextInputLayout textinputAddress;
    private TextInputLayout textinputLatitude;
    private TextInputLayout textinputLongitude;
    private TextInputLayout textinputWaterTemp;
    private TextInputLayout textinputWaterTempDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bathing_site);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        findViewsById();
    }

    private void findViewsById() {

        textinputName = findViewById(R.id.text_input_bathing_site_name);
        textinputDescription = findViewById(R.id.text_input_bathing_site_description);
        textinputAddress = findViewById(R.id.text_input_bathing_site_address);
        textinputLatitude = findViewById(R.id.text_input_bathing_site_latidude);
        textinputLongitude = findViewById(R.id.text_input_bathing_site_longitude);
       


        // textinputLatitude.setFilter(new MinMaxFilter(-90, 90));

    }

}
