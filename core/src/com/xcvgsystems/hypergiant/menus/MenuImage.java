package com.xcvgsystems.hypergiant.menus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xcvgsystems.hypergiant.managers.TextureManager;

public class MenuImage extends MenuObject {

	Sprite sprite;
	
	public MenuImage(Menu context) {
		this(0,0,1,1,MenuObject.LAYER_OBJECT, "", context);
		
		System.err.println("CREATED A USELESS IMAGE OBJECT");
	}

	public MenuImage(int x, int y, int width, int height, int layer, String graphic, Menu context) {
		super(x, y, width, height, layer, context);
		
		sprite = new Sprite(TextureManager.get(graphic));
	}

	@Override
	public void tick() {

		//I am static; do nothing
		
	}

	@Override
	public void draw() {
		sprite.setSize(width, height);
		sprite.setCenter(x, y);
		sprite.draw(CONTEXT.batch);
	}

	@Override
	public void click() {
		
		//I am static; do nothing

	}

}
