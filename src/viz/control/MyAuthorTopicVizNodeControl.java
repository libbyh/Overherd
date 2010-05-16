package viz.control;

import java.awt.event.MouseEvent;
import java.util.HashSet;


import prefuse.controls.ControlAdapter;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.visual.VisualItem;
import registry.ComponentRegistry;
import ui.UserConversationMap;

public class MyAuthorTopicVizNodeControl extends ControlAdapter {
	
	
	public void itemEntered(VisualItem item, MouseEvent e){
		
		Graph graph=ComponentRegistry.registeredAuthorTopicViz.getAuthorGraph();
		Node rootNode=ComponentRegistry.registeredAuthorTopicViz.getCurrentRootNode();
		
		if(item==null){
			return;
		}
		
		if(rootNode==null){
			return;
		}
		
		String sourceAuthor=rootNode.getString(VisualItem.LABEL);
		String neighborAuthor=item.getString(VisualItem.LABEL);
		
		if(sourceAuthor==null || neighborAuthor==null){
			return;
		}
		
		if(sourceAuthor.equals(neighborAuthor)){
			ComponentRegistry.registeredAuthorTopicVizUI.setFullTextViewLabelText(
					"Select another student to see the conversation");
			ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setText("");
			return;
		}
		
		UserConversationMap map=ComponentRegistry.registeredUserMatrix.get(sourceAuthor);
		
		if(map==null){
			ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setText("");
			return;
		}
		
		
		HashSet<String> mSet=map.getConversationMap().get(neighborAuthor);
		if(mSet==null){
			return;
		}
		
	
		StringBuffer buffer=new StringBuffer();
		int count=0;
		for(String m:mSet){
			buffer.append("-----------------------------------------------------\n");
			buffer.append(m);
			buffer.append("\n\n");
			++count;
		}

		ComponentRegistry.registeredAuthorTopicVizUI.setFullTextViewLabelText(sourceAuthor + " and "+neighborAuthor+
				"\'s conversation: ("+count+" posts)");
		
		ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setText(buffer.toString());
		ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setCaretPosition(0);
	}
}
