package com.xcvgsystems.hypergiant.scenes;

import com.xcvgsystems.hypergiant.menus.Menu;

//new menu system means this actually just wraps a Menu
public class MenuScene extends Scene {

	Menu menu;
	
	public MenuScene(Menu menu)
	{
		this.menu = menu;
	}
	
	@Override
	public void enter() {
		menu.enter();
	}

	@Override
	public void tick() {
		menu.update();
	}

	@Override
	public void draw() {
		menu.draw();
		
	}

	@Override
	public void exit() {
		menu.exit();
	}

}
