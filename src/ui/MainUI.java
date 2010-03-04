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
import prefuse.data.query.*;
import prefuse.util.FontLib;
import prefuse.util.ui.*;
import prefuse.visual.*;

public class MainUI extends JPanel {
	private int width=1000;
	private int height=900;
	private String inputFile="D:/workspace3/Overherd/data/forumTree.xml";
	
	public MainUI(){
	//	super ("o v e r h e r d | v i s u a l i z a t i o n");
		createAndShowGUI("name");
	}
	
	public MainUI(String xmlPath){
		inputFile=xmlPath;
		createAndShowGUI("name");
	}
	
	public MainUI(int width, int height){
	//	super ("o v e r h e r d | v i s u a l i z a t i o n");
		this.width=width;
		this.height=height;
		createAndShowGUI("name");
	}
	
	public void createAndShowGUI(final String label){
	//	this.setSize(width, height);
		
	//	this.setVisible(true);
	//	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		Tree tree=null;
		try{
			tree=(Tree)new TreeMLReader().readGraph(inputFile);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		
	//	if(tree!=null){
	//		JPrefuseTree ft=new JPrefuseTree(new Tree(), label);
	//	ft.showTreeWindow(tree, "name");
	//	}
		final TreeMap treemap=new TreeMap(tree, label);
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
		
		final JTextPane textPane=new JTextPane();
		
		treemap.addControlListener(new ControlAdapter(){
			public void itemEntered(VisualItem item, MouseEvent e){
				title.setText(item.getString(label));
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
				title.setText(null);
			//	System.out.println("itemExited:"+item.getString(label));
			}
			
			public void itemClicked(VisualItem item, MouseEvent e){
				textPane.setText(item.getString("type")+":" +item.getString("message_body"));			
				System.out.println("itemClicked:" +item.getString("type")+":"+ item.getString("message_body"));
			}
		});
		
		Box box=UILib.getBox(new Component[]{title,searchPanel},true, 10, 3, 0);
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(treemap, BorderLayout.CENTER);
		panel.add(box, BorderLayout.SOUTH);
		UILib.setColor(panel, Color.BLACK, Color.GRAY);
		
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		
		//Viewer
		
		JPanel viewerPanel=new JPanel(new BorderLayout());
		viewerPanel.setPreferredSize(new Dimension(280,700));
		viewerPanel.add(new JLabel("c o n t e n t | v i e w e r "), BorderLayout.NORTH);
		
		
		textPane.setPreferredSize(new Dimension(275,675));
		textPane.setEditable(false);
		textPane.setText("Click on tree node to view content here.");
		JScrollPane viewScroll=new JScrollPane(textPane);
		viewerPanel.add(viewScroll, BorderLayout.CENTER);
	
		
		viewerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		UILib.setColor(viewerPanel, Color.BLACK, Color.GRAY);
		UILib.setColor(viewScroll, Color.BLACK, Color.GRAY);
		this.add(viewerPanel, BorderLayout.EAST);
		
		//Controller
		JPanel controlPanel=new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setPreferredSize(new Dimension(1000,200));
		controlPanel.add(new JLabel(" c o n t r o l l e r "), BorderLayout.NORTH);
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//UILib.setColor(controlPanel, Color.BLACK, Color.GRAY);
		this.add(controlPanel, BorderLayout.SOUTH);
		
		//range control
		JRangeSlider rangeSlider=treemap.getSlider();
		controlPanel.add(rangeSlider, BorderLayout.CENTER);
		
		//select control
		Box sBox=new Box(BoxLayout.Y_AXIS);
		JScrollPane dScroll=new JScrollPane(sBox);
		dScroll.setPreferredSize(new Dimension(130,200));
		JLabel dLabel=new JLabel("Select depth display");
		dLabel.setToolTipText("Select the node depths to display.");
		sBox.add(dLabel);
		
		int maxDepth=treemap.getMaxDepth();
		System.out.println("max depth of tree: "+maxDepth);
		for(int i=0; i<maxDepth; i++){
			final JCheckBox cB =new JCheckBox("Depth "+i);
			cB.setName(i+"");
			cB.setSelected(true);
			cB.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					int depth=Integer.parseInt(((JCheckBox)e.getSource()).getName());
					if(cB.isSelected()){//if
						
						System.out.println("Selected "+ depth);
						treemap.addDepthPredicate(depth);
						System.out.println(treemap.getDepthPredicates().size());
					}else{
						System.out.println("unselected "+ depth);
						treemap.removeDepthPredicate(depth);
						System.out.println(treemap.getDepthPredicates().size());
					}
				}
			});
			sBox.add(cB);
		}
		controlPanel.add(dScroll, BorderLayout.EAST);
	
		
	}
	
	public static void main(String args[]){
		String inputFile;
		if (args.length==1){
			inputFile=args[0];
		}else{
		//	inputFile="D:/workspace3/Overherd/data/forumTree5.xml";
			inputFile="E:/Users/kevin/My Documents/Aptana RadRails Workspace/Sakai/forumTree5b.xml";
	//	javax.swing.SwingUtilities.invokeLater(new Runnable(){
	//		public void run(){
				MainUI ui=new MainUI(inputFile);
				JFrame frame=new JFrame(" o v e r h e r d | v i s u a l i z a t i o n ");
				frame.setSize(1100, 1000);
				frame.setLayout(new BorderLayout());
				frame.getContentPane().add(ui);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//		}
	//	});
		}
	}
}