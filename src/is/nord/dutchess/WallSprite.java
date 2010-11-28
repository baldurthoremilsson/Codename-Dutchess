package is.nord.dutchess;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

/**
 * This class extends the Sprite class, which includes/inherits all methods we need to "spawn" a shape, scale it, rotate. etc.
 * We also create a body of type Body which is associated with our sprite, and then used with the physics engine.
 * @author gunnarr
 *
 */
public class WallSprite extends GameObject
{		
		
	/**
	 * @param pX is of type float and is the X-coordinate where we want to place our object
	 * @param pY is of type float and is the Y-coordinate where we want to place our object
	 * @param pTextureRegion is a TextureRegion object which has texture already appended to it. (I think this is required).   
	 */
	public WallSprite(final float pX, final float pY, final TextureRegion pTextureRegion, final PhysicsWorld pPhysicsWorld) 
	{
		super(pX, pY, pTextureRegion);
		createBody(pPhysicsWorld);
	}
	
	@Override
	public void onCollision()
	{
		// Well, the wall isn't supposed to do anything on collision..for now.
	}
	
	/**
	 * @return A wall body, of type Body which size is depending on the pTextureRegion's size.  
	 */
	public void createBody(PhysicsWorld pPhysicsWorld) 
	{
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(pPhysicsWorld, this, BodyType.StaticBody, wallFixtureDef);
	}
	
	
}
