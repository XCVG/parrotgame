package com.xcvgsystems.hypergiant.menus;

import java.util.Comparator;


public abstract class MenuObject {

	final static int LAYER_BACK = 0;
	final static int LAYER_WINDOW = 1;
	final static int LAYER_OBJECT = 2;

	final protected Menu CONTEXT;

	int x , y;
	int width, height;
	int layer;

	public abstract void tick();

	public abstract void draw();

	public abstract void click();
	
	public MenuObject(Menu context)
	{
		CONTEXT = context;
	}
	
	public MenuObject(int x, int y, int width, int height, int layer, Menu context)
	{
		this(context);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.layer = layer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static class CompareByLayer implements Comparator<MenuObject>{

		@Override
		public int compare(MenuObject arg0, MenuObject arg1) {
			return Integer.compare(arg0.layer, arg1.layer);
		}

	}



}
