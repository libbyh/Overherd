package viz.control;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

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
		
			
			//set for keyword view
			StringBuffer buffer=new StringBuffer();
			int count=0;
			ArrayList<String> tagStringList=new ArrayList<String>();	//for highlighting keywords
			for(MyDocument m:mSet){
				buffer.append("-----------------------------------------------------\n");
				ArrayList<TagWithTFIDF> tags=m.getTagSet();
				int numToShow=(int)(tags.size()*ValueRegistry.KEYWORD_PERCENTAGE_TO_SHOW);
				numToShow=Math.max(numToShow, 1);
				System.out.println("Showing "+numToShow+ " keywords...");
				
				for(int i=0; i<numToShow; i++){
					TagWithTFIDF tag=tags.get(i);
					tagStringList.add(tag.getTag());
					
					buffer.append(tag.getTag());
					buffer.append(", ");
					
					++count;
				}
				
				buffer.append("\n");
			
			
			ComponentRegistry.registeredAuthorTopicVizUI.setKeywordViewLabelText(sourceAuthor + " and "+neighborAuthor+
					"\'s keywords (showing top "+ (int)(100*ValueRegistry.KEYWORD_PERCENTAGE_TO_SHOW)+"%):");
			
			ComponentRegistry.registeredAuthorTopicVizUI.getKeywordView().setText(buffer.toString());
			ComponentRegistry.registeredAuthorTopicVizUI.getKeywordView().setCaretPosition(0);
			}
			

			
			//set for full text view
			buffer=new StringBuffer();
			count=0;
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
		
			
			//highlight the words
			highlightKeywords(ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView(), tagStringList);
	}
	
	
	/**
	 * Highlight all the words
	 * @param component
	 * @param tags
	 */
	public void highlightKeywords(JTextComponent component, ArrayList<String> tags){
		try{
			Highlighter hilite=component.getHighlighter();
			Document doc=component.getDocument();
			String text=doc.getText(0, doc.getLength());
			
			for(String tag:tags){
				int pos=0;
				while((pos=text.indexOf(tag,pos))>=0){
					hilite.addHighlight(pos, pos+tag.length(), ValueRegistry.DEFAULT_KEYWORD_HIGHLIGHTER);
					pos+=tag.length();
				}
			}
			
			
			
		}catch(BadLocationException e){
			e.printStackTrace();
		}
	}
}
