package com.xcvgsystems.hypergiant.menus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xcvgsystems.hypergiant.managers.TextureManager;

public class MenuButton extends MenuObject {
	
	static final int D_WIDTH = 80;
	static final int D_HEIGHT = 40;
	static final int D_LAYER = MenuObject.LAYER_OBJECT;

	protected int event;
	Sprite sprite;
	
	public MenuButton(int x, int y, int width, int height, int layer, String graphic, int event, Menu context)
	{
		super(x, y, width, height, layer, context);
		this.event = event;
		
		sprite = new Sprite(TextureManager.get(graphic));
		
	}
	
	public MenuButton(Menu context) {
		this(0, 0, D_WIDTH, D_HEIGHT, D_LAYER, "", 0, context);
	}

	@Override
	public void draw() {
		sprite.setSize(width, height);
		sprite.setCenter(x, y);
		sprite.draw(CONTEXT.batch);
	}

	@Override
	public void click() {
		// TODO Auto-generated method stub
		//System.err.println("CLICKED" + this);
		
		CONTEXT.invokeEvent(event);

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
