package com.xcvgsystems.hypergiant.scenes;
import com.badlogic.gdx.math.MathUtils;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;

public class IntroScene extends ImageScene {

	public IntroScene(String image, String music, String nextScene) {
		super(image, music, nextScene);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void tick() {
		//I figured out what this does
		//it's so you can't instantly skip the scene
		if(ticksActive < TICK_DELAY)
		{
			ticksActive++;
			return;
		}

		//check for input and go to the next scene if it's there
		if(InputManager.isUsePressed() || InputManager.isStartPressed() || InputManager.isTouchJustPressed())
		{
			int mapnum = MathUtils.random.nextInt(8) + 1;
			SceneManager.changeScene("MAP0" + mapnum);
		}
		
	}

}
