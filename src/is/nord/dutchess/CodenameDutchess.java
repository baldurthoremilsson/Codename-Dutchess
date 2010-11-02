package is.nord.dutchess;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;


import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.io.IOException;
import java.util.Random;
/**
 * @author Hopur eitt
 */
public class CodenameDutchess extends BaseGameActivity implements IAccelerometerListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;



	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private Scene scene;
	
	private Music mMusic;

	private PhysicsWorld mPhysicsWorld;

	/* Controls textures
	private Texture mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	*/
	// Texture and region for the agent
	private Texture mAgentTexture;
	private TextureRegion mAgentTextureRegion;
	private Sprite mAgent;
	private Body mAgentBody;
	
	// Texture and region for the rewards to be collected
	private Texture mRewTexture;
	private TextureRegion mRewTextureRegion;
	
	// Traps, lines and the endgoal, rewards
	final Rectangle[] lines = new Rectangle[10];
	private Sprite[] rewards = new Sprite[6];
	private Rectangle endRect;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ==========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/");

		this.mAgentTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mAgentTextureRegion = TextureRegionFactory.createFromAsset(this.mAgentTexture, this, "pokese6.png", 0, 0);
		
		this.mRewTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRewTextureRegion = TextureRegionFactory.createFromAsset(this.mRewTexture, this, "coin.png", 0, 0);
		/*
		this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		 */
		this.mEngine.getTextureManager().loadTextures(/*this.mOnScreenControlTexture, */this.mAgentTexture, this.mRewTexture);
		
		MusicFactory.setAssetBasePath("mfx/");
		
			try {
				this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "Acid_techno.ogg");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.mMusic.setLooping(true);
		
		
		// Accelero-support
		this.enableAccelerometerSensor(this);

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(4);
		scene.setBackground(new ColorBackground(0, 0, 0));

		//this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		// 
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		this.initBorders(scene);
		this.initAgent(scene);
		this.initRandomLevel(scene);
		//this.initOnScreenControls(scene);
		

		scene.registerUpdateHandler(this.mPhysicsWorld);
		this.mMusic.play();
				
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}
	
	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
        this.mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getY(), pAccelerometerData.getX()));		
	}

	// ===========================================================
	// Methods
	// ===========================================================
/*
	private void initOnScreenControls(final Scene pScene) {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new IAnalogOnScreenControlListener() {
			private Vector2 mVelocityTemp = new Vector2();

			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				this.mVelocityTemp.set(pValueX * 5, pValueY * 5);
				
				final Body tankBody = CodenameDutchess.this.mAgentBody;
				tankBody.setLinearVelocity(this.mVelocityTemp);
				
				final float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
				tankBody.setTransform(tankBody.getWorldCenter(), rotationInRad);
				
				CodenameDutchess.this.mAgent.setRotation(MathUtils.radToDeg(rotationInRad));
			}

			@Override
			public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(0.75f);
		analogOnScreenControl.getControlKnob().setScale(0.75f);
		analogOnScreenControl.refreshControlKnobPosition();
		
		pScene.setChildScene(analogOnScreenControl);
	}
*/
	private void initAgent(final Scene pScene) {
		this.mAgent = new Sprite(0, 0, this.mAgentTextureRegion);
		this.mAgent.setScale(0.45f);
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.mAgentBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.mAgent, BodyType.DynamicBody, carFixtureDef);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mAgent, this.mAgentBody, true, false, true, false));
		pScene.getTopLayer().addEntity(this.mAgent);
	}
	
	
	private void initBorders(final Scene pScene) {
		final Shape bottomOuter = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape topOuter = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape rightOuter = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);
		
		final ILayer bottomLayer = pScene.getTopLayer();
		bottomLayer.addEntity(bottomOuter);
		bottomLayer.addEntity(topOuter);
		bottomLayer.addEntity(leftOuter);
		bottomLayer.addEntity(rightOuter);
	}

	private void initRandomLevel(final Scene pScene) {
		Random gaur = new Random();
		
		lines[0] = new Rectangle( 0, 0.15f*this.CAMERA_HEIGHT,0.8f*this.CAMERA_WIDTH, 3);
		lines[1] = new Rectangle( 0.5f*this.CAMERA_WIDTH, 0.3f*this.CAMERA_HEIGHT, 0.5f*this.CAMERA_WIDTH, 3);
		lines[2] = new Rectangle( 0.3f * this.CAMERA_WIDTH, 0.15f*this.CAMERA_HEIGHT, 3, 0.5f*this.CAMERA_HEIGHT);
		lines[3] = new Rectangle( 0, 0.4f*this.CAMERA_HEIGHT, 0.16f*this.CAMERA_WIDTH, 3 );
		lines[4] = new Rectangle( 0.4f*this.CAMERA_WIDTH, 0.5f*this.CAMERA_HEIGHT, 3,this.CAMERA_HEIGHT );
		lines[5] = new Rectangle( 0.65f*this.CAMERA_WIDTH, 0.3f*this.CAMERA_HEIGHT, 3, 0.4f*this.CAMERA_HEIGHT );
		lines[6] = new Rectangle( 0.75f*this.CAMERA_WIDTH, 0.6f*this.CAMERA_HEIGHT, 3, 0.4f*this.CAMERA_HEIGHT );

		// Moving guys
		lines[7] = new Rectangle( 0.85f*this.CAMERA_WIDTH, 0.15f*this.CAMERA_HEIGHT, 0.10f*this.CAMERA_WIDTH, 3 );
		lines[8] = new Rectangle( 0.81f*this.CAMERA_WIDTH, 0.55f*this.CAMERA_HEIGHT, 0.13f*this.CAMERA_WIDTH, 3 );
		lines[9] = new Rectangle( 0.11f*this.CAMERA_WIDTH, 0.65f*this.CAMERA_HEIGHT, 0.13f*this.CAMERA_WIDTH, 3 );
		
		// Let us create a box body for some of the lines, and connect them with our physics this.mPhysicsWorld. Others are traps. 
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		for(int i=0; i<7; i++) 
		{
			if(gaur.nextBoolean()) 
			{
				PhysicsFactory.createBoxBody(this.mPhysicsWorld, lines[i], BodyType.StaticBody, wallFixtureDef); 
			} else {
				lines[i].setColor( 0.5f, 0.7f, 0.1f);
			}
		}
		
		// Rotation for the line-traps
		for(int i=7; i<10; i++) 
		{ 
			lines[i].addShapeModifier(new LoopShapeModifier(new ParallelShapeModifier(new RotationModifier(6, 0, 360)))); 
			lines[i].setColor(0.5f, 0.7f, 0.1f);
		}
		
		for(int i=0;i<10;i++) { pScene.getTopLayer().addEntity(lines[i]); }

		// Let's prepare the end goal
		final int centerX = (CAMERA_WIDTH - 32);
		final int centerY = (CAMERA_HEIGHT - 32);		
		endRect = new Rectangle(centerX, centerY, 32, 32);
		endRect.setColor(0, 0, 1);
		pScene.getTopLayer().addEntity(endRect);

		// Distribute rewards over the level	
		for(int i=0; i<6; i++)
		{
			rewards[i] = new Sprite(CodenameDutchess.randomNumber(5, this.CAMERA_WIDTH), 
					CodenameDutchess.randomNumber(5, this.CAMERA_HEIGHT), 
					this.mRewTextureRegion);
			
			rewards[i].setScale(0.9f);
			pScene.getTopLayer().addEntity(rewards[i]);			
		}

		
		/* The actual collision-checking. */
		pScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				for (int i=0; i<10; i++) {
					if(lines[i].collidesWith(CodenameDutchess.this.mAgent) && lines[i].getRed() == 0.5)
						lines[i].setColor(1, 0, 0);
				}
				
				for (int i=0; i<6; i++) {
					if(rewards[i].collidesWith(CodenameDutchess.this.mAgent)) {
						CodenameDutchess.this.scene.getTopLayer().removeEntity(rewards[i]);
					}
				}
				
				if(CodenameDutchess.this.endRect.collidesWith(CodenameDutchess.this.mAgent)) {
					CodenameDutchess.this.endRect.setColor(0, 1, 0);
				}
			}
		});
		
	}
	
	public static int randomNumber(int min, int max) { return min + (new Random()).nextInt(max-min); }


	
	
	
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
