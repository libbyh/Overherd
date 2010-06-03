package nlp;


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
