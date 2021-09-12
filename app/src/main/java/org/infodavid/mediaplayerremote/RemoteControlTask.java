package org.infodavid.mediaplayerremote;

import android.os.AsyncTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class RemoteControlTask extends AsyncTask<Integer, Void, byte[]> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteControlTask.class);

    private RemoteControlClient client;

    private String host;

    private int port;

    protected RemoteControlTask(RemoteControlClient client, String host, int port) {
        super();

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        this.client = client;
        this.host = host;
        this.port = port;
    }

    protected byte[] doInBackground(Integer... params) {
        LOGGER.debug("doInBackground using parameters: {}", params);
        try {
            if (!client.isOpened()) {
                LOGGER.info("Connecting...");

                client.connect(host, port, 2000);
                client.setCharset(StandardCharsets.US_ASCII);
            }

            client.send(params);
        } catch (IOException e) {
            LOGGER.error("Cannot send event to server: " + host + " (" + port + ')', e);
        }

        return null;
    }
}
