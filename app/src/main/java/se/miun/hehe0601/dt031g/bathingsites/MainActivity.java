package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: MainActivity.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds the Main Activity for the bathingsite app project
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-11
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_new_bathing_site);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewBathingSiteActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        setNrOfBathingSites();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                showAbout();
                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

        adb.setTitle(getString(R.string.about_str));
        adb.setMessage(getString(R.string.about_information));
        adb.setCancelable(true);

        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog aboutDialog = adb.create();
        aboutDialog.show();
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
}
