package com.xcvgsystems.hypergiant.scenes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapTile {
	
	protected int x, y;
	protected TextureRegion texture;
	
	/**
	 * @param x
	 * @param y
	 * @param texture
	 */
	public MapTile(int x, int y, TextureRegion texture) {
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public TextureRegion getTexture() {
		return texture;
	}
	
	

}
