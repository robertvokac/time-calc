package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.utils.common.ProgressSmiley;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.io.File;
import java.io.IOException;

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

        helpHtml = "<div style=\"font-family:sans;margin-bottom:20px;\">" + helpHtml + "</div>";
        System.out.println(helpHtml);
        this.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(1, 1,
                getWidth() - SwingUtils.MARGIN,
                getHeight() - SwingUtils.MARGIN);
        add(scrollPane);

        JEditorPane editor = new JEditorPane();
        scrollPane.setViewportView(editor);

        editor.setContentType("text/html");
        editor.setText(helpHtml);
    }
}
