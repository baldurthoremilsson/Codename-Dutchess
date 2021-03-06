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
	private Integer mCoins;
	
	public GameManager(Integer score, Integer level)
	{
		this.mScore = score;
		this.mLevel = level;
		this.mCoins = level;
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
	
	public Integer getCoins()
	{
		return mCoins;
	}
	
	public void decCoins()
	{
		mCoins--;
	}
	
	public void setCoins(int n)
	{
		mCoins = mCoins+n;
	}
	

}
