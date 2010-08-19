package nlp;
/**
 * A wrapper tag class that can contain the tf*idf value.
 * Implements the {@link Comparable} interface so it can be sorted using the tf*idf values.
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */

public class TagWithTFIDF implements Comparable {
	private double score;
	private String word;
	
	public TagWithTFIDF(String word, double score){
		this.word=word;
		this.score=score;
	}
	
	public void setScore(double s){
		score=s;
	}
	
	public String getTag(){
		return this.word;
	}
	
	public double getScore(){
		return score;
	}
	
	/**
	 * Compare two objects with their score values.
	 */
	public int compareTo(Object other) throws ClassCastException {
		if(!(other instanceof TagWithTFIDF)){
			throw new ClassCastException("Not a TagWithTFIDF object.");
		}
		
		double otherScore=((TagWithTFIDF)other).getScore();
		if(score>otherScore){
			return 1;
		}else if(score<otherScore){
			return -1;
		}else{
			return 0;
		}
		
	}
	
	public String toString(){
		return "Word:"+word+", score:"+score;
	}
}
