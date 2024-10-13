package com.robertvokac.utils.timecalc.utils.common;

import com.robertvokac.utils.timecalc.app.TimeCalcProperties;
import com.robertvokac.utils.timecalc.app.TimeCalcProperty;
import com.robertvokac.utils.timecalc.swing.common.Toaster;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Robert Vokac
 * @since 09.02.2024
 */
public class Jokes {

    /**
     * https://bestvtip.cz/ https://www.vtipbaze.cz/
     * https://www.ivtipy.cz/vtipy-kamenaky-3/
     * https://www.bomba-vtipy.cz/vtipy-kamenaky/ https://www.vtipy.net/kamenaky
     * https://www.humor.cz/nejlepsi-vtipy/kamenaky/
     * https://www.panvtip.cz/kamenaky
     * https://pompo.cz/osmismerky-8-vtipy-kamenaky_z123513/
     * https://www.extra.cz/15-nejlepsich-vtipu-z-kamenaku-ktery-mate-nejradeji-vy
     */
    private static String[] array;

    static {
        try {
            array = JokesTxt.getAsArray();
            if (array.length > 0) {
                Set<String> set = new HashSet<>();
                for (String vtip : array) {
                    if (vtip.trim().isEmpty()) {
                        //nothing to do
                        continue;
                    }
                    set.add(vtip.trim());
                }
                array = new String[set.size()];
                array = set.toArray(array);
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(array).forEach(l -> {
            sb.append(l + "\n -----SEPARATOR-----\n");
        });
        JFrame window = new JFrame();
        window.setSize(1400, 1000);
        window.setVisible(true);
        JTextPane text = new JTextPane();
        text.setBounds(10, 10, 1300, 950);
        window.add(text);
        text.setText(sb.toString());
    }

    public static void showRandom() {
        if (array.length == 0 || !TimeCalcProperties.getInstance().getBooleanProperty(
                TimeCalcProperty.JOKES_VISIBLE)) {
            //nothing to do
            return;
        }
        Toaster t = new Toaster();
        t.setToasterWidth(800);
        t.setToasterHeight(800);
        t.setDisplayTime(60000 * 1);
        t.setToasterColor(Color.GRAY);
        Font font = new Font("sans", Font.PLAIN, 16);
        t.setToasterMessageFont(font);
        t.showToaster(array[((int) (Math.random() * ((double) array.length)))]);
    }
}
