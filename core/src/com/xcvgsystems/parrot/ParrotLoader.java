package com.xcvgsystems.parrot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.GVars;
import com.xcvgsystems.hypergiant.managers.MusicManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;
import com.xcvgsystems.hypergiant.menus.GenericMenu;
import com.xcvgsystems.hypergiant.menus.MainMenu;
import com.xcvgsystems.hypergiant.menus.Menu;
import com.xcvgsystems.hypergiant.scenes.ImageScene;
import com.xcvgsystems.hypergiant.scenes.IntroScene;
import com.xcvgsystems.hypergiant.scenes.MenuScene;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;

/**
 * Game-specific resource loader and setup, runs immediately after engine init and before game start.
 * @author Chris
 *
 */
public class ParrotLoader {
	
	public static void init()
	{		
		//run custom inits
		initMenus();
		initScenes();
		
		//change to initial scene
		SceneManager.changeScene("MainMenu");
	
	}
	
	public static void initScenes()
	{
		System.out.print("Init Scenes...");
		
		SceneManager.addScene("TestScene", new ScrollScene());
		SceneManager.addScene("IntroScene", new IntroScene("menu_intro", "M_TITLE", "MAP01"));
		SceneManager.addScene("EndScene", new ImageScene("menu_end", "M_END", "MainMenu"));
		
		SceneManager.addScene("Help1", new ImageScene("menu_help1", "M_TITLE", "Help2"));
		SceneManager.addScene("Help2", new ImageScene("menu_help2", "M_TITLE", "Help3"));
		SceneManager.addScene("Help3", new ImageScene("menu_help3", "M_TITLE", "MainMenu"));
		
		SceneManager.addScene("EndGood", new ImageScene("menu_end1", "M_VIC", "MainMenu"));
		SceneManager.addScene("EndMiddle", new ImageScene("menu_end2", "M_MIX", "MainMenu"));
		SceneManager.addScene("EndBad", new ImageScene("menu_end3", "M_END", "MainMenu"));
		
		System.out.println("done!");
	}
	
	public static void initMenus()
	{
		System.out.print("Init Menus...");
		
		//SceneManager.addScene("TestMenu", new MenuScene(new GenericMenu()));
		SceneManager.addScene("MainMenu", new MenuScene(new MainMenu()));
		
		System.out.println("done!");
	}

}
