package is.nord.dutchess;

import java.util.List;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import com.badlogic.gdx.math.Vector2;

public class SceneUpdateHandler implements IUpdateHandler, IAccelerometerListener {

	private PhysicsWorld physicsWorld;
	private Float timeLeft;
	private Integer coinsLeft;
	private ChangeableText timeText;
	private ChangeableText coinsText;
	private Scene scene;
	private List<GameObject> gameObjects;
	
	public SceneUpdateHandler(PhysicsWorld physicsWorld, ChangeableText timeText, ChangeableText coinsText, float levelTime, int coinsLeft, Scene scene) {
		this.physicsWorld = physicsWorld;
		this.timeLeft = levelTime;
		this.coinsLeft = coinsLeft;
		this.timeText = timeText;
		this.coinsText = coinsText;
		this.scene = scene;
	}
	
	public void setGameObjects(List<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		timeLeft -= pSecondsElapsed;
		timeText.setText(timeLeft.toString());
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
	
	public void coinCollision(CoinSprite coin) {
		this.coinsLeft--;
		this.coinsText.setText(coinsLeft.toString());
		this.scene.getTopLayer().removeEntity(coin);
		gameObjects.remove(coin);
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		this.physicsWorld.setGravity(new Vector2(pAccelerometerData.getY(), pAccelerometerData.getX()));
	}

}
