package org.infodavid.mediaplayerremote;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.util.android.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.HandroidLoggerAdapter;

import com.androidnetworking.AndroidNetworking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    static {
        HandroidLoggerAdapter.DEBUG = BuildConfig.DEBUG;
        HandroidLoggerAdapter.ANDROID_API_LEVEL = Build.VERSION.SDK_INT;
        HandroidLoggerAdapter.APP_NAME = "MediaPlayerRemote";
    }

    /** The TCP client. */
    private transient RemoteControlClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(final View v) {
                String server = preferences.getString("server", null);

                LOGGER.debug("Server: {}", server);
                LOGGER.debug("Button clicked: {}", v.getId());

                if (v.getId() == R.id.settingsButton || StringUtils.isEmpty(server)) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                    return;
                }

                Integer code = Utils.getName(v.getId());

                if (code == null) {
                    LOGGER.debug("Not a known button: {}", v.getId());

                    return;
                }

                synchronized (preferences) {
                    if (client == null) {
                        client = new RemoteControlClient();
                    }

                    String host = StringUtils.substringBefore(server, ":");
                    String port = StringUtils.substringAfter(server, ":");

                    LOGGER.info("Sending event: {}", code);

                    new RemoteControlTask(client, host, Integer.parseInt(port)).execute(code);
                }
            }
        };

        final ViewGroup viewGroup = ViewUtils.getInstance().getViewGroup(this, android.R.id.content);

        for (final Button button : ViewUtils.getInstance().get(viewGroup, Button.class)) {
            button.setOnClickListener(clickListener);
        }

        for (final ImageButton button : ViewUtils.getInstance().get(viewGroup, ImageButton.class)) {
            button.setOnClickListener(clickListener);
        }
    }

    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    private void disconnect() {
        if (client != null && client.isOpened()) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsFragment.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}