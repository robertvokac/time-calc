package org.nanoboot.utils.timecalc.utils.common;

import java.io.File;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class FileConstants {
    public static final File STARTTIME_TXT = new File("starttime.txt");
    public static final File OVERTIME_TXT = new File("overtime.txt");
    public static final File TEST_TXT = new File("test.txt");
    private FileConstants() {
        //Not meant to be instantiated.
    }
}
