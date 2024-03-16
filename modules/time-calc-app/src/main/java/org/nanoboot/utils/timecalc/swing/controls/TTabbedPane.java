/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.nanoboot.utils.timecalc.swing.controls;

import javax.swing.JTabbedPane;

/**
 *
 * @author robertvokac
 */
public class TTabbedPane extends JTabbedPane {
    public int getIndexFor(String title) {
        
            for (int i = 0; i < getTabCount(); i++) {
                if (getTitleAt(i).equals(title)) {
                    return i;
                }
            }
            return -1;
    }
    
    public void switchTo(String title) {
        this.setSelectedIndex(getIndexFor(title));
    }
    
}
