package com.robertvokac.utils.timecalc.swing.windows;

import com.robertvokac.utils.timecalc.swing.controls.TWindow;
import com.robertvokac.utils.timecalc.utils.common.Utils;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.io.IOException;
import com.robertvokac.utils.timecalc.swing.common.SwingUtils;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class HelpWindow extends TWindow {

    public HelpWindow() {
        setSize(800, 600);
        setTitle("Help");
        String helpHtml = "";
        try {
            String helpMd
                    = Utils.readTextFromTextResourceInJar("help/Readme.md");

            helpHtml = com.github.rjeschke.txtmark.Processor.process(
                    helpMd);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                    e.getMessage(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        helpHtml = "<div style=\"font-family:sans;margin-bottom:20px;\">"
                + helpHtml + "</div>";

        this.setLayout(null);
        JScrollPane scrollPane
                = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(1, 1,
                getWidth() - 2 * SwingUtils.MARGIN,
                getHeight() - 4 * SwingUtils.MARGIN);
        add(scrollPane);

        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);
        scrollPane.setViewportView(editor);

        editor.setContentType("text/html");
        editor.setText(helpHtml);
    }
}
