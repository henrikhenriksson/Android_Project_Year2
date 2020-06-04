package se.miun.hehe0601.dt031g.bathingsites;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.net.MalformedURLException;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Source: https://stackoverflow.com/questions/20592659/edittextpreference-settextvalue-not-updating-as-expected
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            setWeatherURL();
            setDownloadURL();
        }

        private void setDownloadURL() {

            final EditTextPreference downloadURL = findPreference("url_download");
            downloadURL.setSummary(downloadURL.getText());

            downloadURL.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        URL newUrl = new URL(newValue.toString());
                    } catch (MalformedURLException e) {
                        Toast.makeText(getContext(), "Invalid URL, no change saved", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    downloadURL.setSummary(newValue.toString());
                    return true;
                }
            });
        }

        private void setWeatherURL() {
            final EditTextPreference weatherURL = findPreference("url_weather");
            assert weatherURL != null;
            weatherURL.setSummary(weatherURL.getText());
            weatherURL.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Attempt to create a new URL object of the input value. If it fails, the URL was malformed.
                    try {
                        URL newUrl = new URL(newValue.toString());
                    } catch (MalformedURLException e) {
                        Toast.makeText(getContext(), "Invalid URL, no changes saved.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    weatherURL.setSummary(newValue.toString());
                    return true;
                }
            });
        }
    }
}