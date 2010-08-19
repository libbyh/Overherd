package nlp;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A wrapper class for storing user name, content, tokenized content, a list of tags with tf-idf values
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */
public class MyDocument {
	/**
	 * Student's name
	 */
	private String userName;
	
	/**
	 * The content of a conversation (reply)
	 */
	private String content;
	
	/**
	 * Tokenized content filtered through tokenizer factories
	 */
	private String tokenizedContent;
	
	/**
	 * A list of tags of type {@link TagWithTFIDF} found from the document.  Each tag has a word and tf*idf value.
	 */
	ArrayList<TagWithTFIDF> tags;
	
	public MyDocument(String userName,String content, ArrayList<TagWithTFIDF> tags){
		setup(userName,content,tags);
	}
	
	public MyDocument(String userName,String content){
		setup(userName,content,new ArrayList<TagWithTFIDF>());
	}
	
	public MyDocument(String userName){
		setup(userName,"",new ArrayList<TagWithTFIDF>());
	}
	
	public void setup(String userName, String content, ArrayList<TagWithTFIDF> tags){
		this.userName=userName;
		this.content=content;
		this.tags=new ArrayList<TagWithTFIDF>();
	}
	
	
	public void setUserName(String s){
		this.userName=s;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setContent(String s){
		this.content=s;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public ArrayList<TagWithTFIDF> getTagSet(){
		return this.tags;
	}
	
	public void setTagSet(ArrayList<TagWithTFIDF> set){
		this.tags=set;
	}
	
	public void addTagWithTFIDF(TagWithTFIDF tag){
		this.tags.add(tag);
	}
	
	public void removeTagWithTFIDF(TagWithTFIDF tag){
		this.tags.remove(tag);
		
	}
	
	public void setTokenizedContent(String s){
		this.tokenizedContent=s;
	}
	
	public String getTokenizedContent(){
		return this.tokenizedContent;
	}
	
	
}
