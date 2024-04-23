package org.nanoboot.utils.timecalc.app;

import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class Main {
    private static final boolean ONLY_ACTIVITIES_WINDOW_IS_ALLOWED = false;
    public static void main(String[] args) throws IOException {
//        for(File f:FileConstants.CLIMATE_TXT.getParentFile().listFiles()) {
//            if(f.getName().contains("weather")) {
//                System.out.println("Going to delete: " + f.getAbsolutePath());
//                f.delete();
//            }
//        }
        FlatLightLaf.setup();
        if(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED) {
            ActivitiesMain.main(args);
        } else {
            TimeCalcApp timeCalcApp = new TimeCalcApp();
            timeCalcApp.start(args);
        }
    }
}
