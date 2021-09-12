package org.infodavid.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Map;

/**
 * The Class NetUtils.
 */
public final class NetUtils {

    /** The singleton. */
    private static WeakReference<NetUtils> instance = null;

    /**
     * Instantiates a new util.
     */
    private NetUtils() {
        super();
    }

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized NetUtils getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new NetUtils());
        }

        return instance.get();
    }

    /**
     * Close quietly.
     * @param socket the socket
     */
    public void closeQuietly(final Closeable socket) {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        }
        catch (final Exception e) { // NOSONAR Quietly
            // noop
        }
    }

    /**
     * Find a free TCP port.
     * @return the integer associated to the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int findFreeTcpPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);

            return socket.getLocalPort();
        }
    }

    /**
     * Find a free TCP port.
     * @param port the initial port
     * @return the integer associated to the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int findFreeTcpPort(final int port) throws IOException {
        for (int i = port; i <= 65535; i++) {
            try (Socket socket = new Socket()) {
                socket.setSoTimeout(200);
                socket.setSoLinger(true, 0);
                socket.connect(new InetSocketAddress(i), 200);

                if (!socket.isConnected()) {
                    return i;
                }
            }
            catch (final SocketTimeoutException e) { // NOSONAR Expected exception
                return i;
            }
        }

        return -1;
    }

    /**
     * Gets the computer name.
     * @return the computer name
     */
    public String getComputerName() {
        final Map<String,String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        }

        if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        }

        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (final UnknownHostException ex) {
            return "UnknownComputer";
        }
    }

    public String getSsid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wifiManager.getConnectionInfo().getSSID();
    }

    public String getWifiAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endian if needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        String ipAddressString;

        try {
            return InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            // noop
        }

        return null;
    }

    /**
     * Ping.
     * @param host the host
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void ping(final String host) throws IOException {
        final boolean reachable = InetAddress.getByName(host).isReachable(200);

        if (!reachable) {
            throw new IOException(host + " is not reachable");
        }
    }

    /**
     * Ping.
     * @param host the host
     * @param port the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void ping(final String host, final int port) throws IOException {
        boolean reachable = false;

        try (Socket socket = new Socket()) {
            socket.setSoTimeout(200);
            socket.setSoLinger(true, 0);
            socket.connect(new InetSocketAddress(host, port), 200);

            reachable = socket.isConnected();
        }

        if (!reachable) {
            throw new IOException(host + " is not reachable on port " + port);
        }
    }
}
