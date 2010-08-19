package nlp;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Collections;
import com.aliasi.util.Counter;
import com.aliasi.util.ObjectToCounterMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import registry.ComponentRegistry;
/**
 * TFIDF handler class for posts in a discussion space.
 * 
 * First, it sets up the tokenizer factories for the text content.  They'll make every word lower case and throw out stop words.
 * I use an additional stop words list to supplement this as well.
 * 
 * A {@link MyDocument} object can be "handled" by this class by adding the object.  All documents need to be added
 * to this handler before the tf*idf values can be found for each word in each document.
 *
 * @author <a href="http://kevinnam.com">kevin nam</a>
 */


public class TFIDFHandler {
    
    private TokenizerFactory tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
    
    /**
     * {@link MyTfIdfDistance} class for calculating the cosign simularity value between two documents.
     */
    private MyTfIdfDistance tfIdfDistance=new MyTfIdfDistance(tokenizerFactory);
    
    /**
     * Keep track of the added {@link MyDocument} instances.
     */
    private ArrayList<MyDocument> docs=new ArrayList<MyDocument>();	
    
    /**
     * Keep track of words and their tf*idf values
     */
    private HashMap<String,Double>TFIDFScoreMap=new HashMap<String,Double>();
    

    /**
     * Set up various tokenizers and stopwords.
     */
    public TFIDFHandler(){
        
    	//create various tokenizerFactory
        tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
        tokenizerFactory=new LowerCaseTokenizerFactory(tokenizerFactory);
        tokenizerFactory=new EnglishStopTokenizerFactory(tokenizerFactory);
    //    tokenizerFactory=new PorterStemmerTokenizerFactory(tokenizerFactory);
        
        //read in stopwords to be used
        if(ComponentRegistry.registeredStopWordSet==null){
        	ComponentRegistry.registeredStopWordSet=new HashSet<String>();
        }
    	try{
    		BufferedReader input=new BufferedReader(new FileReader("data/stopwords.txt"));
    		String line=null;
    		while((line=input.readLine())!=null){
    			
    			ComponentRegistry.registeredStopWordSet.add(line);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        
   
    }

    
    /**
     * Add a {@link MyDocument} instance to the document handler after tokenizing the content.
     * 
     * @param doc Mydocument instance
     */
    public void addDoc(MyDocument doc){
    	//process each doc with tokenizers defined in the tokenizerFactory
    	String content=doc.getContent();
    	Tokenizer tz=tokenizerFactory.tokenizer(content.toCharArray(), 0, content.length());
        String token;
        StringBuffer buffer=new StringBuffer();
        
        //create a placeholder in the map of token to score 
        while((token=tz.nextToken())!=null){
            TFIDFScoreMap.put(token, 0.0);  //initially set all scores to 0.0
            buffer.append(token);
            buffer.append(" ");
        }
        String newdoc=buffer.toString();
        
        //pass the processed doc to handler
        doc.setTokenizedContent(newdoc);
        docs.add(doc);
        tfIdfDistance.handle(doc);
    }
    
    /**
     * Returns the MyTfIdfDistance used here.
     * @return MyTfIdfDistance instance
     */
    public MyTfIdfDistance getTfIdf(){
        
        return this.tfIdfDistance;
    }
    
    /**
     * Returns the document list
     * @return the list of documents added to the handler
     */
    public ArrayList<MyDocument> getDocs(){
    	return docs;
    }

    /**
     * Calculate the tf*idf value for each word and store it into its {@link MyDocument} object where it belongs to. 
     * 
     */
    public void calculateTFIDFForAllWords(){
    	
    	//for each documents added to this handler
        for(MyDocument doc:docs){
        //   System.out.println("doc----------------------------------");
        //    System.out.printf("\n   %18s    %8s\n","Term","Score\n");
        	
        	//create a tag list to store the tags for this document
            ArrayList<TagWithTFIDF> tags=new ArrayList<TagWithTFIDF>();
            
            ObjectToCounterMap<String> tf1=tfIdfDistance.termFrequencyVector(doc.getContent());
            for(Map.Entry<String,Counter>entry:tf1.entrySet()){
                String term=entry.getKey();
                
                if(term.length()==1){
                	continue;
                }
                                
                Counter counter=entry.getValue();
                double score=tfIdfDistance.tfIdf(term, counter);
                TagWithTFIDF tag=new TagWithTFIDF(term, score);
                if(!tags.contains(tag) && !ComponentRegistry.registeredStopWordSet.contains(tag.getTag().toLowerCase())){
                	tags.add(tag);
                //	System.out.println("tag:"+tag);
                    ComponentRegistry.registeredGlobalTagSet.addTagWithTFIDF(tag);
                //    doc.getTagSet().add(tag);
                }
                
            //    System.out.printf("\n %18s  %8s\n", term, score);
            }
            
            //if there are at least two tags, sort it.
            if(tags.size()>1){
            	
            	java.util.Collections.sort(tags, java.util.Collections.reverseOrder());
            //	System.out.println(tags);
            	doc.setTagSet(tags);
           // 	System.out.println(tags.get(1));
            }
        }
        
       
       
    }



    
}
