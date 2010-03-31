package viz.control;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import javax.swing.JTextPane;

import prefuse.Display;
import prefuse.controls.*;
import prefuse.data.Node;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import ui.MainUI;

public class MyWheelNaviControl extends ControlAdapter {
	private Point m_point = new Point();
	private NodeItem currentParentItem=null;    
    private VisualItem previousInvokingItem=null;
    
    private MainUI associatedMainUI=null;
 //   private JTextPane associatedViewerPane=null;
    
    public MyWheelNaviControl(MainUI ui){
    	this.associatedMainUI=ui;
    //	this.associatedViewerPane=ui.getViewerTextPane();
    }
    
	/**
     * @see prefuse.controls.Control#itemWheelMoved(prefuse.visual.VisualItem, java.awt.event.MouseWheelEvent)
     */
    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
        //if()
    	NodeItem node=(NodeItem)item;
        Node parent=node.getParent();
        VisualItem parentViz=(VisualItem)parent;
        
        if(e.getWheelRotation()>0){
        	 if(previousInvokingItem!=null){
             	if(!previousInvokingItem.isHover()){
             		if(currentParentItem!=null){
                     	currentParentItem.setHighlighted(false);
                     	currentParentItem.getVisualization().repaint();
                        currentParentItem.getVisualization().run("colors");
                        StringBuffer buffer=new StringBuffer();
                        
                 		if(currentParentItem.getString("type").equals("forum")){
                 		//	buffer.append(item.getString("))
                 		//	this.associatedMainUI.getViewerTextPane().setText("this is of type forum");
                 		}else if(currentParentItem.getString("type").equals("topic")){
                 		//	this.associatedMainUI.getViewerTextPane().setText("this is of type topic");
                 		}else if(currentParentItem.getString("type").equals("message")){
                 			buffer.append("Post id: ");
                 			buffer.append(currentParentItem.getString("message_id"));
                 			buffer.append("\n");
                 			
                 			buffer.append("Title: ");
                 			buffer.append(currentParentItem.getString("message_title"));
                 			buffer.append("\n");
                 			
                 			buffer.append("User: ");
                 			buffer.append(currentParentItem.getString("message_author"));
                 			buffer.append("\n");
                 			
                 			buffer.append("Date: ");
                 			buffer.append(currentParentItem.getString("message_date"));
                 			buffer.append("\n---------------------------------------------------------------\n");
                 			
                 	//	buffer.append("Text: ");
                 			buffer.append(currentParentItem.getString("message_body"));
                 		}
                 		if(this.associatedMainUI!=null)
                 			this.associatedMainUI.getViewerTextPane().setText(buffer.toString());			
                     }
             	}
             }
             previousInvokingItem=item;
             
             NodeItem parentItem=(NodeItem)item.getVisualization().getVisualItem("tree.nodes", parent);
             parentItem.setHighlighted(true);
             currentParentItem=parentItem;
             
         //    System.out.println(parent);
             parentItem.getVisualization().repaint();
             parentItem.getVisualization().run("colors");
        }else{
        	currentParentItem.setHighlighted(false);
        	currentParentItem.getVisualization().repaint();
            currentParentItem.getVisualization().run("colors");
        }
        
       
       
    }
}
