package rvc.timecalc;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class BooleanHolder {
    private boolean b;

    public void set(boolean b) {
        this.b = b;
    }

    public boolean get() {
        return b;
    }

    public void flip() {
        this.b = !b;
    }
}
