package se.miun.hehe0601.dt031g.bathingsites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class DownloadActivity extends AppCompatActivity {
    Context mContext;
    private WebView webView;

    private RelativeLayout progressLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.mContext = this;
        progressLayout = findViewById(R.id.progressBarHolder);
        progressBar = findViewById(R.id.progressBar);
        createWebView();
        startDownloadListener();
    }

    private void createWebView() {
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new myWebViewClient());

        Intent i = getIntent();
        String url = i.getStringExtra("DOWNLOAD_URL");
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void startDownloadListener() {
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                new myAsyncDownload().execute(url);
            }
        });
    }

    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else
            super.onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    private class myAsyncDownload extends AsyncTask<String, Integer, File> {
        private int downloadCounter = 0;
        private int duplicateCounter = 0;

        @Override
        protected void onPreExecute() {
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressLayout.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        // Courtesy of: https://www.youtube.com/watch?v=kz4LAqT9keg
        protected File doInBackground(String... strings) {
            String urlStr = strings[0];

            BufferedInputStream is;
            BufferedOutputStream os;
            File cacheFile;

            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.connect();

                is = new BufferedInputStream(url.openStream());

                cacheFile = new File(mContext.getCacheDir().getAbsolutePath(), "bathingSites.tmp");
                os = new BufferedOutputStream(new FileOutputStream(cacheFile));

                byte[] buf = new byte[1024];
                int fileLength = conn.getContentLength();
                int downloadProgress = 0;
                int len;

                while ((len = is.read(buf)) != -1) {
                    downloadProgress += len;
                    publishProgress((int) (downloadProgress * 100 / fileLength));
                    os.write(buf, 0, len);
                    SystemClock.sleep(10);
                }

                os.flush();
                os.close();
                is.close();

            } catch (MalformedURLException e) {
                Log.e("Download", "Malformed URL Exception: " + urlStr + "\n" + e.toString());
                return null;
            } catch (IOException e) {
                Log.e("Download", "Error opening/closing connection " + urlStr + "\n" + e.toString());
                return null;
            }

            try {
                saveBathingSites();
            } catch (FileNotFoundException e) {
                return null;
            }

            return cacheFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        // source: https://stackoverflow.com/questions/34417581/how-to-replace-the-characher-n-as-a-new-line-in-android
        // https://developer.android.com/training/data-storage/app-specific
        // https://stackoverflow.com/questions/20164875/android-using-indexof
        private void saveBathingSites() throws FileNotFoundException {

            // find the cache file in the cachedir.
            FileInputStream fileInputStream = new FileInputStream(new File(mContext.getCacheDir().getAbsolutePath(), "bathingSites.tmp"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            // an arraylist will hold all the values we extract from the file.
            ArrayList<String> lines = new ArrayList<>();

            // the while loop will continue adding lines to the arraylist as long as it is not null.
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e("IOException", Objects.requireNonNull(e.getMessage()));
            }

            // for each line in the lines arraylist
           // source https://stackoverflow.com/questions/18462826/split-string-only-on-first-instance-java
            for (String line :
                    lines) {
                // Replace all " signs, split the array by "," values.
                line = line.replaceAll("\"", "");
                String[] bathingSiteValues = line.split(",", 4);
                String longitude = bathingSiteValues[0];
                String latitude = bathingSiteValues[1];

                String bathingSiteName = bathingSiteValues[2];
                String bathingSiteAddress = null;

                if(bathingSiteValues.length > 3) {
                    bathingSiteAddress = bathingSiteValues[3];
                }

                // Save the bathingsite to Database
                BathingSite bathingSite = new BathingSite(bathingSiteName,
                        "",
                        bathingSiteAddress,
                        latitude,
                        longitude,
                        null,
                        null,
                        "");

                try {
                    AppDataBase.getDataBase(mContext).bathingSiteDao().addBathingSite(bathingSite);
                    downloadCounter++;
                } catch (SQLiteConstraintException e) {
                    duplicateCounter++;
                }
            }
        }

        @Override
        protected void onPostExecute(File cacheFile) {
            super.onPostExecute(cacheFile);

            if (cacheFile == null) {
                Toast.makeText(mContext, "Something went wrong while Downloading files...", Toast.LENGTH_SHORT).show();
                return;
            }
            String message = downloadCounter + " Sites downloaded\n" + duplicateCounter + " Duplicate sites not added";
            AppDataBase.destroyInstance();
            Intent i = new Intent(mContext, MainActivity.class);
            startActivity(i);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


            progressLayout.setVisibility(View.INVISIBLE);
        }
    }

}