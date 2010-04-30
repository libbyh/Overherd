package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import prefuse.Visualization;

import registry.ComponentRegistry;

public class AuthorTopicVizUI extends JFrame implements ItemListener {

	private JCheckBox directAuthorsCheck;
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
		Box cBox=new Box(BoxLayout.Y_AXIS);
		
		mainPanel.setSize(200, 800);
		
		directAuthorsCheck=new JCheckBox("Show only direct authors");
		directAuthorsCheck.setSelected(false);
		directAuthorsCheck.addItemListener(this);
		
		showTopicsCheck=new JCheckBox("Show topics discussed");
		showTopicsCheck.setSelected(false);
		showTopicsCheck.addItemListener(this);
		
		cBox.add(new JLabel("- Conversation viewer -"));
	//	cBox.add(directAuthorsCheck);
	//	cBox.add(showTopicsCheck);
		
		JPanel contentPanel=new JPanel();
		
		
		mainPanel.add(cBox);
		return mainPanel;
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
