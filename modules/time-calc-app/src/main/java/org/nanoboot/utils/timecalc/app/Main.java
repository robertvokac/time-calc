package org.nanoboot.utils.timecalc.app;

import java.io.IOException;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {
        TimeCalcApp timeCalcApp = new TimeCalcApp();
        timeCalcApp.start(args);
    }
}

