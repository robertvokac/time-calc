package org.nanoboot.utils.timecalc.utils.common;

import java.io.File;
import java.io.IOException;


/**
 * @author Robert Vokac
 * @since 15.02.2024
 */
public class JokesTxt {
    private JokesTxt() {
        //Not meant to be instantiated.
    }

    public static String[] getAsArray() throws IOException {
        File jokeTxtFile = new File("jokes.txt");
        if(!jokeTxtFile.exists()) {
            //nothing to do
            return new String[]{"A","B","C"};
        }
        return Utils.readTextFromFile(jokeTxtFile).split("-----SEPARATOR-----");
    }
}
