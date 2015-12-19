package com.xcvgsystems.parrot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.Engine;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;

public class ParrotGame extends ApplicationAdapter {

	@Override
	public void create() {

		// init engine
		Engine.init();
		
		//init game-specific code
		ParrotLoader.init();
		
		//start engine
		Engine.start();		
		
	}

	@Override
	public void render() {
		
		Engine.run();		

	}

	@Override
	public void dispose() {
		// dispose of all the native resources

		Engine.dispose();
		
	}

	@Override
	public void resize(int width, int height) {
		//IT JUST WORKS		
		//(sort of)
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
