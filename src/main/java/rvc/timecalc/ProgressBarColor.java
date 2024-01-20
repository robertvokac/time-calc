package rvc.timecalc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgressBarColor {

    public static final Color PROGRESS_RED_COLOR = new Color(255, 93, 86);
    public static final Color PROGRESS_ORANGE_COLOR = new Color(255, 180, 56);
    public static final Color PROGRESS_GREEN_COLOR = new Color(24, 255, 21);

    public static final int RED_LIMIT = 24;
    public static final int ORANGE_LIMIT = 44;
    private static final Map<Integer, Color> colorMap = createColorMap();

    private ProgressBarColor() {
        //Not meant to be instantiated
    }

    public static Color forProgress(int progress) {
        return colorMap.get(progress);
    }

    private static Map<Integer, Color> createColorMap() {
        java.util.List<Color> redRedTransition = createTransition(PROGRESS_RED_COLOR, PROGRESS_RED_COLOR, RED_LIMIT - 1);
        java.util.List<Color> redOrangeTransition = createTransition(PROGRESS_RED_COLOR, PROGRESS_ORANGE_COLOR, ORANGE_LIMIT - RED_LIMIT - 1);
        java.util.List<Color> orangeGreenTransition = createTransition(PROGRESS_ORANGE_COLOR, PROGRESS_GREEN_COLOR, 100 - ORANGE_LIMIT - 1);
        Map<Integer, Color> map = new HashMap<>();
        for (int i = 0; i <= RED_LIMIT; i++) {
            if (i == 0) {
                map.put(0, PROGRESS_RED_COLOR);
                continue;
            }
            if (i == RED_LIMIT) {
                map.put(RED_LIMIT, PROGRESS_RED_COLOR);
                continue;
            }
            map.put(i, redRedTransition.get(i - 1));
        }
        for (int i = (RED_LIMIT + 1); i <= ORANGE_LIMIT; i++) {
            if (i == ORANGE_LIMIT) {
                map.put(ORANGE_LIMIT, PROGRESS_ORANGE_COLOR);
                continue;
            }
            map.put(i, redOrangeTransition.get(i - (RED_LIMIT + 1)));
        }
        for (int i = (ORANGE_LIMIT + 1); i <= 100; i++) {
            if (i == 100) {
                map.put(100, PROGRESS_GREEN_COLOR);
                continue;
            }
            map.put(i, orangeGreenTransition.get(i - (ORANGE_LIMIT + 1)));
        }
        return map;
    }

    private static java.util.List<Color> createTransition(Color color1, Color color2, int listSize) {
        java.util.List<Color> list = new ArrayList<>();
        int red1 = color1.getRed();
        int green1 = color1.getGreen();
        int blue1 = color1.getBlue();
        int red2 = color2.getRed();
        int green2 = color2.getGreen();
        int blue2 = color2.getBlue();
        int redDiff = Math.abs(red1 - red2);
        int greenDiff = Math.abs(green1 - green2);
        int blueDiff = Math.abs(blue1 - blue2);
        boolean red2Bigger = red1 < red2;
        boolean green2Bigger = green1 < green2;
        boolean blue2Bigger = blue1 < blue2;
        for (int i = 1; i <= listSize; i++) {
            int red = red1 + (red2Bigger ? 1 : -1) * (redDiff / listSize * i);
            int green = green1 + (green2Bigger ? 1 : -1) * (greenDiff / listSize * i);
            int blue = blue1 + (blue2Bigger ? 1 : -1) * (blueDiff / listSize * i);
            Color color = new Color(red, green, blue);
            list.add(color);
        }
        return list;
    }
}
