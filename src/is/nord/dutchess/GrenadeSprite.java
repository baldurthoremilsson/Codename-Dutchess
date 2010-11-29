package is.nord.dutchess;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GrenadeSprite extends GameObject {
	public GrenadeSprite(final float pX, final float pY, final TextureRegion pTextureRegion) 
	{
		super(pX, pY, pTextureRegion);
	}
	
	public GrenadeSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) 
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion);

	}

	@Override
	public void onCollision() {
		// TODO Auto-generated method stub
		
	}
}
