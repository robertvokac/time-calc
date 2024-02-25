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
public class ProgressSmileyIcon extends javax.swing.ImageIcon {

    private static final Map<ProgressSmiley, ProgressSmileyIcon> cache = new HashMap<>();
    @Getter
    private final ProgressSmiley progressSmiley;
    @Getter
    private final ImageIcon icon;

    public static ProgressSmileyIcon forSmiley(ProgressSmiley progressSmiley) {
        if (!cache.containsKey(progressSmiley)) {
            cache.put(progressSmiley, new ProgressSmileyIcon(progressSmiley));
        }
        return cache.get(progressSmiley);
    }

    private ProgressSmileyIcon(ProgressSmiley progressSmiley) {
        this.progressSmiley = progressSmiley;
        java.net.URL smileyUrl = getClass().getResource("/smileys/" + progressSmiley.name() + ".png");
        ImageIcon tmpIcon = new javax.swing.ImageIcon(smileyUrl);
        this.icon = new ImageIcon(tmpIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
    }

}
