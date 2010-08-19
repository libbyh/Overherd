package ui.authormap;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import nlp.MyDocument;
import nlp.TFIDFHandler;

import prefuse.Visualization;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tree;
import prefuse.util.ColorLib;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

import registry.ComponentRegistry;
import registry.ValueRegistry;
import ui.authormap.data.UserConversationMap;
import ui.authormap.viz.AuthorTopicViz;
import ui.treemap.viz.TreeMap;


/**
 * User interfaces for visualizing students (authors) and their conversations.
 * 
 * Each node represents a student.  A link connects two nodes if the two students had a conversation (i.e., one replied to 
 * another's post).
 * 
 * A user can double click a node to center it.  The node's neighbor nodes represents all the students with whom the
 * centered student had a conversation with.  Clicking a neighbor node shows their conversations and keywords in the
 * respective views.
 * 
 */
public class AuthorTopicVizUI extends JFrame implements ItemListener {

	private JTabbedPane tabbedPane=new JTabbedPane();
	private JPanel keywordView=new JPanel();
	private JPanel sentenceView=new JPanel();
	private JPanel fullTextView=new JPanel();
	
	private JTextArea fullTextArea;
	private JLabel fullTextViewLabel;
	
	private JTextArea sentenceArea;
	private JLabel sentenceViewLabel;
	
	private JTextArea keywordArea;
	private JLabel keywordViewLabel;
	

    private ContentAnalysisTask cTask;
	private JProgressBar progressBar=new JProgressBar(0,100);
    
	JCheckBox showTopicsCheck;

	
	public AuthorTopicVizUI(String title){
		super(title);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
		JPanel cp=getControlPanel();
		
		getContentPane().add(cp,BorderLayout.WEST);
		getContentPane().add(getAuthorTopicViz(), BorderLayout.CENTER);
		setSize(1200, 800);
		ComponentRegistry.registeredAuthorTopicVizUI=this;
		setVisible(false);
	}
	
	public AuthorTopicViz getAuthorTopicViz(){
		if(ComponentRegistry.registeredAuthorTopicViz==null){
			ComponentRegistry.registeredAuthorTopicViz=new AuthorTopicViz();
		}
		return ComponentRegistry.registeredAuthorTopicViz;
	}
	
	
	public void showUI(){
		 if(!ValueRegistry.CONTENT_ANALYZED){
		    ComponentRegistry.registeredProgressBarFrame.showUI("Please wait while analyzing content...");
	        cTask=new ContentAnalysisTask();
	        cTask.execute();
	        
	        ValueRegistry.CONTENT_ANALYZED=true;
        }
		 
		 this.setVisible(true);
	}
	
	/**
	 * ContentAnalysisTask that analyzes the conversations and calculates the tf*idf values for the words
	 * Since it's a time consuming work, I use a SwingWorker class.
	 * 
	 * @author <a href="http://kevinnam.com">kevin nam</a>
	 *
	 */
	class ContentAnalysisTask extends SwingWorker<Void, String>{
    	public Void doInBackground(){
    		setupContentAnalysis();
    		return null;
    	}
    	
    	protected void done(){
    		try{
    			ComponentRegistry.registeredProgressBarFrame.hideUI();
    			JOptionPane.showMessageDialog(ComponentRegistry.registeredAuthorTopicViz,
    					"Double click a student to center him/her. \nClick his/her direct neighbor to view conversations between two students.");
    		}catch(Exception e){
    			
    		}
    	}
    }
    
	
	/**
	 * Various content panels that show conversations and keywords
	 */
	public JPanel getControlPanel(){
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
	
		mainPanel.setSize(200, 800);
			
		showTopicsCheck=new JCheckBox("Show topics discussed");
		showTopicsCheck.setSelected(false);
		showTopicsCheck.addItemListener(this);
		
		mainPanel.add(new JLabel("- Conversation viewer -"), BorderLayout.NORTH);
	
		tabbedPane.setPreferredSize(new Dimension(400,600));
		tabbedPane.addTab("Keyword view", keywordView);
		tabbedPane.addTab("Fulltext view", fullTextView);
		
		setupKeywordView();
		setupFullTextView();
		
		keywordView.setToolTipText("Dislays various keywords found in the conversation\n between two students.");
		sentenceView.setToolTipText("Dislays selected sentences found in the conversation\n between two students.");
		fullTextView.setToolTipText("Displays full conversation between two students.");
		
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		return mainPanel;
	}
	
	/**
	 * Sets up the keyword view panel
	 */
	public void setupKeywordView(){
	
		this.keywordView.setLayout(new BorderLayout());
		this.keywordViewLabel=new JLabel("Click a student to center and hover over another to see conversation.");
		this.keywordView.add(keywordViewLabel,BorderLayout.NORTH);
		
		JScrollPane scrollPane=new JScrollPane();
		keywordArea=new JTextArea();
		keywordArea.setLineWrap(true);
		keywordArea.setWrapStyleWord(true);
		keywordArea.setFont(ValueRegistry.TEXT_FONT);
		keywordArea.setForeground(ValueRegistry.KEYWORD_COLOR);
	
		scrollPane.setViewportView(keywordArea);
		
		keywordView.add(scrollPane,BorderLayout.CENTER);
	}
	
	public void setKeywordViewLabelText(String t){
		this.keywordViewLabel.setText(t);
	}
	
	public JTextArea getKeywordView(){
		return this.keywordArea;
	}
	
	public void setupSentenceView(){
		
	}
	
	public int getSelectedTabIndex(){
		return this.tabbedPane.getSelectedIndex();
	}
	
	/**
	 * Sets us the fullTextView that shows the entire conversations with keywords highlighted
	 */
	public void setupFullTextView(){
		this.fullTextView.setLayout(new BorderLayout());
		fullTextViewLabel=new JLabel("Click a student to center and hover over another to see conversation.");
		fullTextView.add(fullTextViewLabel,BorderLayout.NORTH);
		
		JScrollPane scrollPane=new JScrollPane();
		fullTextArea=new JTextArea();
		fullTextArea.setLineWrap(true);
		fullTextArea.setWrapStyleWord(true);
		fullTextArea.setFont(ValueRegistry.TEXT_FONT);
		
		
		scrollPane.setViewportView(fullTextArea);
		
		fullTextView.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setFullTextViewLabelText(String t){
		this.fullTextViewLabel.setText(t);
	}
	
	public JTextArea getFullTextView(){
		return this.fullTextArea;
	}
	
	// not used at the moment
	public void itemStateChanged(ItemEvent e){
		Object source=e.getItemSelectable();
		AuthorTopicViz display=getAuthorTopicViz();
		Visualization viz=display.getVisualization();
		/*
		if(source==directAuthorsCheck){
			if(e.getStateChange()==ItemEvent.SELECTED){
			//	display.dFilter.setDistance(1);
			//	viz.run("filterRedraw");
			}else if (e.getStateChange()==ItemEvent.DESELECTED){
			//	display.dFilter.setDistance(display.MaxDepth);
			//	viz.run("filterRedraw");
			}
		}else if(source==showTopicsCheck){
			if(e.getStateChange()==ItemEvent.SELECTED){
				
			}else if (e.getStateChange()==ItemEvent.DESELECTED){
				
			}
		}
		*/
	}
	
	/**
     * Sets up author-author matrix with conversation contents.  
     * 
     * Also calculate TFIDF values for words
     * 
     */
    public void setupContentAnalysis(){
    	
    	//Check if findUniqueAuthors has been called
    	AuthorTopicViz atViz=ComponentRegistry.registeredAuthorTopicViz;
    	
    	if(atViz.getAuthorCountMap().isEmpty()){
    		atViz.findUniqueAuthors();
    	}
    	
    	//for each author, create a UserConversationMap object and put it in the map
    	if(ComponentRegistry.registeredUserMatrix==null){
    		ComponentRegistry.registeredUserMatrix=new HashMap<String,UserConversationMap>();
    	}
    	
    	Set<String> authors=atViz.getAuthorCountMap().keySet();
    	for(String author:authors){
    		ComponentRegistry.registeredUserMatrix.put(author, new UserConversationMap(author));
    	}
    	    	
    	//add each node documents 
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	
    	if(ComponentRegistry.registeredTFIDFHandler==null){
    		ComponentRegistry.registeredTFIDFHandler=
    			new TFIDFHandler();
    	}
    	TFIDFHandler handler=ComponentRegistry.registeredTFIDFHandler;
    	
    	//for each edge, find two authors, update user matrix
    	Tree tree=treeMap.getTree();
    	int numEdges=tree.getEdgeCount();
    	
    	int countDone=0;
    	Iterator iter=tree.edges();
    	while(iter.hasNext()){
    		Edge edge=(Edge)iter.next();
    		VisualItem edgeItem=treeMap.getVisualization().getVisualItem("tree.edges", edge);
    		
    		Node sourceNode=edge.getSourceNode();
    		Node targetNode=edge.getTargetNode();
    		String sourceAuthor=sourceNode.getString("author");
    		String targetAuthor=targetNode.getString("author");
    		MyDocument sourceDocument=null;
    		MyDocument targetDocument=null;
    		
    		String sourceMessage="";
    		String targetMessage="";
    		if(sourceNode.canGetString("message_body")){
    			sourceMessage=sourceNode.getString("message_body");
    			sourceDocument=new MyDocument(sourceAuthor,sourceMessage);
    		}
    		if(targetNode.canGetString("message_body")){
    			targetMessage=targetNode.getString("message_body");
    			targetDocument=new MyDocument(targetAuthor,targetMessage);
    		}
    		
    		//if A wrote message Ma and B replied to it with Mb, map A<->B:Ma, A<->B:Mb, and save this information for each A and B
    		UserConversationMap sourceMap=ComponentRegistry.registeredUserMatrix.get(sourceAuthor);
    		UserConversationMap targetMap=ComponentRegistry.registeredUserMatrix.get(targetAuthor);
    		if(sourceMessage!=null && !sourceMessage.equals("")){
    			//if map already has the other author
    			if(sourceMap.getConversationMap().containsKey(targetAuthor)){
    				HashSet<MyDocument> mList=sourceMap.getConversationMap().get(targetAuthor);
    				mList.add(sourceDocument);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				sourceMap.getEntireConversationBuffer().append(sourceMessage);
    				sourceMap.getEntireConversationBuffer().append(" ");
    			}else{//create an entry for the other author
    				HashSet<MyDocument> mList=new HashSet<MyDocument>();
    				mList.add(sourceDocument);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				sourceMap.getEntireConversationBuffer().append(sourceMessage);
    				sourceMap.getEntireConversationBuffer().append(" ");
    			}
    			//do the same for target author
    			//if map already has the other author
    			if(targetMap.getConversationMap().containsKey(sourceAuthor)){
    				HashSet<MyDocument> mList=targetMap.getConversationMap().get(sourceAuthor);
    				mList.add(sourceDocument);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				targetMap.getEntireConversationBuffer().append(targetMessage);
    				targetMap.getEntireConversationBuffer().append(" ");
    			}else{//create an entry for the other author
    				HashSet<MyDocument> mList=new HashSet<MyDocument>();
    				mList.add(sourceDocument);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				targetMap.getEntireConversationBuffer().append(targetMessage);
    				targetMap.getEntireConversationBuffer().append(" ");
    			}
    		}
    		
    		if(targetMessage!=null && !targetMessage.equals("")){
    			//if map already has the other author
    			if(sourceMap.getConversationMap().containsKey(targetAuthor)){
    				HashSet<MyDocument> mList=sourceMap.getConversationMap().get(targetAuthor);
    				mList.add(targetDocument);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				
    			}else{//create an entry for the other author
    				HashSet<MyDocument> mList=new HashSet<MyDocument>();
    				mList.add(targetDocument);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    			}
    			//do the same for target author
    			//if map already has the other author
    			if(targetMap.getConversationMap().containsKey(sourceAuthor)){
    				HashSet<MyDocument> mList=targetMap.getConversationMap().get(sourceAuthor);
    				mList.add(targetDocument);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				
    			}else{//create an entry for the other author
    				HashSet<MyDocument> mList=new HashSet<MyDocument>();
    				mList.add(targetDocument);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    			}
    		}
   
    		countDone+=1;
    	}
    	
    	
    	
    	/*Iterator iter=treeMap.getTree().nodes();
    	while(iter.hasNext()){
    		Node node=(Node)iter.next();
    		VisualItem nodeItem=treeMap.getVisualization().getVisualItem("tree.nodes", node);
    		
    		if(nodeItem.canGetString("message_body")){
    			System.out.println("node item:"+nodeItem);
    			String text=nodeItem.getString("message_body");
    			
    			System.out.println("body text:"+text);
    			if(text!=null){
    				handler.addDoc(text);
    			}
    		}
    	}
    	*/
    	
    	//update the edge size
    	
    	
    //	handler.calculateTFIDFForAllWords();
    	setupTFIDF();
    }
    
    
    /**
     * Set up for TFIDF for finding keywords
     */
    
    public void setupTFIDF(){
    //	
    	if(ComponentRegistry.registeredTFIDFHandler==null){
    		ComponentRegistry.registeredTFIDFHandler=
    			new TFIDFHandler();
    	}
    	
    	//add each node documents 
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	TFIDFHandler handler=ComponentRegistry.registeredTFIDFHandler;
    	Set<String>authors=ComponentRegistry.registeredAuthorTopicViz.getAuthorCountMap().keySet();
    	
    	//for each author, get conversation map.  
    	//Note: this is not entirely correct since conversations from author A->B and B->A will be counted
    	//But for calculating tf*idf, it doesn't matter???
    	
    	for(String author:authors){
    		UserConversationMap cMap=ComponentRegistry.registeredUserMatrix.get(author);
    		//get the conversation map with the other authors and contents.
    		Set<String>otherAuthors=cMap.getConversationMap().keySet();
    		for(String otherAuthor:otherAuthors){
    			HashSet<MyDocument>docs=cMap.getConversationMap().get(otherAuthor);
    			//add each document to handler
    			Iterator<MyDocument>iter=docs.iterator();
    			while(iter.hasNext()){
    				handler.addDoc(iter.next());
    			}
    		}
    		
    	}
    	
    	handler.calculateTFIDFForAllWords();
    	
    }
    
    
	
}
