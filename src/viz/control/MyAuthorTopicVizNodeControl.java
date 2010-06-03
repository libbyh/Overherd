package viz.control;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import nlp.MyDocument;
import nlp.TagWithTFIDF;


import prefuse.controls.ControlAdapter;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.visual.VisualItem;
import registry.ComponentRegistry;
import registry.ValueRegistry;
import ui.UserConversationMap;

public class MyAuthorTopicVizNodeControl extends ControlAdapter {
	
	
	public void itemClicked(VisualItem item, MouseEvent e){
		
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
		
		
		HashSet<MyDocument> mSet=map.getConversationMap().get(neighborAuthor);
		if(mSet==null){
			return;
		}
		
	
		
		
		int view=ComponentRegistry.registeredAuthorTopicVizUI.getSelectedTabIndex();
		
		if(view==ValueRegistry.CONVERSATION_TEXT_TAB_VIEW){
			
			StringBuffer buffer=new StringBuffer();
			int count=0;
			for(MyDocument m:mSet){
				buffer.append("-----------------------------------------------------\n");
				buffer.append(m.getContent());
				buffer.append("\n\n");
				++count;
			}
			ComponentRegistry.registeredAuthorTopicVizUI.setFullTextViewLabelText(sourceAuthor + " and "+neighborAuthor+
					"\'s conversation: ("+count+" posts)");
			
			ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setText(buffer.toString());
			ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setCaretPosition(0);
		}else if(view==ValueRegistry.CONVERSATION_SENTENCE_TAB_VIEW){
			
			
		}else if(view==ValueRegistry.CONVERSATION_KEYWORD_TAB_VIEW){
			StringBuffer buffer=new StringBuffer();
			int count=0;
			for(MyDocument m:mSet){
				buffer.append("-----------------------------------------------------\n");
				Set<TagWithTFIDF> tags=m.getTagSet();
				for(TagWithTFIDF tag:tags){
					buffer.append(tag.getTag());
					buffer.append("\n\n");
					++count;
				}
			}
			
			ComponentRegistry.registeredAuthorTopicVizUI.setKeywordViewLabelText(sourceAuthor + " and "+neighborAuthor+
					"\'s conversation: ("+count+" words)");
			
			ComponentRegistry.registeredAuthorTopicVizUI.getKeywordView().setText(buffer.toString());
			ComponentRegistry.registeredAuthorTopicVizUI.getKeywordView().setCaretPosition(0);
		}
	}
}
