package com.xcvgsystems.hypergiant.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.MusicManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;

public class ImageScene extends StaticScene {

	static final int TICK_DELAY = 10;
	
	protected TextureRegion background;
	protected String music;
	protected String nextScene;
	
	int ticksActive;
	
	public ImageScene(String image, String music, String nextScene)
	{
		camera= new OrthographicCamera(EVars.WINDOW_X, EVars.WINDOW_Y);
		camera.setToOrtho(true, EVars.WINDOW_X, EVars.WINDOW_Y);

		if(image != null)
			background = TextureManager.get(image);
		
		this.music = music;
		this.nextScene = nextScene;
		
	}
	
	@Override
	public void enter()
	{
		ticksActive = 0;
		batch = new SpriteBatch();
		MusicManager.play(music, true);
	}

	@Override
	public void tick() {
		//a gross hack
		if(ticksActive < TICK_DELAY)
		{
			ticksActive++;
			return;
		}

		//check for input
		if(InputManager.isUsePressed() || InputManager.isTouchJustPressed())
			SceneManager.changeScene(nextScene);
	}

	@Override
	public void draw() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0, EVars.WINDOW_X, EVars.WINDOW_Y);
		batch.end();
	}

	@Override
	public void exit()
	{
		batch = null;
		//MusicManager.stop();
	}

}
