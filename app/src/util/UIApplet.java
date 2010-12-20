package util;

import prefuse.util.ui.JPrefuseApplet;
import ui.treemap.MainUI;


public class UIApplet extends JPrefuseApplet {

    public void init() {
        this.setContentPane(
            new MainUI()
            );
    }
    
} // end of class TreeMap
