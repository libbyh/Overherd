package registry;

import java.awt.Color;
import java.awt.Font;

import prefuse.util.ColorLib;

import util.*;

public class ValueRegistry {
	public static int CONVERSATION_TEXT_TAB_VIEW=2;
	public static int CONVERSATION_SENTENCE_TAB_VIEW=1;
	public static int CONVERSATION_KEYWORD_TAB_VIEW=0;
	
	public static boolean CONTENT_ANALYZED=false;
	
	//the percentage of the entire key words in a text to be shown in the keyword view in AuthorTopicVizUI
	public static double KEYWORD_PERCENTAGE_TO_SHOW=0.3;
	
	//color to be used for displaying keywords in AUthorTopicVizUI
	public static Color KEYWORD_COLOR=new Color(51,51,255);
	public static Font TEXT_FONT=new Font("Tahoma", Font.PLAIN, 13);
	
	public static MyKeywordsHighlighter DEFAULT_KEYWORD_HIGHLIGHTER=
		new MyKeywordsHighlighter(new Color(51,51,255,50));
	
	private ValueRegistry(){
		
	}
}
