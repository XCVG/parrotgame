package com.xcvgsystems.hypergiant.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.managers.FontManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;

//basic lifebar and time remaining HUD
public class WorldHUDOverlay extends Overlay {

	final int WIDTH = EVars.WINDOW_X;
	final int HEIGHT = EVars.WINDOW_Y;
	
	int hp;
	int time, timeleft;
	int timesec;
	
	public WorldHUDOverlay()
	{

		//init camera
		camera= new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(true, WIDTH, HEIGHT);

		//init spritebatch
		batch = new SpriteBatch();
	}
	
	@Override
	public void update() {
		//TODO: updating values!
		//System.err.println(this + "UPDATE!");
		
		hp = StateManager.getPlayer().getHp();
		time = ((ScrollScene) SceneManager.getCurrentScene()).getTimeElapsed();
		timeleft = ((ScrollScene) SceneManager.getCurrentScene()).getTIME();

		timesec = (timeleft - time) / 60;
		
	}

	@Override
	public void draw() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		//draw healthbar
		//batch.draw(TextureManager.get("UI_hbarback"), 0, 0, 120, 60);
		batch.draw(TextureManager.get("UI_frame"), 0, 0, 120, 60);
		switch(hp)
		{
		case 0:
			batch.draw(TextureManager.get("UI_hbar0"), 10, 10, 100, 12);
			break;
		case 1:
			batch.draw(TextureManager.get("UI_hbar1"), 10, 10, 100, 12);
			break;
		case 2:
			batch.draw(TextureManager.get("UI_hbar2"), 10, 10, 100, 12);
			break;
		default:
			batch.draw(TextureManager.get("UI_hbar3"), 10, 10, 100, 12);
			break;
		}
		
		//FontManager.writeInto(Integer.toString(hp), batch, 40, 40);
		//FontManager.writeInto(Integer.toString(time) + " / " + Integer.toString(timeleft), batch, 40, 80);
		batch.draw(TextureManager.get("UI_hbarclock"), 20, 26, 16, 16);
		FontManager.writeInto(Integer.toString(timesec), "HUDFONT1", batch, 40, 28);
		
		batch.end();
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
