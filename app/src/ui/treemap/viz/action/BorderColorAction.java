package ui.treemap.viz.action;

import prefuse.action.assignment.*;
import prefuse.util.ColorLib;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

/**
 * 
 * @author <a href="http://kevinnam.com>kevin name</a>
 * A customized border color action for a node in the TreeMap
 * Change the color of the border according to the depth of the node.
 */

public class BorderColorAction extends ColorAction {
	public BorderColorAction(String group){
		super(group, VisualItem.STROKECOLOR);
	}
	
	public int getColor(VisualItem item){
		NodeItem ni=(NodeItem)item;
		if(ni.isHover()){
			return ColorLib.rgb(99, 130, 191);
		}
		int depth=ni.getDepth();
		if(depth==1){
			return ColorLib.gray(200);
		}else if(depth==2){
			return ColorLib.gray(140);//.rgb(255,0,102);
		}else if(depth==3){
			return ColorLib.gray(100);//rgb(120,255,0);
			
		}else {
			return ColorLib.gray(65);
		}
	}
}
