package ui;

/**
 * @author kevin nam
 * @version 0.1
 * 
 * The main user interfaces.
 * It has three components.  1) the treemap display 2) viewer for content 3) controller
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefuse.controls.*;
import prefuse.data.*;
import prefuse.data.io.*;
import prefuse.util.FontLib;
import prefuse.util.ui.*;
import prefuse.visual.*;

public class MainUI extends JFrame {
	private int width=1000;
	private int height=900;
	private String inputFile="E:/Users/kevin/My Documents/Aptana RadRails Workspace/Sakai/forumTree.xml";
	
	public MainUI(){
		super ("o v e r h e r d | v i s u a l i z a t i o n");
		createAndShowGUI();
	}
	
	public MainUI(int width, int height){
		super ("o v e r h e r d | v i s u a l i z a t i o n");
		this.width=width;
		this.height=height;
		createAndShowGUI();
	}
	
	public void createAndShowGUI(){
		this.setSize(width, height);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		Tree tree=null;
		try{
			tree=(Tree)new TreeMLReader().readGraph(inputFile);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		
		final TreeMap treemap=new TreeMap(tree);
		JSearchPanel searchPanel=treemap.getSearchQuery().createSearchPanel(true);
		searchPanel.setShowResultCount(true);
		searchPanel.setShowCancel(true);
		searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
		searchPanel.setFont(FontLib.getFont("Tahoma",Font.PLAIN, 11));
		
		final JFastLabel title=new JFastLabel("               ");
		title.setPreferredSize(new Dimension(350,20));
		title.setVerticalAlignment(SwingConstants.BOTTOM);
		title.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		title.setFont(FontLib.getFont("Tahoma",Font.PLAIN, 16));
		
		treemap.addControlListener(new ControlAdapter(){
			public void itemEntered(VisualItem item, MouseEvent e){
				title.setText(item.getString("name"));
			}
			
			public void itemExited(VisualItem item, MouseEvent e){
				title.setText("");
			}
		});
		
		Box box=UILib.getBox(new Component[]{title,searchPanel},true, 10, 3, 0);
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(treemap, BorderLayout.CENTER);
		panel.add(box, BorderLayout.SOUTH);
		UILib.setColor(panel, Color.BLACK, Color.GRAY);
		
		this.setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		//Viewer
		JPanel viewerPanel=new JPanel(new BorderLayout());
		viewerPanel.setPreferredSize(new Dimension(200,700));
		viewerPanel.add(new JLabel(" v i e w e r "), BorderLayout.NORTH);
		viewerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		UILib.setColor(viewerPanel, Color.BLACK, Color.GRAY);
		this.getContentPane().add(viewerPanel, BorderLayout.EAST);
		
		//Controller
		JPanel controlPanel=new JPanel();
		controlPanel.setPreferredSize(new Dimension(1000,200));
		controlPanel.add(new JLabel(" c o n t r o l l e r "), BorderLayout.NORTH);
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//UILib.setColor(controlPanel, Color.BLACK, Color.GRAY);
		this.getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String args[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				MainUI ui=new MainUI();
			}
		});
	}
}