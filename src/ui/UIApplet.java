package ui;

import prefuse.util.ui.JPrefuseApplet;


public class UIApplet extends JPrefuseApplet {

    public void init() {
        this.setContentPane(
            new MainUI()
            );
    }
    
} // end of class TreeMap
