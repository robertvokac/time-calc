package com.robertvokac.utils.timecalc.utils.common;

import lombok.Getter;
import com.robertvokac.utils.timecalc.app.TimeCalcException;

import java.util.Arrays;

/**
 * @author Robert Vokac
 * @since 26.02.2024
 */
public enum ProgressSmiley {

    SMILEY_1("😱", "face screaming in fear"),
    SMILEY_2("😡", "pouting face"),
    SMILEY_3("😠", "angry face"),
    SMILEY_4("😭", "loudly crying face"),
    SMILEY_5("😢", "crying face"),
    SMILEY_6("😞", "disappointed face"),
    SMILEY_7("😫", "tired face"),
    SMILEY_8("😨", "fearful face"),
    SMILEY_9("😲", "astonished face"),
    SMILEY_10("😦", "frowning face with open mouth"),
    SMILEY_11("😊", "smiling face with smiling eyes"),
    SMILEY_12("😃", "smiling face with open mouth"),
    SMILEY_13("😁", "grinning face with smiling eyes"),
    SMILEY_14("😎", "smiling face with sunglasses"),
    SMILEY_15("😍", "smiling face with heart-shaped eyes"),
    SMILEY_16("😈", "smiling face with horns");

    @Getter
    private final String character;
    @Getter
    private final String description;

    ProgressSmiley(String ch, String d) {
        this.character = ch;
        this.description = d;
    }

    public static ProgressSmiley forNumber(int number) {
        for (ProgressSmiley s : ProgressSmiley.values()) {
            if (s.getNumber() == number) {
                return s;
            }
        }
        throw new TimeCalcException(
                "There is no smiley with this number: " + number);
    }

    public static ProgressSmiley forProgress(double progress) {
        progress = progress * 100;
        if (progress < 0) {
            return SMILEY_1;
        }
        for (int i = 1; i < 16; i++) {
            if (progress < 100d / 16d * ((double) i)) {
                return forNumber(i);
            }
        }
        return SMILEY_16;
    }

    public static void main(String[] args) {
        Arrays.stream(values()).forEach(s -> {
            s.getCharacter().codePoints().mapToObj(Integer::toHexString)
                    .forEach(System.out::println);

        });
    }

    private static String toUnicode(char ch) {
        return String.format("\\u%04x", (int) ch);
    }

    public int getNumber() {
        return Integer.valueOf(this.name().replace("SMILEY_", ""));
    }
}
