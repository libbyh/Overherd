package registry;
/**
 * A registry class for various values.
 *  
 *  @author <a href="http://kevinnam.com">kevin nam</a>
 */
import java.awt.Color;
import java.awt.Font;

import prefuse.util.ColorLib;

import util.*;

public class ValueRegistry {
	/**
	 * To keep track of which "mode" the author-topic view's content viewer currently is.
	 * CONVERSATION_TEXT_TAB_VIEW shows the full text with keywords highlighted.
	 */
	public static int CONVERSATION_TEXT_TAB_VIEW=2;
	
	public static int CONVERSATION_SENTENCE_TAB_VIEW=1;	//not used currently
	
	/**
	 * To keep track of which "mode" the author-topic view's content viewer currently is.
	 * CONVERSATION_KEYWORD_TAB_VIEW shows the keywords found by tf-idf values.
	 */
	public static int CONVERSATION_KEYWORD_TAB_VIEW=0;
	
	/**
	 * Make it true if the lengthy content analysis is done, so it'll only process once.
	 */
	public static boolean CONTENT_ANALYZED=false;
	
	
	/**
	 * The percentage of the top keywords (found by tf*idf) in each document to be shown in the keyword view in AuthorTopicVizUI.
	 * 
	 */
	public static double KEYWORD_PERCENTAGE_TO_SHOW=0.3;
	
	/**
	 * Color to be used for displaying keywords in {@link ui.authormap.AuthorTopicVizUI}
	 */
	public static Color KEYWORD_COLOR=new Color(51,51,255);
	
	/**
	 * Font to be used for displaying keywords in AuthorTopicVizUI
	 */
	public static Font TEXT_FONT=new Font("Tahoma", Font.PLAIN, 13);
	
	
	/**
	 * Default word highlighter color in the text view.
	 * Use an ahpha value so more occurences will be indicated by the color intensity.
	 */
	public static MyKeywordsHighlighter DEFAULT_KEYWORD_HIGHLIGHTER=
		new MyKeywordsHighlighter(new Color(51,51,255,50));
	
	/**
	 * Prevent instantiation.
	 */
	private ValueRegistry(){
		
	}
}
