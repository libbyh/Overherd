package ui;

/**
 * @author kevin nam
 * @version 0.1
 * 
 * The main user interfaces.
 * It has three components.  1) the treemap display 2) viewer for content 3) controller
 */

import javax.swing.*;

public class MainUI extends JFrame {
	private int width=1000;
	private int height=900;
	
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
	}
	
	public static void main(String args[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				MainUI ui=new MainUI();
			}
		});
	}
}