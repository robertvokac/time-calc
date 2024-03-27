package org.nanoboot.utils.timecalc.swing.progress.weather;

import java.util.Arrays;

/**
 * @author pc00289
 * @since 25.03.2024
 */
public class UtilityWhichKeysAreUsed {
    public static void main(String[] args) {
        String javaCode = "";
        Arrays.stream(javaCode.split("\n")).filter(l->l.contains("VK_"))
                .map(l->l.replace("case KeyEvent.VK_",""))
                .map(l->l.replace(": {","")).sorted()
                .forEach(System.out::println);
    }
}
