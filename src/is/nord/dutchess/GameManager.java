package is.nord.dutchess;

//GameManager

/**
 * @author lettfeti
 * Handles scores 
 * Handles levels
 */
public class GameManager {
	private Integer mScore;
	private Integer mLevel;
	
	public GameManager(Integer score, Integer level)
	{
		this.mScore = score;
		this.mLevel = level;
	}
	
	public Integer getmScore() {
		return mScore;
	}
	public void setmScore(Integer mScore) {
		this.mScore = mScore;
	}
	
	public void incmScore() {
		this.mScore++;
	}
	
	public Integer getmLevel() {
		return mLevel;
	}
	public void setmLevel(Integer mLevel) {
		this.mLevel = mLevel;
	}
	
	

}
