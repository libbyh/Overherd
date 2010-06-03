package nlp;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * MyDocument class for storing user name, content, a list of tags with tf-idf values
 * 
 * @author kevin
 *
 */
public class MyDocument {
	
	private String userName;
	private String content;
	private String tokenizedContent;
	HashSet<TagWithTFIDF> tags;
	
	public MyDocument(String userName,String content, HashSet<TagWithTFIDF> tags){
		setup(userName,content,tags);
	}
	
	public MyDocument(String userName,String content){
		setup(userName,content,new HashSet<TagWithTFIDF>());
	}
	
	public MyDocument(String userName){
		setup(userName,"",new HashSet<TagWithTFIDF>());
	}
	
	public void setup(String userName, String content, HashSet<TagWithTFIDF> tags){
		this.userName=userName;
		this.content=content;
		this.tags=new HashSet<TagWithTFIDF>();
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
	
	public HashSet<TagWithTFIDF> getTagSet(){
		return this.tags;
	}
	
	public void setTagSet(HashSet<TagWithTFIDF> set){
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
