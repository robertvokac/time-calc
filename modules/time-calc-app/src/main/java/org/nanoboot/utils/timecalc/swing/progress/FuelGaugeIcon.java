package org.nanoboot.utils.timecalc.swing.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.utils.common.ProgressSmiley;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Robert Vokac
 * @since 27.02.2024
 */
public class FuelGaugeIcon extends ImageIcon {

    private static final Map<Boolean, FuelGaugeIcon> cache
            = new HashMap<>();
    @Getter
    private boolean reserve;
    @Getter
    private final ImageIcon icon;

    private FuelGaugeIcon(boolean reserve) {
        this.reserve = reserve;
        java.net.URL iconUrl = getClass()
                .getResource("/fuel_gauge/fuel_gauge_icon_" + (reserve
                              ? "orange" : "white") + ".gif");
        ImageIcon tmpIcon = new ImageIcon(iconUrl);
        this.icon = new ImageIcon(tmpIcon.getImage()
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }

    public static FuelGaugeIcon getInstance(boolean reserve) {
        if (!cache.containsKey(reserve)) {
            cache.put(reserve, new FuelGaugeIcon(reserve));
        }
        return cache.get(reserve);
    }

}
