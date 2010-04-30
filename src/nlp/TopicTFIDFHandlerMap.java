package nlp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
/**
 * Maps a topic and its TFIDF handler
 * @author kevin
 */
public class TopicTFIDFHandlerMap {
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
