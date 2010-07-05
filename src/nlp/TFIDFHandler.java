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
 * Create one for each discussion space
 * @author kevin
 */
public class TFIDFHandler {
    
    private TokenizerFactory tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
    private MyTfIdfDistance tfIdfHandler=new MyTfIdfDistance(tokenizerFactory);
    private ArrayList<MyDocument> docs=new ArrayList<MyDocument>();	//keep track of what string docs are added
    private HashMap<String,Double>TFIDFScoreMap=new HashMap<String,Double>();
    

    public TFIDFHandler(){
        
        tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
        tokenizerFactory=new LowerCaseTokenizerFactory(tokenizerFactory);
        tokenizerFactory=new EnglishStopTokenizerFactory(tokenizerFactory);
    //    tokenizerFactory=new PorterStemmerTokenizerFactory(tokenizerFactory);
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
        
        
   //     System.out.println("Printing stopwords...");
   //     for(String t:ComponentRegistry.registeredStopWordSet){
   //     	System.out.println("stopword:"+t);
   //     }
    }

    /*
    public void addDoc(String doc){
    	//process each doc with tokenizers defined in the tokenizerFactory
        Tokenizer tz=tokenizerFactory.tokenizer(doc.toCharArray(), 0, doc.length());
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
        docs.add(newdoc);
        tfIdfHandler.handle(newdoc);
    }
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
        tfIdfHandler.handle(doc);
    }
    
    public MyTfIdfDistance getTfIdf(){
        
        return this.tfIdfHandler;
    }
    
    public ArrayList<MyDocument> getDocs(){
    	return docs;
    }

    /**
     * Calculate tf idf values for each term 
     * @author kevin
     */
    public void calculateTFIDFForAllWords(){
    	
    	//for each documents
        for(MyDocument doc:docs){
        //   System.out.println("doc----------------------------------");
        //    System.out.printf("\n   %18s    %8s\n","Term","Score\n");
            ArrayList<TagWithTFIDF> tags=new ArrayList<TagWithTFIDF>();
            ObjectToCounterMap<String> tf1=tfIdfHandler.termFrequencyVector(doc.getContent());
            for(Map.Entry<String,Counter>entry:tf1.entrySet()){
                String term=entry.getKey();
                
                if(term.length()==1){
                	continue;
                }
                                
                Counter counter=entry.getValue();
                double score=tfIdfHandler.tfIdf(term, counter);
                TagWithTFIDF tag=new TagWithTFIDF(term, score);
                if(!tags.contains(tag) && !ComponentRegistry.registeredStopWordSet.contains(tag.getTag().toLowerCase())){
                	tags.add(tag);
                //	System.out.println("tag:"+tag);
                    ComponentRegistry.registeredGlobalTagSet.addTagWithTFIDF(tag);
                //    doc.getTagSet().add(tag);
                }
                
            //    System.out.printf("\n %18s  %8s\n", term, score);
            }
            if(tags.size()>0){
            	
            	java.util.Collections.sort(tags, java.util.Collections.reverseOrder());
            //	System.out.println(tags);
            	doc.setTagSet(tags);
           // 	System.out.println(tags.get(1));
            }
        }
        
        /*
        System.out.println("check what tags are in the doc and save them");
        //now that tf-idf values are calculated for all terms, save them to my doc objects
        Iterator iter=ComponentRegistry.registeredGlobalTagSet.iterator();
        while(iter.hasNext()){
        	//for each tag, check whether a doc has the term
       
        	TagWithTFIDF tag=(TagWithTFIDF)iter.next();
        	String t=tag.getTag();
        	if(t.length()==1){
        		continue;
        	}
        	
        	if(ComponentRegistry.registeredStopWordSet.contains(t)){
        		continue;
        	}
        	System.out.println("checking for:"+tag.getTag());
        	for(MyDocument doc:docs){
	        	if(doc.getTokenizedContent().contains(tag.getTag())){
	        		System.out.println("Doc has the tag");
	        		doc.addTagWithTFIDF(tag);
	        	}
	        	
	        }
        	
        }
        */
       
    }



    
}
