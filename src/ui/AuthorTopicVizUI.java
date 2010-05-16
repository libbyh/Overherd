package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import prefuse.Visualization;

import registry.ComponentRegistry;

public class AuthorTopicVizUI extends JFrame implements ItemListener {

	private JCheckBox directAuthorsCheck;
	private JTabbedPane tabbedPane=new JTabbedPane();
	private JPanel keywordView=new JPanel();
//	private JPanel keywordView2=new JPanel();
	private JPanel sentenceView=new JPanel();
	private JPanel fullTextView=new JPanel();
	
	private JTextArea fullTextArea;
	private JLabel fullTextViewLabel;
	
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
	
	public JPanel getControlPanel(){
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
	//	Box cBox=new Box(BoxLayout.Y_AXIS);
		
		mainPanel.setSize(200, 800);
		
		directAuthorsCheck=new JCheckBox("Show only direct authors");
		directAuthorsCheck.setSelected(false);
		directAuthorsCheck.addItemListener(this);
		
		showTopicsCheck=new JCheckBox("Show topics discussed");
		showTopicsCheck.setSelected(false);
		showTopicsCheck.addItemListener(this);
		
		mainPanel.add(new JLabel("- Conversation viewer -"), BorderLayout.NORTH);
	//	cBox.add(directAuthorsCheck);
	//	cBox.add(showTopicsCheck);
		
		tabbedPane.setPreferredSize(new Dimension(400,600));
		tabbedPane.addTab("Keyword view", keywordView);
		tabbedPane.addTab("Sentence view", sentenceView);
		tabbedPane.addTab("Fulltext view", fullTextView);
		
		setupKeywordView();
		setupSentenceView();
		setupFullTextView();
		
		keywordView.setToolTipText("Dislays various keywords found in the conversation\n between two students.");
		sentenceView.setToolTipText("Dislays selected sentences found in the conversation\n between two students.");
		fullTextView.setToolTipText("Displays full conversation between two students.");
		
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		return mainPanel;
	}
	
	/**
	 * Sets up keyword view panel
	 */
	public void setupKeywordView(){
	//	keywordView.add(ComponentRegistry.registeredAuthorTopicViz.setupKeywordView());
	}
	
	public void setupSentenceView(){
		
	}
	
	public void setupFullTextView(){
		this.fullTextView.setLayout(new BorderLayout());
		fullTextViewLabel=new JLabel("Click a student to center and hover over another to see conversation.");
		fullTextView.add(fullTextViewLabel,BorderLayout.NORTH);
		JScrollPane scrollPane=new JScrollPane();
		fullTextArea=new JTextArea();
		fullTextArea.setLineWrap(true);
		fullTextArea.setWrapStyleWord(true);
		
		
		scrollPane.setViewportView(fullTextArea);
		
		fullTextView.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setFullTextViewLabelText(String t){
		this.fullTextViewLabel.setText(t);
	}
	
	public JTextArea getFullTextView(){
		return this.fullTextArea;
	}
	
	public void itemStateChanged(ItemEvent e){
		Object source=e.getItemSelectable();
		AuthorTopicViz display=getAuthorTopicViz();
		Visualization viz=display.getVisualization();
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
	}
	
}
