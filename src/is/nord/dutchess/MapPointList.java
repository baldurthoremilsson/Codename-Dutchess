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
		
		//populate the cells around the wall/coin/trap
		for(int i = 0; i!=3; i++){
			
			MapPoint tempPp = new MapPoint(px+(i+1),py+(i+1));
			MapPoint tempPm = new MapPoint(px-(i+1),py+(i-1));
			mMapPoints.add(tempPp);
			mMapPoints.add(tempPp);
		}

		}
	}

}
