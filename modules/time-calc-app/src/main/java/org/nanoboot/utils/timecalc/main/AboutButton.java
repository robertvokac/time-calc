package org.nanoboot.utils.timecalc.main;

import org.nanoboot.utils.timecalc.gui.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.utils.Utils;

import javax.swing.JOptionPane;

/**
 * @author pc00289
 * @since 21.02.2024
 */
public class AboutButton extends TimeCalcButton {
    public AboutButton() {
        super("About");
        addActionListener(e -> {
            String version = Utils.getVersion();
            String buildDate = Utils.getBuildDate();
            if (version == null) {
                version = "unknown";
            }

            if (buildDate == null) {
                buildDate = "unknown";
            }
            JOptionPane.showMessageDialog(null,
                    "Version: " + version + "\n" + "Built on (universal time): "
                    + buildDate, "About \"Pdf DME Downloader\"",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
