package org.infodavid.mediaplayerremote;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RemoteControlClient implements Closeable {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteControlClient.class);

    private static final int BUFFER_SIZE = 128;

    private Charset charset = StandardCharsets.US_ASCII;

    private Socket socket;

    public boolean isOpened() {
        if (socket == null) {
            return false;
        }

        try {
            socket.getOutputStream();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private String host;

    private int port;

    private int timeout = 30000;

    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public synchronized void connect(String host, int port, int timeout) throws IOException {
        if (isOpened()) {
            return;
        }

        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("Host cannot be null");
        }

        if (port <= 0) {
            throw new IllegalArgumentException("Port cannot be negative");
        }

        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }

        LOGGER.debug("Creating socket...");

        socket = new Socket();

        LOGGER.debug("Connecting to host: {} using port: {}...", host, port);

        socket.connect(new InetSocketAddress(host, port), timeout);
        socket.setSoTimeout(5000);
        socket.setReuseAddress(true);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);

        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public void setCharset(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("Charset cannot be null");
        }

        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public byte[] send(Integer... params) throws IOException {
        if (socket.isClosed()) {
            connect(this.host, this.port, this.timeout);
        }

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        for (Integer integer : params) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sending: 0x{}...", String.format("%02X", integer.byteValue()));
            }

            out.write(new byte[]{integer.byteValue(), 0x0D, 0x0A});
            out.flush();
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] data = new byte[0];

        try {
            for (int read; (read = in.read(buffer)) != -1; ) {
                data = ArrayUtils.addAll(data, ArrayUtils.subarray(buffer, 0, read));

                LOGGER.debug("Bytes read: {}", Arrays.toString(data));

                if (data.length > 2 && data[data.length - 2] == 0x0D && data[data.length - 1] == 0x0A) {
                    data = ArrayUtils.subarray(data, 0, data.length - 2);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Data received: {}", Arrays.toString(data));
                    }

                    return data;
                }
            }
        } catch (SocketTimeoutException e) {
            LOGGER.trace("Ignored error when reading response", e);
        }

        return data;
    }
}
