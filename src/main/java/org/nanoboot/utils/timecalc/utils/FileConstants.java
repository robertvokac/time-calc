package org.nanoboot.utils.timecalc.utils;

import java.io.File;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class FileConstants {
    private FileConstants() {
        //Not meant to be instantiated.
    }
    public static final File STARTTIME_TXT = new File("starttime.txt");
    public static final File OVERTIME_TXT = new File("overtime.txt");
    public static final File TEST_TXT = new File("test.txt");
    public static final File FOCUS_TXT = new File("focus.txt");
}
