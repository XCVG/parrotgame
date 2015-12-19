package com.xcvgsystems.hypergiant.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Overlay {
	
	SpriteBatch batch;
	OrthographicCamera camera;
	
	public abstract void update();
	public abstract void draw();
	public abstract void load();
	public abstract void unload();
	
	@Override
	protected void finalize() throws Throwable
	{
	    try{
	        batch.dispose();
	        batch = null;
	        camera = null;
	    }catch(Throwable t){
	        throw t;
	    }finally{
	        super.finalize();
	    }
	}

}
