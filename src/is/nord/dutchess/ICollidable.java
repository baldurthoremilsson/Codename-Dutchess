package is.nord.dutchess;

import org.anddev.andengine.entity.shape.IShape;

public interface ICollidable {
	/**
	 * Usage:	shapeObject.onCollision();
	 * Pre:		there has been a collision between two shapeObjects
	 * Post: 	the collision has been handled in a proper manner depending on shapeObject.
	 */
	public void onCollision();
	
	public boolean collidesWith(final IShape pOtherShape);
}
