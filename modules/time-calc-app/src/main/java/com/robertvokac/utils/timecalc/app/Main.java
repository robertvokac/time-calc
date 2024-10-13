package com.robertvokac.utils.timecalc.app;

import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Supplier;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class Main {
    public static final boolean ONLY_ACTIVITIES_WINDOW_IS_ALLOWED = ((Supplier<Boolean>) () -> {

                try {
                    return !Main.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
                            .getPath().contains("time-calc");
                } catch (URISyntaxException e) {
                    return true;
                }
            }).get();

    public static final boolean ACTIVITIES_WINDOW_SHOW_SORTKEY = false;
    public static void main(String[] args) throws IOException {
//        for(File f:FileConstants.CLIMATE_TXT.getParentFile().listFiles()) {
//            if(f.getName().contains("weather")) {
//                System.out.println("Going to delete: " + f.getAbsolutePath());
//                f.delete();
//            }
//        }
        FlatLightLaf.setup();
        TimeCalcApp timeCalcApp = new TimeCalcApp();
        timeCalcApp.start(args);
    }
}
