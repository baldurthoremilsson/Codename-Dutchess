package is.nord.dutchess;

import java.util.List;

public class MapPointList {
	
	private List<MapPoint> mMapPoints;
	
	public MapPointList()
	{
		
	}
	
	public List<MapPoint> getMapPoints()
	{
		return mMapPoints;
	}
	
	public void addPoint(MapPoint p)
	{
		if(mMapPoints.contains(p))
		{
			//already there so don't do anything
		}
		else {
		Integer px = p.getX();
		Integer py = p.getY();
		
		MapPoint tempP = new MapPoint(px+1,py+1);
		mMapPoints.add(tempP);
		tempP = new MapPoint(px-1,py-1);
		mMapPoints.add(tempP);
		}
	}

}
