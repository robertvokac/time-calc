package org.nanoboot.utils.timecalc.swing.common;

import lombok.Getter;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ComponentRegistry<T extends Component> {

    @Getter
    private final Set<T> set = new HashSet<>();

    public ComponentRegistry() {

    }

    public void add(T component) {
        this.set.add(component);
    }

    public void addAll(T... component) {
        for (T c : component) {
            add(c);
        }
    }
    public void setVisible(boolean b) {
        setVisible(null, b);
    }

    public void setVisible(Predicate<Component> predicate, boolean b) {
        for (T c : set) {
            if(c instanceof TButton) {
                if(!MainWindow.hideShowCheckBox.isSelected() && b) {
                    continue;
                }
            }
            if(predicate != null) {
                if(!predicate.test(c)) {
                    continue;
                }
            }
            c.setVisible(b);
        }
    }

}
