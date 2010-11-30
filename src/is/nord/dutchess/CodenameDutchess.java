package is.nord.dutchess;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;

import org.anddev.andengine.engine.camera.BoundCamera;
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
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.util.Log;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.io.IOException;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Hopur eitt
 */
public class CodenameDutchess extends BaseGameActivity implements
		IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	public static final int MENU_UNHANDLED = 0;
	public static final int MENU_MAIN_NEWGAME = 1;
	public static final int MENU_MAIN_QUIT = 2;
	public static final int MENU_PAUSE_CONTINUE = 3;
	public static final int MENU_PAUSE_QUIT = 4;
	public static final int MENU_GAMEOVER_CONTINUE = 5;
	public static final int MENU_GAMEOVER_QUIT = 6;
	public static final int MENU_WINNING_NEXT_LEVEL = 7;
	public static final int MENU_WINNING_QUIT = 8;

	public static final String DEBUG_TAG = "SCENEDEBUG";
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private BoundCamera mBoundChaseCamera;
	private Scene scene;

	private Music mMusic;
	private Music mZelda;
	private Music mCoinSound;

	private PhysicsWorld mPhysicsWorld;

	// Texture and region for the agent
	private Texture mAgentTexture;
	private TextureRegion mAgentTextureRegion;
	private Sprite mAgent;
	private Body mAgentBody;

	// Test wood texture
	private Texture mWoodTextureVertical;
	private TextureRegion mWoodTextureVerticalRegion;
	private Texture mWoodTextureHorizonal;
	private TextureRegion mWoodTextureHorizonalRegion;

	// Grenade Texture
	private Texture mGrenadeTexture;
	private TextureRegion mGrenadeTextureRegion;

	// Font texture
	private Texture mFontTexture;
	private Font mFont;
	private Texture altFontTexture;
	private Font altFont;

	// Texture and region for the rewards to be collected
	private Texture mRewTexture;
	private TextureRegion mRewTextureRegion;

	private RepeatingSpriteBackground mGrassBackground;

	private Integer coins;
	private Integer timeLeft;

	SceneFactory sf;
	GameObjectRegistry gor;
	GameManager gm;
	AudioManager am;

	private ChangeableText mScoreText;
	protected int currScene;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH,
				CAMERA_HEIGHT, 0, CAMERA_WIDTH * 2, 0, CAMERA_HEIGHT * 2);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mBoundChaseCamera).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		/* Load Font/Textures. */
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);

		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this,
				"Droid.ttf", 48, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

		/* Load Sprite Textures */
		TextureRegionFactory.setAssetBasePath("gfx/");
		// Agent
		this.mAgentTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mAgentTextureRegion = TextureRegionFactory.createFromAsset(
				this.mAgentTexture, this, "pokese6.png", 0, 0);
		this.mRewTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRewTextureRegion = TextureRegionFactory.createFromAsset(
				this.mRewTexture, this, "coin.png", 0, 0);

		// Grenades
		this.mGrenadeTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mGrenadeTextureRegion = TextureRegionFactory.createFromAsset(
				this.mGrenadeTexture, this, "face_box.png", 0, 0);
		// Wood
		this.mWoodTextureVertical = new Texture(8, 64, TextureOptions.REPEATING);
		this.mWoodTextureVerticalRegion = TextureRegionFactory.createFromAsset(
				this.mWoodTextureVertical, this, "wood_small.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(this.mAgentTexture,
				this.mRewTexture, this.mWoodTextureVertical);

		/* A Grass background */
		this.mGrassBackground = new RepeatingSpriteBackground(CAMERA_WIDTH,
				CAMERA_HEIGHT, this.mEngine.getTextureManager(),
				new AssetTextureSource(this, "gfx/background_grass.png"));

		/* Game Music */
		MusicFactory.setAssetBasePath("mfx/");

		try {
			// Last one seems to start? how about creating playlist?
			this.mMusic = MusicFactory.createMusicFromAsset(
					this.mEngine.getMusicManager(), this, "unreal.ogg");
			mMusic.setLooping(true);
			this.mZelda = MusicFactory.createMusicFromAsset(
					this.mEngine.getMusicManager(), this, "zelda.ogg");
			mZelda.setLooping(true);
			this.mCoinSound = MusicFactory.createMusicFromAsset(
					this.mEngine.getMusicManager(), this, "smb_coin.ogg");

			// this.mCoinSound =
			// SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(),
			// this, "munch.ogg");
			// this.mMusic =
			// MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(),
			// this, "Acid_techno.ogg");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		// currScene = 1;

		this.gor = new GameObjectRegistry(this.mPhysicsWorld);
		this.gm = new GameManager(0, 1);
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.am = new AudioManager(this.mCoinSound);
		this.am.addToPlayList(mMusic);
		this.am.addToPlayList(mZelda);

		// Append our textures and stuff to our game object registry
		this.gor.setAgentTextureRegion(this.mAgentTextureRegion);
		this.gor.setCoinTextureRegion(this.mRewTextureRegion);
		this.gor.setWallTextureRegion(this.mWoodTextureVerticalRegion);
		this.gor.setTrapTextureRegion(this.mGrenadeTextureRegion);
		this.gor.setRepeatingBackground(this.mGrassBackground);

		sf = new SceneFactory(this, this.mBoundChaseCamera, this.mFont, this.gor, this.am);
		// this.scene = sf.createStartScene(this);

		return sf.createStartScene(this);
	}

	@Override
	public void onLoadComplete() {

	}
	
	public void setAccelerometerSensor(IAccelerometerListener accelerometerListener) {
		this.enableAccelerometerSensor(accelerometerListener);
	}

	public static int randomNumber(int min, int max) {
		return min + (new Random()).nextInt(max - min);
	}
	
	public void gameOver() {
		mEngine.setScene(this.sf.createGameOverScene(this));
	}
	
	public void gameWon() {
		mEngine.setScene(this.sf.createWinningScene(this));
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_MAIN_QUIT:
		case MENU_PAUSE_QUIT:
		case MENU_GAMEOVER_QUIT:
		case MENU_WINNING_QUIT:
			System.exit(0); // Should also be activity finish something
			break;
		case MENU_MAIN_NEWGAME:
			mEngine.setScene(this.sf.createLevelScene(1));
			break;
		case MENU_PAUSE_CONTINUE:
			mEngine.getScene().back();
			break;
		case MENU_GAMEOVER_CONTINUE:
			mEngine.setScene(this.sf.createLevelScene(1));
			break;
		case MENU_WINNING_NEXT_LEVEL:
			mEngine.setScene(this.sf.createLevelScene(2));
			break;
		case MENU_UNHANDLED:
			break;
		}
		return false;
	}

	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (this.mEngine.getScene().hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mEngine.getScene().back();
			} else {
				/* Attach the menu. */
				this.mEngine.getScene().setChildScene(sf.createPauseScene(this), false, true, true);
			}
		}
		return true;
	}
}
