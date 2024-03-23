package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.swing.progress.Battery;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author pc00289
 * @since 21.03.2024
 */
public class WidgetMenu extends JPopupMenu {
    private final JMenuItem typeMinuteMenuItem;
    private final JMenuItem typeHourMenuItem;
    private final JMenuItem typeDayMenuItem;
    private final JMenuItem typeWeekMenuItem;
    private final JMenuItem typeMonthMenuItem;
    private final JMenuItem typeYearMenuItem;
    private final Widget widget;
    private WidgetType selectedType;
    private Consumer<Object> refreshConsumer;

    public WidgetMenu(Widget widget) {
        this(widget, null);
    }
    public WidgetMenu(Widget widget, Consumer<Object> refreshConsumer) {
        this.widget = widget;
        this.refreshConsumer = refreshConsumer;

        JMenuItem typeMenuItem = new JMenu("Type"/*,*/
                /*new ImageIcon("images/newproject.png")*/);
        //menuItem.setMnemonic(KeyEvent.VK_P);
        //menuItem.getAccessibleContext().setAccessibleDescription("New Project");
        add(typeMenuItem);

        this.typeMinuteMenuItem = new JMenuItem(WidgetType.MINUTE.name());
        this.typeHourMenuItem = new JMenuItem(WidgetType.HOUR.name());
        this.typeDayMenuItem = new JMenuItem(WidgetType.DAY.name());
        this.typeWeekMenuItem = new JMenuItem(WidgetType.WEEK.name());
        this.typeMonthMenuItem = new JMenuItem(WidgetType.MONTH.name());
        this.typeYearMenuItem = new JMenuItem(WidgetType.YEAR.name());
        typeMenuItem.add(typeMinuteMenuItem);
        typeMenuItem.add(typeHourMenuItem);
        typeMenuItem.add(typeDayMenuItem);
        typeMenuItem.add(typeWeekMenuItem);
        typeMenuItem.add(typeMonthMenuItem);
        typeMenuItem.add(typeYearMenuItem);

        BiConsumer<JMenuItem, WidgetType> typeActionCreator = (m,w) -> {
            m.addActionListener(e -> {
                if(((widget instanceof Battery) || (widget instanceof AnalogClock)) && !widget.typeProperty.getValue().equals(w.name().toLowerCase(Locale.ROOT))) {
                    //nothing to do
                    return;
                }
                markAsSelected(w);
                widget.typeProperty.setValue(w.name().toLowerCase(Locale.ROOT));
            });
        };
        typeActionCreator.accept(typeMinuteMenuItem, WidgetType.MINUTE);
        typeActionCreator.accept(typeHourMenuItem, WidgetType.HOUR);
        typeActionCreator.accept(typeDayMenuItem, WidgetType.DAY);
        typeActionCreator.accept(typeWeekMenuItem, WidgetType.WEEK);
        typeActionCreator.accept(typeMonthMenuItem, WidgetType.MONTH);
        typeActionCreator.accept(typeYearMenuItem, WidgetType.YEAR);

        typeWeekMenuItem.addActionListener(e -> {
            markAsSelected(WidgetType.WEEK);
            widget.typeProperty.setValue(WidgetType.WEEK
                    .name().toLowerCase(Locale.ROOT));
        });
        typeMonthMenuItem.addActionListener(e -> {
            markAsSelected(WidgetType.MONTH);
            widget.typeProperty.setValue(WidgetType.MONTH
                    .name().toLowerCase(Locale.ROOT));
        });
        typeYearMenuItem.addActionListener(e -> {
            markAsSelected(WidgetType.YEAR);
            widget.typeProperty.setValue(WidgetType.YEAR
                    .name().toLowerCase(Locale.ROOT));
        });
        //if(!aClass.getSimpleName().contains("Battery")) {
            add(typeMenuItem);
        //}

        JMenuItem hideMenuItem = new JMenuItem("Hide");
        add(hideMenuItem);
        hideMenuItem.addActionListener(e -> {
            this.widget.hideWidget();
        });
    }
    public void markAsSelected(WidgetType widgetType) {
        this.typeMinuteMenuItem.setText(WidgetType.MINUTE.name());
        this.typeHourMenuItem .setText(WidgetType.HOUR.name());
        this.typeDayMenuItem.setText(WidgetType.DAY.name());
        this.typeWeekMenuItem.setText(WidgetType.WEEK.name());
        this.typeMonthMenuItem.setText(WidgetType.MONTH.name());
        this.typeYearMenuItem.setText(WidgetType.YEAR.name());
        switch (widgetType) {
            case MINUTE: typeMinuteMenuItem.setText(typeMinuteMenuItem.getText() + " (*)");break;
            case HOUR: typeHourMenuItem.setText(typeHourMenuItem.getText() + " (*)");break;
            case DAY: typeDayMenuItem.setText(typeDayMenuItem.getText() + " (*)");break;
            case WEEK: typeWeekMenuItem.setText(typeWeekMenuItem.getText() + " (*)");break;
            case MONTH: typeMonthMenuItem.setText(typeMonthMenuItem.getText() + " (*)");break;
            case YEAR: typeYearMenuItem.setText(typeYearMenuItem.getText() + " (*)");break;
            default: throw new TimeCalcException("Unsupported WidgetType: " + widgetType);
        }
        this.selectedType = widgetType;
    }
    public void refresh () {
        if(refreshConsumer == null) {
            //nothing to do
            return;
        }
        this.refreshConsumer.accept(null);
    }
}
