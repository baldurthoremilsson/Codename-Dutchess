package is.nord.dutchess;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.ColoredTextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.opengl.font.Font;

import android.view.KeyEvent;

/**
 * 
 * @author gunnarr
 *
 * This class is constructs and manages scenes both for game play and menus. A part of that registering the scene to an 
 * update handler and implementing it's onUpdate method.  
 *
 */

public class SceneFactory implements IOnMenuItemClickListener {

	// Local variables
	private Scene activeScene;
	private Camera mCamera;
	private Font mFont;
	
	private GameObjectRegistry mGor;
	
	private static final int MENU_NEWGAME = 0;
	private static final int MENU_QUIT = MENU_NEWGAME + 1;
	
	/*
	 * Usage:	SceneFactory sf = new SceneFactory(camera, font, scene);
	 * Pre:		camera is of type Camera, font of type Font, and scene of type Scene, and all three have been set up
	 * Post:	sf is a SceenFactory object based on the parameters
	 */
	public SceneFactory(Camera camera, Font font, Scene scene, GameObjectRegistry gor)
	{
		this.mCamera = camera;
		this.mFont = font;
		this.activeScene = scene;
		
		this.mGor = gor;
		
		this.activeScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				// invoke onCollision() on game objects here
			}
		});
		
	}

	/*
	 * Usage:	gameScene = sf.getGameScene();
	 * Pre:		sf is a SceneFactory object
	 * Post:	gameScene represents the latest scene manifactured for the game, that is the level.
	 */
	public Scene getGameScene()
	{
		return this.activeScene;
	}

	/*
	 * Usage:	menuScene = sf.createMenuScene()
	 * Pre:		sf is a SceneFactory object
	 * Post:	menuScene holds the main gamemenu 
	 */
	public MenuScene createMenuScene() {
		final MenuScene menuScene = new MenuScene(this.mCamera);

		menuScene.addMenuItem(new ColoredTextMenuItem(MENU_NEWGAME, this.mFont, "NEW GAME", 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f));
		menuScene.addMenuItem(new ColoredTextMenuItem(MENU_QUIT, this.mFont, "QUIT", 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f));
		menuScene.buildAnimations();

		menuScene.setBackgroundEnabled(false);

		menuScene.setOnMenuItemClickListener(this);
		return menuScene;
	}
	
	/*
	 * Usage:	welcomeScene = sf.createWelcomeScene();
	 * Pre:		sf is a SceneFactory object
	 * Post:	welcomeScene holds the initial scene loaded when the game is started
	 */
	public Scene createWelcomeScene()
	{
		this.activeScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		this.activeScene.setChildScene(this.createMenuScene(), false, true, true);
		return this.activeScene;
	}
	
	
	
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) 
	{
		switch(pMenuItem.getID()) 
		{
			case MENU_QUIT: System.exit(0); // Should also be activity finish something
			case MENU_NEWGAME: this.activeScene.reset();
			return true;
		}
		return false;
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.activeScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.activeScene.back();
			} else {
				/* Attach the menu. */
				this.activeScene.setChildScene(this.createMenuScene(), false, true, true);
			}
			return true;
		} else {
			return false;//super.onKeyDown(pKeyCode, pEvent);
		}
	}

}
