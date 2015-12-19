
package com.xcvgsystems.hypergiant.menus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xcvgsystems.hypergiant.managers.FontManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;

public class MenuText extends MenuObject {

	final static String D_FONT = "DEFAULT";
	
	protected String text;
	protected String font;
	
	public MenuText(Menu context) {
		this(0,0,MenuObject.LAYER_OBJECT, "", context);
		
		System.err.println("CREATED A USELESS TEXT OBJECT");
	}
	
	public MenuText(int x, int y, int layer, String text, Menu context) {
		super(x, y, 0, 0, layer, context);
		
		this.text = text;
		this.font = D_FONT;
		
	}

	public MenuText(int x, int y, int layer, String font, String text, Menu context) {
		super(x, y, 0, 0, layer, context);
		
		this.text = text;
		this.font = font;
		
	}

	@Override
	public void draw() {
		FontManager.writeCentered(text, font, CONTEXT.batch, x, y);

	}

	@Override
	public void click() {
		

	}

	@Override
	public void tick() {
		
		
	}

}
