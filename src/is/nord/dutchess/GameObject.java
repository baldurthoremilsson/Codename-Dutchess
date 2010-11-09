package is.nord.dutchess;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * 
 * @author gunnarr
 * This interface is meant to define the mutual methods each game object needs to implement.
 *
 */

public interface GameObject {
	/**
	 * Usage:	shapeObject.onCollision();
	 * Pre:		there has been a collision between two shapeObjects
	 * Post: 	the collision has been handled in a proper manner depending on shapeObject.
	 */
	public void onCollision();
	
	/**
	 * Usage:	shapeObject.createBody();
	 * Pre:		shapeObject has been instansiated		
	 * Post:	a Body has been created according to shapeObject, and assigned to it.
	 */
	public void createBody(PhysicsWorld pPhysicsWorld);
	

}
