package ui.authormap.viz.render;

/**
 * 
 * @author kevin
 * Custom edge renderer.  Displays different edge visualizations according to the type
 */

import java.awt.*;
import java.util.HashSet;

import nlp.MyDocument;
import prefuse.render.*;
import prefuse.visual.*;
import prefuse.util.*;

import registry.*;
import ui.authormap.data.UserConversationMap;

public class AuthorVizEdgeRenderer extends EdgeRenderer {
	
	public AuthorVizEdgeRenderer(){
		super();
	}
	
	public AuthorVizEdgeRenderer(int edgeType){
		super(edgeType);
	}
	
	/**
	 * @override
	 * 
	 * Return a width according to the type
	 */
	protected double getLineWidth(VisualItem item){
		return 1.0;
		/*
		double width=0.0;
		EdgeItem edge=(EdgeItem)item;
		VisualItem node1=edge.getSourceItem();
		VisualItem node2=edge.getTargetItem();

		String sourceAuthor=node1.getString(VisualItem.LABEL);
		String neighborAuthor=node2.getString(VisualItem.LABEL);
		
		if(sourceAuthor==null || neighborAuthor==null){
			return 1.0;
		}
		
		UserConversationMap map=ComponentRegistry.registeredUserMatrix.get(sourceAuthor);
		
		if(map==null){
			ComponentRegistry.registeredAuthorTopicVizUI.getFullTextView().setText("");
			return 1.0;
		}
		
		
		HashSet<MyDocument> mSet=map.getConversationMap().get(neighborAuthor);
		if(mSet==null){
			return 1.0;
		}
		
		return 0.5 * mSet.size();
	//	return 5.0;
        */
	
	}

	
}
