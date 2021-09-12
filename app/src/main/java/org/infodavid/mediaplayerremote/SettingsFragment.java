 package org.infodavid.mediaplayerremote;

 import org.apache.commons.lang3.ArrayUtils;
 import org.apache.commons.lang3.StringUtils;
 import org.infodavid.util.NetUtils;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 import android.content.Context;
 import android.net.nsd.NsdManager;
 import android.net.nsd.NsdServiceInfo;
 import android.os.Bundle;
 import android.preference.ListPreference;
 import android.preference.PreferenceFragment;
 import android.util.Log;

 import androidx.annotation.Nullable;

 public class SettingsFragment extends PreferenceFragment {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(org.infodavid.mediaplayerremote.SettingsFragment.class);

    private static final String DNS_TYPE = "_mediaplayer._tcp";

    private NsdManager.DiscoveryListener discoveryListener;

    private NsdManager.ResolveListener resolveListener;

    private NsdManager mNsdManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ListPreference serverPreference = (ListPreference)findPreference("server");

        serverPreference.setEnabled(false);

        LOGGER.info("Initializing discovery...");

        try {
            resolveListener = new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    LOGGER.error("Resolve failed: {}", errorCode);
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    LOGGER.debug("Resolved: {}", serviceInfo);

                    synchronized (serverPreference) {
                        CharSequence[] entries = serverPreference.getEntries();
                        CharSequence[] values = serverPreference.getEntryValues();

                        if (entries == null) {
                            entries = new CharSequence[0];
                        }

                        if (values == null) {
                            values = new CharSequence[0];
                        }

                        String resolvedValue = serviceInfo.getHost().getHostAddress() + ':' + serviceInfo.getPort();

                        for (CharSequence value : values) {
                            if (value.equals(resolvedValue)) {
                                return;
                            }
                        }

                        entries = ArrayUtils.add(entries, serviceInfo.getHost().getHostName());
                        values = ArrayUtils.add(values, resolvedValue);

                        serverPreference.setEntries(entries);
                        serverPreference.setEntryValues(values);
                        getActivity().runOnUiThread(() -> serverPreference.setEnabled(true));
                    }
                }
            };
            discoveryListener = new NsdManager.DiscoveryListener() {

                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                    LOGGER.error("Discovery start failed: {}", errorCode);
                }

                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                    LOGGER.error("Discovery stop failed: {}", errorCode);
                }

                @Override
                public void onDiscoveryStarted(String serviceType) {
                    LOGGER.debug("Discovery started for service type: {}", serviceType);
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                    LOGGER.debug("Discovery stopped for service type: {}", serviceType);
                }

                @Override
                public void onServiceFound(NsdServiceInfo serviceInfo) {
                    LOGGER.debug("Service found: " + serviceInfo.getServiceName());

                    if (StringUtils.startsWith(serviceInfo.getServiceType(), DNS_TYPE)) {
                        LOGGER.debug("Resolving service: " + serviceInfo.getServiceName());

                        mNsdManager.resolveService(serviceInfo, resolveListener);
                    }
                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {
                    LOGGER.debug("Discovery service lost: {}", serviceInfo);
                }
            };

            String ssid = NetUtils.getInstance().getSsid(getContext());
            String address = NetUtils.getInstance().getWifiAddress(getContext());

            LOGGER.info("SSID: {}", ssid);
            LOGGER.info("IP address: {}", address);

            findPreference("ssid").setTitle(getResources().getString(R.string.title_ssid) + ": " + ssid);
            findPreference("ip_address").setTitle(getResources().getString(R.string.title_ip_address) + ": " + address);

            mNsdManager = (NsdManager) getContext().getSystemService(Context.NSD_SERVICE);
            mNsdManager.discoverServices(DNS_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        } catch (Exception e) {
            LOGGER.error("Cannot start discovery", e);

            serverPreference.setEnabled(false);
        }

        serverPreference.setOnPreferenceClickListener(p -> {
                LOGGER.debug("Server preference click");

                return false;
        });
    }

    @Override
    public void onStop() {
       if (mNsdManager != null) {
           try {
               mNsdManager.stopServiceDiscovery(discoveryListener);
           } catch (IllegalArgumentException e) {
               // noop
           }
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mNsdManager != null) {
            try {
                mNsdManager.stopServiceDiscovery(discoveryListener);
            } catch (IllegalArgumentException e) {
                // noop
            }
        }

        super.onDestroy();
    }
}