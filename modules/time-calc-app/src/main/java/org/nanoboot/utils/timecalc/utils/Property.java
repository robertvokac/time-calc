package org.nanoboot.utils.timecalc.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Robert
 * @since 23.02.2024
 */
public class Property <T>{
    @Getter @Setter
    private T value;
    public Property(T valueIn) {
        this.value = valueIn;
    }
}
