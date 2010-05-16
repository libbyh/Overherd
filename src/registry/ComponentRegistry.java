package registry;

import java.util.HashMap;

import nlp.*;
import ui.*;

public class ComponentRegistry {
	public static MainUI registeredMainUI;
	public static TreeMap registeredTreeMap;
	public static AuthorTopicViz registeredAuthorTopicViz;
	public static AuthorTopicVizUI registeredAuthorTopicVizUI;
	
	//NLP
	public static TopicTFIDFHandlerMap registeredTopicTFIDFHandler;
	public static TFIDFHandler registeredTFIDFHandler;
	public static HashMap<String,UserConversationMap> registeredUserMatrix;
	
	
	//do not instantiate
	private ComponentRegistry(){
		
	}
}
