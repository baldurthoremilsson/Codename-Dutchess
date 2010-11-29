package is.nord.dutchess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.MusicManager;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.ColoredTextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.font.Font;

import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @Hopureitt
 *
 * This class is constructs and manages scenes both for game play and menus. A part of that registering the scene to an 
 * update handler and implementing it's onUpdate method.  
 * 
 * FIXME: Coins don't always spawn in the frame. 
 *
 */

public class SceneFactory {

	// Local variables
	private Scene activeScene;
	private Camera camera;
	private BoundCamera bCamera;
	private Font font;
	private GameObjectRegistry gor;
	private AudioManager am;
	private CodenameDutchess mcd;
	private PhysicsWorld mPhysicsWorld;
	private ChangeableText mScoreText;
	private ChangeableText mTime;
	AgentSprite agent;
	Body agentBody;
	private List<CoinSprite> coins = new ArrayList<CoinSprite>();
	private List<WallSprite> walls = new ArrayList<WallSprite>();
	private List<GameObject> mSceneObjects = new ArrayList<GameObject>();
	
	private static String COINS = "coins concurrency exception";

	
	/*
	 * Usage:	SceneFactory sf = new SceneFactory(camera, font, scene);
	 * Pre:		camera is of type Camera, font of type Font, and scene of type Scene, and all three have been set up
	 * Post:	sf is a SceenFactory object based on the parameters
	 */
	public SceneFactory(BoundCamera camera, Font font, Scene scene, GameObjectRegistry gor, final AudioManager am, PhysicsWorld physicsWorld)
	{
		this.bCamera = camera;
		this.bCamera.setBoundsEnabled(true);
		this.font = font;
		this.activeScene = scene;
		this.gor = gor;
		this.am = am;
		this.mPhysicsWorld = physicsWorld;
		
		//Play a random song
		Random r = new Random();
		this.am.getPlayList().get(r.nextInt(this.am.getPlayList().size()-1)).play();
	}
	
	public Scene getActiveScene()
	{
		return activeScene;
	}
	
	
	public List<GameObject> getSceneObjects()
	{
		return this.mSceneObjects;
	}
	
	public ChangeableText getScoreText()
	{
		return mScoreText;
	}
	
	public Scene setScoreText(String score)
	{
		mScoreText.setText(score);
		return activeScene;
	}
	
	public Body getAgentBody()
	{
		return agentBody;
	}
	
	/**
	 * 
	 * @return agentSprite
	 */
	public Sprite getAgentSprite()
	{
		return agent;
	}
	
	/**
	 * 
	 * @return ChangeableText time
	 */
	public ChangeableText getTime()
	{
		return mTime;
	}
	
	/**
	 * 
	 * @param String time
	 */
	public Scene setTime(String time)
	{
		mTime.setText(time);
		return activeScene;
	}

	public Scene createStartScene(IOnMenuItemClickListener listener) {
		MenuScene menuScene = new MenuScene(this.bCamera);
		menuScene.addMenuItem(new ColoredTextMenuItem(CodenameDutchess.MENU_MAIN_NEWGAME, this.font, "NEW GAME", 1.0f,0.7f,0.7f, 0.7f,0.7f,0.7f));
		menuScene.addMenuItem(new ColoredTextMenuItem(CodenameDutchess.MENU_MAIN_QUIT, this.font, "QUIT", 1.0f,0.7f,0.7f, 0.7f,0.7f,0.7f));
		//menuScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		menuScene.buildAnimations();
		menuScene.setOnMenuItemClickListener(listener);
		activeScene = menuScene;
		return menuScene;
	}
	
	public Scene createPauseScene(IOnMenuItemClickListener listener) {
		MenuScene menuScene = new MenuScene(this.bCamera);
		menuScene.addMenuItem(new ColoredTextMenuItem(CodenameDutchess.MENU_PAUSE_CONTINUE, this.font, "CONTINUE", 1.0f,0.7f,0.7f, 0.7f,0.7f,0.7f));
		menuScene.addMenuItem(new ColoredTextMenuItem(CodenameDutchess.MENU_MAIN_QUIT, this.font, "QUIT", 1.0f,0.7f,0.7f, 0.7f,0.7f,0.7f));
		//menuScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		menuScene.buildAnimations();
		menuScene.setOnMenuItemClickListener(listener);
		menuScene.centerShapeInCamera(agent);
		activeScene = menuScene;
		return menuScene;
	}
	
	
	/*
	 * Usage:	sf.createDemoScene();
	 * Pre:		sf is a SceneFactory object
	 * Post:	the active scene is a demo scene, similar to the one from earlier vesions of the game.
	 * 			Proof of concept but not a final or definite version of how game levels are constructed
	 */
	public Scene createLevelScene(int level)
	{
		Scene scene = new Scene(1);
		//scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		scene.setBackground(this.gor.getRepeatingBackground());
		scene.registerUpdateHandler(this.mPhysicsWorld);
		
		/* make the frame */
		initBorders(scene, this.bCamera, mPhysicsWorld);
		agent = new AgentSprite(0, 0, this.gor.getAgentTextureRegion());
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		agentBody = PhysicsFactory.createBoxBody(mPhysicsWorld, agent, BodyType.DynamicBody, carFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(agent, agentBody, true, false, true, false));
		scene.getTopLayer().addEntity(agent);
		
		//make bounded camera chase the agent
		this.bCamera.setChaseShape(agent);
		

		
		this.mScoreText = new ChangeableText(5, 5, this.font, "0", 2);
		this.mScoreText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreText.setAlpha(0.5f);
		HUD hud = new HUD();
		hud.getTopLayer().addEntity(this.mScoreText);
		
		this.mTime = new ChangeableText(480-60, 5, this.font, "0",2);
		this.mTime.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mTime.setAlpha(0.5f);
		this.mTime.setScale(0.9f);
		hud.getTopLayer().addEntity(this.mTime);
		//hud.centerShapeInCamera(agent);
		this.bCamera.setHUD(hud);
		
		// Create the coins, must be randomized better
		CoinSprite coin;
		List<CoinSprite> xCoins = new ArrayList<CoinSprite>();
		for(int i=0; i!=level*5; i++)
		{
			coin = new CoinSprite(SceneFactory.randomNumber(5, 480*2-20), 
					SceneFactory.randomNumber(10, 320*2-20),
					20,
					20,
					this.gor.getCoinTextureRegion());
			xCoins.add(coin);
			mSceneObjects.add(coin);
			
		}
		
		// Spawn the coins
		for( CoinSprite coinsprite : xCoins)
		{
			scene.getTopLayer().addEntity(coinsprite);			
		}
		
		GrenadeSprite gren = new GrenadeSprite(100, 100, this.gor.getCoinTextureRegion());
		scene.getTopLayer().addEntity(gren);
		
		GameObject wallie;
		Random rand = new Random();
		for(int i=0; i!= (20+level*2); i++)
		{
			wallie = new WallSprite(SceneFactory.randomNumber(10, 480*2-20), 
					SceneFactory.randomNumber(10, 320*2-20), 
					this.gor.getWallTextureRegion(), 
					mPhysicsWorld);
			//wallie.addShapeModifier(new RotationModifier(0.001f, 90, rand.nextBoolean() ? 90 : 0));
			/*if(rand.nextBoolean())
			{
				wallie.addShapeModifier(new RotationModifier(0.01f, 0, 90));
			}*/
			scene.getTopLayer().addEntity(wallie);
			mSceneObjects.add(wallie);
		}
		activeScene = scene;
		return scene;
	}
	
	/*
	 * Usage:	this.initBorders();
	 * Pre:		this is a sf object
	 * Post:	The game level's borders have been initialized to form a frame
	 */
	private void initBorders(Scene scene, Camera camera, PhysicsWorld physicsWorld) {
		final Shape bottomOuter = new Rectangle(0, camera.getHeight()*2 - 2, camera.getWidth()*2, 2);
		final Shape topOuter = new Rectangle(0, 0, camera.getWidth()*2, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, camera.getHeight()*2);
		final Shape rightOuter = new Rectangle(camera.getWidth()*2 - 2, 0, 2, camera.getHeight()*2);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(physicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);
		
		final ILayer bottomLayer = scene.getTopLayer();
		bottomLayer.addEntity(bottomOuter);
		bottomLayer.addEntity(topOuter);
		bottomLayer.addEntity(leftOuter);
		bottomLayer.addEntity(rightOuter);	
	}
	
	public static int randomNumber(int min, int max) { return min + (new Random()).nextInt(max-min); }


}
