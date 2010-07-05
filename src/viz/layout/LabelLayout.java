package viz.layout;
/**
 * @author kevin
 * A label decorator layout
 */

import java.util.Iterator;

import prefuse.*;
import prefuse.action.layout.*;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;


public class LabelLayout extends Layout{
	public LabelLayout(String group){
		super(group);
	}
	public void run(double num){
		Iterator iter=m_vis.items(m_group);
		
		while(iter.hasNext()){
			//for each label
			DecoratorItem label=(DecoratorItem)iter.next();
		/*	StringBuffer tempBuff=new StringBuffer();
			tempBuff.append(label.getString("name"));
			int spaceCount=0;
			int buffLen=tempBuff.length();
			for(int i=0; i<buffLen; i++){
				if(tempBuff.charAt(i)==' '){
					spaceCount++;
				}
				if (spaceCount==8){
					tempBuff.insert(i, '\n');
					break;
				}
			}
			*/
		//	System.out.println("Label:" + label.getString("name"));
			//set its location to the center of the node it belongs to
			VisualItem n=label.getDecoratedItem();
			
		//	System.out.println("Width: "+n.getBounds().getWidth());
			this.setX(label, null, n.getBounds().getCenterX());
			this.setY(label, null, n.getBounds().getCenterY());
		}
	}
}
