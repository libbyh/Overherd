package ui.authormap.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nlp.MyDocument;

/**
 * For this user, has a map to other users with conversation content
 * @author kevin
 *
 */
public class UserConversationMap {
	private String userName;
	
	/**
	 * conversationMap for conversation between the user and other users
	 * Key: the name of other user
	 * Value: list of string text
	 * 
	 * *Note: the other user will also have his own map that lists this user, so the content is listed twice.
	 */
	
	
	private HashMap<String, HashSet<MyDocument>> conversationMap=new HashMap<String,HashSet<MyDocument>>();
	private HashSet<String> highTFIDFWords=new HashSet<String>();
	private StringBuffer entireConversationBuffer=new StringBuffer();	//concatenate all the posts in the map - used to check if a word is found
	
	public UserConversationMap(String userName){
		this.userName=userName;
	}
	
	
	public HashMap<String,HashSet<MyDocument>> getConversationMap(){
		return this.conversationMap;
	}
	
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
	 * @return
	 */
	public int countUsersTalkedTo(){
		return conversationMap.keySet().size();
	}
	
	/**
	 * Counts and returns the number of conversation this student had with otherUser.
	 * 
	 * @param otherUser
	 * @return
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
	 * Counts and returns the number of conversation this student had with all others.
	 * @return
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
