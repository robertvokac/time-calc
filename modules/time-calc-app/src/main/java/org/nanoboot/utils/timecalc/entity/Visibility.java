package org.nanoboot.utils.timecalc.entity;

import org.nanoboot.utils.timecalc.utils.property.StringProperty;

/**
 * @author Robert
 * @since 23.02.2024
 */
public enum Visibility {
    STRONGLY_COLORED, WEAKLY_COLORED, GRAY, NONE;
    public boolean isStronglyColored() {
        return this == STRONGLY_COLORED;
    }
    public boolean isWeaklyColored() {
        return this == WEAKLY_COLORED;
    }
    public boolean isGray() {
        return this == GRAY;
    }
    public boolean isNone() {
        return this == NONE;
    }
    public boolean isNotNone() {
        return !isNone();
    }
    public static Visibility ofProperty(StringProperty stringProperty) {
        return Visibility.valueOf(stringProperty.getValue());
    }
}
