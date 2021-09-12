package org.infodavid.mediaplayerremote;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle(R.string.action_settings);
        }

        if (findViewById(R.id.settings) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getFragmentManager().beginTransaction().add(R.id.settings, new SettingsFragment()).commit();
        }
    }
}
