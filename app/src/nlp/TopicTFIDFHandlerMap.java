package nlp;

import java.util.*;

/**
 * Maps a topic and its TFIDF handler.  This is for when there are multiple discussion spaces to handle.  Currently not used.
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 */
public class TopicTFIDFHandlerMap {
	
	/**
	 * So that there's only one instance of this class
	 */
    private static TopicTFIDFHandlerMap instance=null;
    private static HashMap<Long,TFIDFHandler> topicTFIDFHandlerMap=new HashMap<Long,TFIDFHandler>();

    private TopicTFIDFHandlerMap(){

    }

    public static TopicTFIDFHandlerMap getInstance(){
        if(instance==null){
            instance=new TopicTFIDFHandlerMap();
        }
        return instance;
    }

    
    public static HashMap<Long,TFIDFHandler> getTopicTFIDFHandlerMap(){
        return topicTFIDFHandlerMap;
    }
}
