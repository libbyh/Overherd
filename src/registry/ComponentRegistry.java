
package registry;



import java.util.HashMap;
import java.util.HashSet;

import nlp.*;

import ui.authormap.AuthorTopicVizUI;
import ui.authormap.data.UserConversationMap;
import ui.authormap.viz.AuthorTopicViz;
import ui.treemap.MainUI;
import ui.treemap.viz.TreeMap;
import util.ProgressBarFrame;

/**
 * A registry class for various components in Overherd.
 * When a class is created it links itself to the corresponding registry object so it can be accessed from any class.
 *  
 *  @author <a href="http://kevinnam.com">kevin nam</a>
 */
public class ComponentRegistry {
	
	//various visual component and UIs
	/**
	 * The main UI that's running.
	 */
	public static MainUI registeredMainUI;
	
	/**
	 * The tree map of the discussion space shown within the main UI
	 */
	public static TreeMap registeredTreeMap;
	
	/**
	 * The AuthorTopicViz object that shows conversations among the students
	 */
	public static AuthorTopicViz registeredAuthorTopicViz;
	
	/**
	 * The UI object that has the AuthorTopicViz.
	 */
	public static AuthorTopicVizUI registeredAuthorTopicVizUI;
	
	//NLP related components
	
	/**
	 * Currently not used.  For when there are more than one discussion space in use.
	 */
	public static TopicTFIDFHandlerMap registeredTopicTFIDFHandler;
	
	/**
	 * The TFIDFHanlder being used.
	 */
	public static TFIDFHandler registeredTFIDFHandler;
	
	/**
	 * Maps user name and his/her UserConversationMap object.  UserConversationMap objects maps the owing user (student) with 
	 * all the other users (students)with the conversations. 
	 */
	public static HashMap<String,UserConversationMap> registeredUserMatrix;
	
	/**
	 * The GlobalTagSet being used.
	 */
	public static GlobalTagSet registeredGlobalTagSet;
	
	/**
	 * The set containing the stop words to filter out not important words.
	 */
	public static HashSet<String>registeredStopWordSet;
	
	/**
	 * A progress bar that shows up when loading the AuthorTopicViz
	 */
	public static ProgressBarFrame registeredProgressBarFrame=new ProgressBarFrame("Please wait...");
	
	//do not allow instantiation
	private ComponentRegistry(){
		
	}
}
