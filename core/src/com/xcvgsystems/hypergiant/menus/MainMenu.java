package com.xcvgsystems.hypergiant.menus;

import com.badlogic.gdx.Gdx;
import com.xcvgsystems.hypergiant.managers.MusicManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.models.MenuChoice;

public class MainMenu extends Menu {
	
	protected final String D_MUSIC = "M_TITLE";

	public MainMenu() {
		super();
		//MusicManager.play(D_MUSIC, true);
		
		//objects.add(new MenuButton(this));
		objects.add(new MenuText(WIDTH/2, 400, MenuObject.LAYER_OBJECT, "SMALLFNT", "For want of a message...", this));
		objects.add(new MenuImage(WIDTH/2, HEIGHT/2 + 40, 400, 300, MenuObject.LAYER_WINDOW, "UI_frame", this));
		objects.add(new MenuImage(WIDTH/2, 60, 400, 100, MenuObject.LAYER_WINDOW, "menu_title", this));
		objects.add(new MenuButton(WIDTH/2, (int)(HEIGHT/2.5f), 200, 80, MenuObject.LAYER_OBJECT, "button_start", 1, this));
		//objects.add(new MenuButton(WIDTH/2, (int)(HEIGHT/1.5f), 200, 80, MenuObject.LAYER_OBJECT, "button_exit", 2, this));
		objects.add(new MenuButton(WIDTH/2, (int)(HEIGHT/1.5f), 200, 80, MenuObject.LAYER_OBJECT, "button_help", 3, this));
		
		//create buttons
		
		//System.err.println(objects);
	}
	
	@Override
	public void enter() {
		super.enter();
		MusicManager.play(D_MUSIC, true);
		
	}

	@Override
	public void exit() {
		super.exit();
		
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void invokeEvent(int ev) {
		
		switch(ev)
		{
		case 1:
			StateManager.reset();
			SceneManager.changeScene("IntroScene");
			break;
		case 2:
			Gdx.app.exit();
			break;
		case 3:
			SceneManager.changeScene("Help1");
			break;
		default:
			break;
		}
		
	}

}
