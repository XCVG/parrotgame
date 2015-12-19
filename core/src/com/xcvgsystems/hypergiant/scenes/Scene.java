package com.xcvgsystems.hypergiant.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {

	SpriteBatch batch;
	OrthographicCamera camera;
	
	public abstract void enter();
	
	public abstract void tick(); //run a tick
	
	public abstract void draw(); //run a draw
	
	public abstract void exit();
	
}
