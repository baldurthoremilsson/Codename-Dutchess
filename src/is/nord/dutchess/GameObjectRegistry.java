package is.nord.dutchess;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * 
 * @author gunnarr
 *
 *	This class holds information regarding each game object which is required for the object's construction. 
 */
public class GameObjectRegistry {
	private PhysicsWorld physicsworld;
	
	private TextureRegion agentTextureRegion;
	private TextureRegion wallTextureRegion;
	private TextureRegion coinTextureRegion;
	
	private Body agentBody;
	
	/*
	 * Usage:	GameObjectRegistry gor = new GameObjectRegistry(physicsworld);
	 * Pre:		physicsworld is of type PhysicsWorld and dictates the physics for the game objects
	 * Post:	gor is a new GameObjectRegistry affiliated with physicsworld. 
	 */
	public GameObjectRegistry(PhysicsWorld physicsEngine)
	{
		this.physicsworld = physicsEngine;
	}
	
	/* Setters */
	public void setAgentTextureRegion(TextureRegion agentTextRegion)
	{
		this.agentTextureRegion = agentTextRegion;
	}
	
	public void setWallTextureRegion(TextureRegion wallTextRegion)
	{
		this.wallTextureRegion = wallTextRegion;
	}
	
	public void setCoinTextureRegion(TextureRegion coinTextRegion)
	{
		this.coinTextureRegion = coinTextRegion;
	}
	
	public void setAgentBody(Body agentBody)
	{
		this.agentBody = agentBody;
	}
	
	/* Getters */
	public TextureRegion getAgentTextureRegion()
	{
		return this.agentTextureRegion;
	}
	
	public TextureRegion getWallTextureRegion()
	{
		return this.wallTextureRegion;
	}
	
	public TextureRegion getCoinTextureRegion()
	{
		return this.coinTextureRegion;
	}
	
	public Body getAgentBody()
	{
		return this.agentBody;
		
	}
	
	public PhysicsWorld getPhysicsWorld()
	{
		return this.physicsworld;
	}

}
