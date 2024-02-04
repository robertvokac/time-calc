package org.nanoboot.utils.timecalc.gui.common;

import javax.swing.JComponent;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pc00289
 * @since 21.02.2024
 */
public class ComponentRegistry {
    private final Set<Component> set = new HashSet<>();
    public ComponentRegistry() {

    }
    public void add(JComponent component) {
        this.set.add(component);
    }
    public void addAll(JComponent... component) {
        for(JComponent c:component) {
            add(c);
        }
    }

    public void setVisible(boolean b) {
        for(Component c:set) {
            c.setVisible(b);
        }
    }
}
