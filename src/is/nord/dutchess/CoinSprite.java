package is.nord.dutchess;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class CoinSprite extends GameObject
{
	private boolean isEnabled;
	private SceneUpdateHandler sceneUpdateHandler;
	
	// Let there be two constructors! In case we want to play around with the sprite's height and width.
	public CoinSprite(final float pX, final float pY, final TextureRegion pTextureRegion) 
	{
		super(pX, pY, pTextureRegion);
		this.isEnabled = true;
	}
	
	public CoinSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion, SceneUpdateHandler sceneUpdateHandler) 
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		this.isEnabled = true;
		this.sceneUpdateHandler = sceneUpdateHandler;
	}
	
	/*
	 * Usage:	c.onCollision();
	 * Pre:		c is a CoinSprite object
	 * Post:	the collision has been handled in a coinly-manner
	 */
	@Override
	public void onCollision()
	{
		sceneUpdateHandler.coinCollision(this);
	}
	
	public void disable()
	{
		this.isEnabled = false;
	}
	
	public boolean isEnabled()
	{
		return this.isEnabled;
		
	}
}
