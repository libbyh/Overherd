package ui.treemap.viz.action;
/**
 * 
 * @author kevin
 * A customized fill color action.
 */

import prefuse.Visualization;
import prefuse.action.assignment.*;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class FillColorAction extends ColorAction {
	public FillColorAction(String group){
		super(group, VisualItem.FILLCOLOR);
	}
//	private ColorMap cmap=new ColorMap(ColorLib.getInterpolatedPalette(10,
	//		ColorLib.rgb(85, 85, 85), ColorLib.rgb(0, 0, 0)), 0, 9);
	
	private ColorMap cmap=new ColorMap(ColorLib.getInterpolatedPalette(10, 
			ColorLib.rgb(0, 204, 255), ColorLib.rgb(0, 0, 153)),0,9);
	
	
	public int getColor(VisualItem item){
		if(item instanceof NodeItem){
			NodeItem ni=(NodeItem)item;
			if(ni.getChildCount()>0 && !ni.isHighlighted()){
			//	System.out.println("node with children");
			//	return ColorLib.rgba(255, 153, 51, 50);
				return 0;
			}else{
				if(item.isInGroup(Visualization.SEARCH_ITEMS)){
					System.out.println("search");
					return ColorLib.rgb(191,99,130);
			//	}else if(m_vis.isInGroup(ni, Visualization.SELECTED_ITEMS)){
			//		return ColorLib.rgb(142, 88, 23);
				}else if(item.isInGroup(Visualization.FOCUS_ITEMS)){
					System.out.println("highlight");
					return ColorLib.rgb(255,200,125);
				}else if(item.isHighlighted()){
					return ColorLib.rgb(255, 100, 100);
				}else{
					return cmap.getColor(ni.getDepth());
				}
			}
		}else{	//edge?
			return cmap.getColor(0);
		//	return ColorLib.rgb(100, 100, 100);
		}
	}
}
