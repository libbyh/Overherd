package nlp;

import java.util.HashSet;
import java.util.Iterator;

import registry.ComponentRegistry;

/**
 * HashSet class that saves instances of {@link TagWithTFIDF} from the entire document space.
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */
public class GlobalTagSet {
	private HashSet<TagWithTFIDF> globalTagSet;
	
	public GlobalTagSet(){
		ComponentRegistry.registeredGlobalTagSet=this;
		globalTagSet=new HashSet<TagWithTFIDF>();
	}
	
	public HashSet<TagWithTFIDF> getGlobalTagSet(){
		return this.globalTagSet;
	}
	
	public Iterator iterator(){
		return globalTagSet.iterator();
	}
	
	public boolean addTagWithTFIDF(TagWithTFIDF t){
		return this.globalTagSet.add(t);
		
	}
	
	public boolean removeTagWithTFIDF(TagWithTFIDF t){
		return this.globalTagSet.remove(t);
	}
	
	
}
