package se.miun.hehe0601.dt031g.bathingsites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/*
 * FileName: BathingSitesView.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds a BathingSiteView for the bathingsite app project
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-11
 */


/**
 * TODO: document your custom view class.
 */
public class BathingSitesView extends ConstraintLayout {

    private TextView NoOfBathingSites;
    private int bathSiteCounter = 0;

    public BathingSitesView(Context context) {
        super(context);
        init(null);
    }

    public BathingSitesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BathingSitesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

        inflate(getContext(), R.layout.bathing_sites_view, this);
        NoOfBathingSites = findViewById(R.id.bathingSiteText);

        // Get the ammount of BathingSites
        // value for debugging:
        setBathingSites();
        setIconOnClickListener();


    }

    private void setIconOnClickListener() {

        ImageView imageView = findViewById(R.id.SwimImageView);
        imageView.setOnClickListener(new OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "This Is the Icon Click", Toast.LENGTH_SHORT).show();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        BathingSiteDownloader bsd = new BathingSiteDownloader(getContext());
//                        BathingSite bathingSite = bsd.returnLastBathingSite();
//                        if(bathingSite != null) {
//                            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
//                            adb.setTitle("Last Saved BathingSite");
//
//                        }
//                    }
//                }).start();

                new AsyncTask<Void, Void, BathingSite>() {

                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());


                    @Override
                    protected void onPreExecute() {

                        adb.setTitle("Last Saved BathingSite");


                        super.onPreExecute();
                    }

                    @Override
                    protected BathingSite doInBackground(Void... voids) {
                        BathingSite bathingSite;
                        BathingSiteDownloader bsd = new BathingSiteDownloader(getContext());
                        bathingSite = bsd.returnLastBathingSite();

                        return bathingSite;
                    }

                    @Override
                    protected void onPostExecute(BathingSite bathingSite) {
                        super.onPostExecute(bathingSite);
                        if (bathingSite == null) {
                            Toast.makeText(getContext(), "Error loading bathing sites", Toast.LENGTH_SHORT).show();
                        }
                        adb.setMessage(bathingSite.toString());
                        adb.setCancelable(true);
                        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        adb.create().show();
                    }
                }.execute();


            }
        });


    }


    public void setBathSiteCounter(int bathSiteCounter) {
        this.bathSiteCounter = bathSiteCounter;
        setBathingSites();
    }

    private void setBathingSites() {
        NoOfBathingSites.setText(bathSiteCounter + " Bathing Sites");
    }


}
