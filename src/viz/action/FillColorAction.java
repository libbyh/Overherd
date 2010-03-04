package viz.action;
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
	private ColorMap cmap=new ColorMap(ColorLib.getInterpolatedPalette(10,
			ColorLib.rgb(85, 85, 85), ColorLib.rgb(0, 0, 0)), 0, 9);
	
	public int getColor(VisualItem item){
		if(item instanceof NodeItem){
			NodeItem ni=(NodeItem)item;
			if(ni.getChildCount()>0){
				return 0;
			}else{
				if(m_vis.isInGroup(ni, Visualization.SEARCH_ITEMS)){
					return ColorLib.rgb(191,99,130);
			//	}else if(m_vis.isInGroup(ni, Visualization.SELECTED_ITEMS)){
			//		return ColorLib.rgb(142, 88, 23);
				}else{
					return cmap.getColor(ni.getDepth());
				}
			}
		}else{
			return cmap.getColor(0);
		}
	}
}
