package util;

import prefuse.util.ui.JPrefuseApplet;
import ui.treemap.MainUI;


public class UIApplet extends JPrefuseApplet {

    public void init() {
        this.setContentPane(
            new MainUI("http://www.kevinnam.com/overherd/data/forumTree.xml")
            );
    }
    
} // end of class TreeMap
