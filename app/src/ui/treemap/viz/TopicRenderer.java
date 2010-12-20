package ui.treemap.viz;



import java.util.Iterator;

import prefuse.render.*;
import prefuse.visual.*;

/**
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 * Topic renderer for rendering title label for the topic in the TreeMap
 */
public class TopicRenderer extends LabelRenderer {
	
	public TopicRenderer(String field){
		super(field);
	}
	
	/**
	 * Get the label from the data column named "name"
	 */
	protected String getText(VisualItem item){
	
		StringBuffer tempBuff=new StringBuffer();
		tempBuff.append(item.getString("name"));
		int spaceCount=0;
		int buffLen=tempBuff.length();
		for(int i=0; i<buffLen; i++){
			if(tempBuff.charAt(i)==' '){
				spaceCount++;
			}
			if (spaceCount==5){
				tempBuff.insert(i, '\n');
				spaceCount=0;
				break;
			}
		}
		return tempBuff.toString();
	}
}
