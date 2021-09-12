package org.infodavid.mediaplayerremote;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<Integer,Integer> RC_CODES = new HashMap<>();

    static {
        RC_CODES.put(R.id.k0Button, 0x00);
        RC_CODES.put(R.id.k1Button, 0x01);
        RC_CODES.put(R.id.k2Button, 0x02);
        RC_CODES.put(R.id.k3Button, 0x03);
        RC_CODES.put(R.id.k4Button, 0x04);
        RC_CODES.put(R.id.k5Button, 0x05);
        RC_CODES.put(R.id.k6Button, 0x06);
        RC_CODES.put(R.id.k7Button, 0x07);
        RC_CODES.put(R.id.k8Button, 0x08);
        RC_CODES.put(R.id.k9Button, 0x09);
        RC_CODES.put(R.id.leftButton, 0x0A);
        RC_CODES.put(R.id.rightButton, 0x0B);
        RC_CODES.put(R.id.upButton, 0x0C);
        RC_CODES.put(R.id.downButton, 0x0D);
        RC_CODES.put(R.id.backButton, 0x0E);
        RC_CODES.put(R.id.playListButton, 0x0F);
        RC_CODES.put(R.id.okButton, 0x10);
        RC_CODES.put(R.id.detailsButton, 0x11);
        RC_CODES.put(R.id.previousButton, 0x12);
        RC_CODES.put(R.id.nextButton, 0x13);
        RC_CODES.put(R.id.playButton, 0x14);
        RC_CODES.put(R.id.muteButton, 0x16);
        RC_CODES.put(R.id.volumeDownButton, 0x17);
        RC_CODES.put(R.id.volumeUpButton, 0x18);
        RC_CODES.put(R.id.volumeButton, 0x19);
        RC_CODES.put(R.id.channelUpButton, 0x1A);
        RC_CODES.put(R.id.channelDownButton, 0x1B);
        RC_CODES.put(R.id.channelButton, 0x1C);
        RC_CODES.put(R.id.sourceButton, 0x1D);
        RC_CODES.put(R.id.poweroffButton, 0x1E);
        RC_CODES.put(R.id.searchButton, 0xA0);
    }

    /**
     * Instantiates a new util.
     */
    private Utils() {
        super();
    }

    public static Integer getName(int id) {
        return RC_CODES.get(id);
    }
}
