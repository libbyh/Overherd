package util;

import prefuse.data.expression.*;

import java.util.*;
/**
 * @author <a href="http://kevinnam.com">kevin nam</a>
 * Custom OrPredicate which makes the retrieval of the underlying data structure easier
 */
public class MyOrPredicate extends OrPredicate {
	
	public ArrayList getPredicateList(){
		return m_clauses;
	}
}
