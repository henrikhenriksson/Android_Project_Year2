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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadActivity extends AppCompatActivity {
    Context mContext;
    private WebView webView;

    private RelativeLayout progressLayout;
    private ProgressBar progressBar;
    private TextView downloadInfo;

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
        downloadInfo = findViewById(R.id.downloadInformation);
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

            BufferedInputStream is = null;
            BufferedOutputStream os = null;

            File cacheFile = new File(mContext.getCacheDir().getAbsolutePath(), "bathingSites");

            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.connect();

                is = new BufferedInputStream(url.openStream());
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

                saveBathingSites(cacheFile);

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

            return cacheFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        // source: https://stackoverflow.com/questions/34417581/how-to-replace-the-characher-n-as-a-new-line-in-android
        // https://stackoverflow.com/questions/20164875/android-using-indexof
        private void saveBathingSites(File cacheFile) {
            BufferedReader bufferedReader = null;
            String line = "";

            try {
                bufferedReader = new BufferedReader(new FileReader(cacheFile));

                while ((line = bufferedReader.readLine()) != null) {
                    line = line.replaceFirst("\"", "");
                    String coordinates = line.substring(0, line.indexOf("\""));
                    String bathingSiteInfo = line.substring(line.indexOf("\"") + 1);
                    bathingSiteInfo = bathingSiteInfo.replaceAll("\"", "");

                    // get the Coordinates:
                    String longitude = coordinates.substring(0, coordinates.indexOf(","));
                    String latitude = coordinates.substring(coordinates.indexOf(",") + 1);
                    latitude = latitude.replaceAll(",", "");

                    Double longi = Double.parseDouble(longitude);
                    Double lati = Double.parseDouble(latitude);

                    String bathingSiteName = null;
                    String bathingSiteAddress = null;

                    if (bathingSiteInfo.contains(",")) {
                        bathingSiteName = bathingSiteInfo.substring(0, bathingSiteInfo.indexOf(","));
                        bathingSiteAddress = bathingSiteInfo.substring(bathingSiteInfo.indexOf(",") + 1);
                    } else {
                        bathingSiteName = bathingSiteInfo;
                    }

                    // Save the bathingsite to Database
                    BathingSite bathingSite = new BathingSite(bathingSiteName,
                            "",
                            bathingSiteAddress,
                            lati,
                            longi,
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

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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