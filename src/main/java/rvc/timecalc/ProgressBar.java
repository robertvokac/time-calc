package rvc.timecalc;


import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import java.awt.Color;

/**
 * @author Robert
 * @since 20.04.2022
 */
public class ProgressBar extends JProgressBar {
    private static final String DOT_DOT_DOT = "";

    private boolean coloring = true;
    public ProgressBar() {
        super();
        setValue(0);
        setBorderPainted(true);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    @Override
    public void setValue(int n) {
        super.setValue(n);
        setStringPainted(true);
        setForeground(coloring ? ProgressBarColor.forProgress(n) : Color.GRAY);
        setString(DOT_DOT_DOT);
    }

}
