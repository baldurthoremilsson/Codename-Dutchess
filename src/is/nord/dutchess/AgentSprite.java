package is.nord.dutchess;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class AgentSprite extends GameObject
{
	// Let there be two constructors! In case we want to play around with the sprite's height and width.
	public AgentSprite(final float pX, final float pY, final TextureRegion pTextureRegion) 
	{
		super(pX, pY, pTextureRegion);
	}
	
	public AgentSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) 
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}
	
	/*
	 * Usage:	c.onCollision(activeScene);
	 * Pre:		c is a CoinSprite object, activeScene is of type scene.
	 * Post:	the collision has been handled in a coinly-manner, which affects activeScene
	 * UGLY:	Passing the active scene to this function is Fugly.
	 */
	public void onCollision(Scene activeScene)
	{
	}

	@Override
	public void onCollision() {
		// TODO Auto-generated method stub
		
	}
}
