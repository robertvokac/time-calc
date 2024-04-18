package org.nanoboot.utils.timecalc.app;

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
        if(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED) {
            ActivitiesMain.main(args);
        } else {
            TimeCalcApp timeCalcApp = new TimeCalcApp();
            timeCalcApp.start(args);
        }
    }
}
