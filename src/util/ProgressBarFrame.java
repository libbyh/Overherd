package util;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import registry.ComponentRegistry;

/**
 * A progress bar that appears while performing a content analysis 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */
public class ProgressBarFrame extends JFrame{
	private JProgressBar pbar=new JProgressBar(0,100);
	public ProgressBarFrame(String title){
		super(title);
		ComponentRegistry.registeredProgressBarFrame=this;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		pbar.setIndeterminate(true);
		this.getContentPane().add(pbar, BorderLayout.CENTER);
		this.setSize(400, 70);
		this.setLocation(400, 300);
		this.setAlwaysOnTop(true);
		this.setVisible(false);
	}
	
	
	/**
	 * Show this progress bar with a title
	 * @param title
	 */
	public void showUI(String title){
		this.setTitle(title);
		this.setVisible(true);
	}
	
	public void hideUI(){
		this.setVisible(false);
	}
	
	
}
