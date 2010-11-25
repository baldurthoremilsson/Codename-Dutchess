package is.nord.dutchess;


/**
 * 
 * @author lettfeti
 * Class that helps us determine where to put objects into a map
 */
public class MapPoint {
	
	private Integer x;
	private Integer y;
	
	public MapPoint(Integer x, Integer y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Integer getX()
	{
		return this.x;
	}
	
	public Integer getY()
	{
		return this.y;
	}

}
