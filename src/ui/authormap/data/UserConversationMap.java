package ui.authormap.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nlp.MyDocument;

/**
 * For the owning user of this class, this object creates a map to other users with conversation content.
 * <key, value>: key is the name of the other user (student) that his user had a conversation with.  Value contains a
 * MyDocument object that contains the conversation.
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */
public class UserConversationMap {
	private String userName;
	
	/**
	 * A conversationMap for conversations between the author (student) and other authors
	 * Key: the name of the other user
	 * Value: list of MyDocuments that have conversation contents
	 * 
	 * *Note: the other user will also have his own map that lists this user, so the content is listed twice.
	 */
	
	private HashMap<String, HashSet<MyDocument>> conversationMap=new HashMap<String,HashSet<MyDocument>>();
//	private HashSet<String> highTFIDFWords=new HashSet<String>();
	
	/**
	 * Concatenate all the posts in the map - used to check if a word is found
	 */
	private StringBuffer entireConversationBuffer=new StringBuffer();	
	
	public UserConversationMap(String userName){
		this.userName=userName;
	}
	
	
	public HashMap<String,HashSet<MyDocument>> getConversationMap(){
		return this.conversationMap;
	}
	
	/**
	 * Add the other user with conversation content
	 * 
	 * @param otherUser the student with whom the owning student had a conversation
	 * @param conversation  MyDocument object that contains the conversation
	 */
	public void addConversationForUser(String otherUser,MyDocument conversation){
		
		//if other user first encountered, create an entry in the map
		if(!conversationMap.containsKey(otherUser)){
			HashSet<MyDocument> set=new HashSet<MyDocument>();
			set.add(conversation);
			conversationMap.put(otherUser, set);
		}else{
			//if other user already in the map, just update the conversation
			HashSet<MyDocument> set=conversationMap.get(otherUser);
			set.add(conversation);
			conversationMap.put(otherUser, set);
		}
		
	}
	
	/**
	 * Counts and returns the number of students this student talked to.
	 * Just a number of keys in the conversation map
	 * @return the number of the neighbors
	 */
	public int countUsersTalkedTo(){
		return conversationMap.keySet().size();
	}
	
	/**
	 * Counts and returns the number of conversation this student had with otherUser.
	 * 
	 * @param otherUser
	 * @return the number of conversation
	 */
	public int countConversationInstance(String otherUser){
		//if otherUser not found, then no conversation mapped so far
		if(!conversationMap.containsKey(otherUser)){
			return 0;
		}else{
			HashSet<MyDocument> set=conversationMap.get(otherUser);
			return set.size();
		}
	}
	
	/**
	 * Counts and returns the number of conversations this student had with all others.
	 * @return the number of the conversations
	 */
	public int countConversationInstances(){
		int count=0;
		//get all the other users
		Set<String>set=conversationMap.keySet();
		
		//for each user count the number of conversation
		for(String otherUser:set){
			count+=conversationMap.get(otherUser).size();
		}
		
		return count;
	}
	
	public StringBuffer getEntireConversationBuffer(){
		return this.entireConversationBuffer;
	}
}
