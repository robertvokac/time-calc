package org.nanoboot.utils.timecalc.app;

import java.io.File;
import java.io.IOException;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {
//        for(File f:FileConstants.CLIMATE_TXT.getParentFile().listFiles()) {
//            if(f.getName().contains("weather")) {
//                System.out.println("Going to delete: " + f.getAbsolutePath());
//                f.delete();
//            }
//        }
        TimeCalcApp timeCalcApp = new TimeCalcApp();
        timeCalcApp.start(args);
    }
}
