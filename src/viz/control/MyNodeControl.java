package viz.control;

import java.awt.event.MouseEvent;

import javax.swing.JTextPane;

import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;

import ui.*;

public class MyNodeControl extends ControlAdapter {
	private MainUI associatedMainUI=null;
	private JTextPane associatedTextPane=null;
	
	public MyNodeControl(MainUI ui){
		this.associatedMainUI=ui;
		this.associatedTextPane=ui.getViewerTextPane();
	}
	
	
	public void itemEntered(VisualItem item, MouseEvent e){
	//	title.setText(item.getString(label));
		if(item.canGetString("type")){
			String type=item.getString("type");
			if(type.equals("topic")){
				if (item.canGetDate("topic_date")){
					System.out.println("Topic date: " + item.getDate("topic_date"));
				}else{
					System.out.println("Topic date failed: " + item.getString("topic_date"));
				}
			}
		}
	//	System.out.println("itemEntered:"+item.getString(label));
	}
	
	public void itemExited(VisualItem item, MouseEvent e){
	//	title.setText(null);
	//	System.out.println("itemExited:"+item.getString(label));
	}
	
	public void itemClicked(VisualItem item, MouseEvent e){
		StringBuffer buffer=new StringBuffer();
		if(item.getString("type").equals("forum")){
		//	buffer.append(item.getString("))
		}else if(item.getString("type").equals("topic")){
			
		}else if(item.getString("type").equals("message")){
			buffer.append("Post id: ");
			buffer.append(item.getString("message_id"));
			buffer.append("\n");
			
			buffer.append("Title: ");
			buffer.append(item.getString("message_title"));
			buffer.append("\n");
			
			buffer.append("User: ");
			buffer.append(item.getString("message_author"));
			buffer.append("\n");
			
			buffer.append("Date: ");
			buffer.append(item.getString("message_date"));
			buffer.append("\n---------------------------------------------------------------\n");
			
	//	buffer.append("Text: ");
			buffer.append(item.getString("message_body"));
		}
		
		this.associatedTextPane.setText(buffer.toString());
		this.associatedTextPane.setCaretPosition(0);
	//	System.out.println("itemClicked:" +item.getString("type")+":"+ item.getString("message_body"));
	}
}
