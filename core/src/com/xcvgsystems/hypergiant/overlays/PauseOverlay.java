package com.xcvgsystems.hypergiant.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.managers.FontManager;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;

//TODO
public class PauseOverlay extends Overlay {

	static final int WINDOW_WIDTH = 200;
	static final int WINDOW_HEIGHT = 120;
	static final int TICK_DELAY = 10;
	
	final int WIDTH = EVars.WINDOW_X;
	final int HEIGHT = EVars.WINDOW_Y;
	
	int ticksActive = 0;
	
	public PauseOverlay() {
		//init camera
		camera= new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(true, WIDTH, HEIGHT);

		//init spritebatch
		batch = new SpriteBatch();
	}

	@Override
	public void update() {
		
		//a gross hack
		if(ticksActive < TICK_DELAY)
		{
			ticksActive++;
			return;
		}
		
		if(InputManager.isEscJustPressed())
		{
			SceneManager.setRunning(true);
			//SceneManager.setCurrentOverlay(SceneManager.OVERLAY_DEFAULT_HUD);
			SceneManager.setCurrentOverlay(null, 2);
		}

	}

	@Override
	public void draw() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		batch.draw(TextureManager.get("UI_pause"), WIDTH/2 - WINDOW_WIDTH/2, HEIGHT/2 - WINDOW_HEIGHT/2, WINDOW_WIDTH, WINDOW_HEIGHT);

		batch.end();


	}
	
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub
		
	}

}
