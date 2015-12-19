package com.xcvgsystems.hypergiant.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;

//should this be abstract?
public abstract class Menu {
	
	public static final String D_BACKGROUND = "MENUBACK";
	
	SpriteBatch batch;
	OrthographicCamera camera;	
	
	protected boolean active; //means the interface is active
	
	protected List<MenuObject> objects;
	
	//who likes dirty pseudo-constants! me! me!
	protected TextureRegion background;
	public final int WIDTH, HEIGHT;
	
	public Menu()
	{
		background = TextureManager.get(D_BACKGROUND);
		WIDTH = EVars.WINDOW_X;
		HEIGHT = EVars.WINDOW_Y;
		
		objects = new ArrayList<MenuObject>();
		
		
		
		active = true;
	}

	public void enter()
	{
		//init camera
		camera= new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(true, WIDTH, HEIGHT);

		//init spritebatch
		batch = new SpriteBatch();
	}

	public void exit()
	{
		batch.dispose();
		batch = null;
		camera = null;
	}

	public void update()
	{
		//sort all objects
		Collections.sort(objects, new MenuObject.CompareByLayer());
		
		//tick all objects
		for(MenuObject o : objects)
		{
			o.tick();
		}
		
		//TODO: perform click handling
		if(active && InputManager.isTouchJustPressed())
		{
			//this neat block here transforms touch input from the screen coordinate space to render coordinate space
			//it can be done using matrices, but I almost failed matrix algebra
			
			int touchX = InputManager.getTouchX();
			int touchY = InputManager.getTouchY();
			
			//System.err.println("(" + touchX + "," + touchY + ")");
			
			int transX = Math.round((float)touchX * ((float)WIDTH / Gdx.graphics.getWidth())); //seems legit
			int transY = Math.round((float)touchY * ((float)HEIGHT / Gdx.graphics.getHeight()));
			
			//System.err.println("(" + transX + "," + transY + ")");
			
			//find objects and click them
			for(MenuObject o : objects)
			{
				boolean touchedX = (o.x - o.width/2 < transX) && (transX < o.x + o.width/2);
				boolean touchedY = (o.y - o.height/2 < transY) && (transY < o.y + o.height/2);
				if(touchedX && touchedY)
				{
					o.click();
				}
			}
			
		}

	}
	
	public void draw()
	{
		//setup draw
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		//draw background

		batch.draw(background, 0, 0, WIDTH, HEIGHT);
		
		batch.flush();

		//sort objects by layer
		Collections.sort(objects, new MenuObject.CompareByLayer());

		//draw
		for(MenuObject o : objects)
		{
			o.draw();
			batch.flush();
		}
		
		batch.end();
	}
	
	//base for probably the worst event system in existence
	public abstract void invokeEvent(int ev);
	
	@Override
	protected void finalize() throws Throwable
	{
	    try{
	        batch.dispose();
	        batch = null;
	        camera = null;
	        objects = null;
	    }catch(Throwable t){
	        throw t;
	    }finally{
	        super.finalize();
	    }
	}
	
	

}
