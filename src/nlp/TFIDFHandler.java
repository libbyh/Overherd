package nlp;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
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
import java.util.Map;
import java.util.Set;
/**
 * TFIDF handler class for posts in a discussion space.
 * Create one for each discussion space
 * @author kevin
 */
public class TFIDFHandler {
    
    private TokenizerFactory tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
    private MyTfIdfDistance tfIdfHandler=new MyTfIdfDistance(tokenizerFactory);
    private ArrayList<String> docs=new ArrayList<String>();
    private HashMap<String,Double>TFIDFScoreMap=new HashMap<String,Double>();
    

    public TFIDFHandler(){
        
        tokenizerFactory=IndoEuropeanTokenizerFactory.INSTANCE;
        tokenizerFactory=new LowerCaseTokenizerFactory(tokenizerFactory);
        tokenizerFactory=new EnglishStopTokenizerFactory(tokenizerFactory);
    //    tokenizerFactory=new PorterStemmerTokenizerFactory(tokenizerFactory);
    }

    public void addDoc(String doc){
        Tokenizer tz=tokenizerFactory.tokenizer(doc.toCharArray(), 0, doc.length());
        String token;
        StringBuffer buffer=new StringBuffer();
        while((token=tz.nextToken())!=null){
            TFIDFScoreMap.put(token, 0.0);  //initially set all scores to 0.0
            buffer.append(token);
            buffer.append(" ");
        }
        String newdoc=buffer.toString();
        docs.add(newdoc);
        tfIdfHandler.handle(newdoc);
    }

    public MyTfIdfDistance getTfIdf(){
        
        return this.tfIdfHandler;
    }
    
    public ArrayList<String> getDocs(){
    	return docs;
    }

    public void calculateTFIDFForAllWords(){
        for(String doc:docs){
           System.out.println("doc----------------------------------");
        //    System.out.printf("\n   %18s    %8s\n","Term","Score\n");
            ArrayList<TagWithTFIDF> tags=new ArrayList<TagWithTFIDF>();
            ObjectToCounterMap<String> tf1=tfIdfHandler.termFrequencyVector(doc);
            for(Map.Entry<String,Counter>entry:tf1.entrySet()){
                String term=entry.getKey();
                Counter counter=entry.getValue();
                double score=tfIdfHandler.tfIdf(term, counter);
                TagWithTFIDF tag=new TagWithTFIDF(term, score);
                tags.add(tag);
                
            //    System.out.printf("\n %18s  %8s\n", term, score);
            }
            Arrays.sort(tags.toArray(), java.util.Collections.reverseOrder());
            if(tags.size()>0){
            	System.out.println(tags);
           // 	System.out.println(tags.get(1));
            }
        }
        Set<String>keys=TFIDFScoreMap.keySet();
       
       
    }



    
}
