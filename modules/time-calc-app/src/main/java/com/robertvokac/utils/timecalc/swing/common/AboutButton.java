package com.robertvokac.utils.timecalc.swing.common;

import com.robertvokac.utils.timecalc.swing.controls.TButton;
import com.robertvokac.utils.timecalc.utils.common.Utils;

import javax.swing.JOptionPane;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class AboutButton extends TButton {

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
