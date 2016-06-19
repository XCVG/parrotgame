package com.xcvgsystems.hypergiant.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.managers.FontManager;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;

//a bit of a hack
public class NextMapOverlay extends Overlay {

	static final int WINDOW_WIDTH = 200;
	static final int WINDOW_HEIGHT = 120;
	static final int TICK_DELAY = 10;
	
	final int WIDTH = EVars.WINDOW_X;
	final int HEIGHT = EVars.WINDOW_Y;
	
	protected String message;
	protected String next;
	
	int ticksActive = 0;
	
	public NextMapOverlay(String message, String next)
	{
		this.message = message;
		this.next = next;
		
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

		//check for input
		if(InputManager.isUsePressed() || InputManager.isTouchPressed() || InputManager.isStartPressed())
		{
			//SceneManager.changeScene(next);
			SceneManager.changeScene("MainMenu");
			SceneManager.setRunning(true);
			//OverlayManager.deactivateOverlay(this); //well that seems pretty safe
			//ticksActive = 0;
		}

	}

	@Override
	public void draw() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		batch.draw(TextureManager.get("UI_frame"), WIDTH/2 - WINDOW_WIDTH/2, HEIGHT/2 - WINDOW_HEIGHT/2, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		FontManager.writeCentered(message, "SMALLFNT", batch, WIDTH/2, HEIGHT/2);
		
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
