package util;
/**
 * @author kevin
 * Custom OrPredicate which makes the retrieval of the underlying data structure easier
 */
import prefuse.data.expression.*;

import java.util.*;

public class MyOrPredicate extends OrPredicate {
	
	public ArrayList getPredicateList(){
		return m_clauses;
	}
}
