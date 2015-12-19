package com.xcvgsystems.hypergiant.managers;

import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xcvgsystems.hypergiant.GVars;
import com.xcvgsystems.hypergiant.exceptions.SceneNotFoundException;
import com.xcvgsystems.hypergiant.menus.ChoiceMenu;
import com.xcvgsystems.hypergiant.models.MenuChoice;
import com.xcvgsystems.hypergiant.overlays.Overlay;
import com.xcvgsystems.hypergiant.overlays.WorldHUDOverlay;
import com.xcvgsystems.hypergiant.scenes.*;
import com.xcvgsystems.hypergiant.utils.Breaker;

/**
 * Provides scene services.
 * @author Chris
 *
 */
public class SceneManager {
	
	public static final String SCENE_MENU = "MainMenu";
	public static final String SCENE_END = "EndScene";

	//eventually the overlay system will be much more advanced
	public static Overlay OVERLAY_DEFAULT_HUD;
	
	static Map<String,Scene> scenes;

	static Scene currentScene;
	static Overlay[] currentOverlays;

	private static boolean running;
	private static boolean drawing;
	private static Scene nextScene;
	private static boolean justChangedScene;
	private static long tickNum;
	
	/**
	 * Initialize the SceneManager, loading all maps as scenes.
	 */
	public static void init()
	{
		System.out.print("SceneManager.init...");
		
		scenes = new HashMap<String, Scene>();
		tickNum = 0;
		
		//load all maps (might move this)
		FileHandle gamePath = Gdx.files.internal(GVars.GAME_PATH);
		//FileHandle mapsPath = gamePath.child("maps");
		loadAllMaps(gamePath.child("data").child("MAPINFO.txt"));
		
		OVERLAY_DEFAULT_HUD = new WorldHUDOverlay();
		//currentOverlay = OVERLAY_DEFAULT_HUD;
		
		currentOverlays = new Overlay[8];
		
		System.out.println("done!");
	}
	
	//TODO: move load methods to a helper class?
	
	/**
	 * Load all maps into scene
	 */
	private static void loadAllMaps(FileHandle file) {

		//System.err.println(file.name());
		Map<String, Map<String, String>> mapMap = Breaker.parseFile(file.readString());
		
		//System.err.println(mapMap);
		
		//TODO: iterate through Map, load ScrollScene and ChoiceMenu			
		Iterator<Entry<String, Map<String, String>>> mIt = mapMap.entrySet().iterator();
		while(mIt.hasNext())
		{
			Entry<String, Map<String, String>> entry = mIt.next();
			String name = entry.getKey();
			Map<String, String> kvmap = entry.getValue();
			
			//handle different types of definitions
			if(name.startsWith("MAP"))
			{
				scenes.put(name, loadScrollScene(kvmap));
			}
			else if(name.startsWith("MENU"))
			{
				scenes.put(name, loadChoiceMenuScene(kvmap));
			}
			
		}		
		
	}
	
	//TODO: move Scene methods into SceneHelper class
	
	private static ScrollScene loadScrollScene(Map<String, String> kvmap)
	{
		//TODO: safety
		
		//System.err.println(kvmap);

		Map<String, Integer> spawnList = new HashMap<String, Integer>();
		
		String spawnItemBase = "enemy";
		String spawnItemParam = "rate";
		int currSpawnNum = 1;
		String currSpawnItem = spawnItemBase + currSpawnNum;
				
		while(kvmap.containsKey(currSpawnItem))
		{
			//System.err.println(kvmap.get(currSpawnItem));
			//System.err.println(kvmap.get(currSpawnItem+spawnItemParam));
			
			spawnList.put(kvmap.get(currSpawnItem), Integer.parseInt(kvmap.get(currSpawnItem+spawnItemParam)));
			
			currSpawnNum++;
			currSpawnItem = spawnItemBase + currSpawnNum;
		}
		
		//System.err.println(spawnList);
		
		ScrollScene scn = new ScrollScene(kvmap.get("name"), kvmap.get("music"), Integer.parseInt(kvmap.get("time")),
				kvmap.get("background"), kvmap.get("foreground"), Integer.parseInt(kvmap.get("fgheight")), Integer.parseInt(kvmap.get("bgscroll")),
				Integer.parseInt(kvmap.get("fgscroll")), Integer.parseInt(kvmap.get("speed")), kvmap.get("next"), kvmap.get("endnext"), kvmap.get("endtext"), kvmap.get("special"), spawnList );
		
		return scn;
	}
	
	private static MenuScene loadChoiceMenuScene(Map<String, String> kvmap)
	{
		
		//System.err.println(kvmap);
		
		List<MenuChoice> choices = new ArrayList<MenuChoice>();
		
		//TODO: getting all the choices
		final String choiceBase = "choice";
		final String choiceParamTitle = "title";
		final String choiceParamDescription = "text";
		int currChoiceNum = 1;
		String currChoiceName = choiceBase + currChoiceNum;
		
		while(kvmap.containsKey(currChoiceName))
		{
			//System.err.println(kvmap.get(currSpawnItem));
			//System.err.println(kvmap.get(currSpawnItem+spawnItemParam));
			
			choices.add(new MenuChoice(kvmap.get(currChoiceName + choiceParamTitle), kvmap.get(currChoiceName + choiceParamDescription), kvmap.get(currChoiceName)));
			
			currChoiceNum++;
			currChoiceName = choiceBase + currChoiceNum;
		}
		
		//System.err.println(choices);
		
		ChoiceMenu menu = new ChoiceMenu(kvmap.get("title"), kvmap.get("description"), kvmap.get("background"), kvmap.get("music"), choices);
		
		MenuScene scn = new MenuScene(menu);
		
		return scn;		
	}

	/**
	 * Advance one tick on the current scene.
	 */
	public static void tick()
	{
		if(running)
		{
			//change scene
			if(justChangedScene)
			{
				if(currentScene != null)
					currentScene.exit();
				currentScene = nextScene;
				currentScene.enter();
				nextScene = null;
				justChangedScene = false;
			}
			
			currentScene.tick();
			tickNum++;
		}
		
		for(Overlay o : currentOverlays)
		{
			if(o != null)
				o.update();
		}
		
		//OverlayManager.tick();
	}
	
	/**
	 * Draw the current scene.
	 */
	public static void draw()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(drawing && currentScene != null)
			currentScene.draw();
		
		for(Overlay o : currentOverlays)
		{
			if(o != null)
				o.draw();
		}
		
		//OverlayManager.draw();
	}
		
	/**
	 * Dispose of the SceneManager.
	 */
	public static void dispose()
	{
		System.out.print("SceneManager.dispose...");
		
		scenes = null;
		currentScene = null;
		
		System.out.println("done!");
	}

	/**
	 * Change scenes.
	 * @param scene the new scene
	 */
	public static void changeScene(String scene)
	{
		
		System.out.println("SceneManager.changeScene : " + scene);
		
		scene = scene.toUpperCase(Locale.ROOT);

		
		if(scenes.containsKey(scene))
		{	
			//if(currentScene != null)
			//	currentScene.exit();
			nextScene = scenes.get(scene);
			//currentScene.enter();
			justChangedScene = true;
		}
		else
		{
			System.err.println("SCENE \"" + scene + "\" NOT FOUND!");
		}
		
	}
		
	/**
	 * Adds a scene to the scene manager.
	 * @param name the name of the scene to add
	 * @param scene the scene to add
	 */
	public static void addScene(String name, Scene scene)
	{
		name = name.toUpperCase(Locale.ROOT);
		scenes.put(name, scene);
	}
	
	/**
	 * Drops a scene from the scene manager.
	 * @param name the scene to remove
	 */
	public static void dropScene(String name)
	{
		name = name.toUpperCase(Locale.ROOT);
		scenes.remove(name);
	}

	/**
	 * @return if the scene is running
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * @param running whether the scene is running
	 */
	public static void setRunning(boolean running) {
		SceneManager.running = running;
	}

	/**
	 * @return if the scene is drawing
	 */
	public static boolean isDrawing() {
		return drawing;
	}

	/**
	 * @param drawing whether the scene is drawing
	 */
	public static void setDrawing(boolean drawing) {
		SceneManager.drawing = drawing;
	}
	
	/**
	 * @return the current number of ticks elapsed
	 */
	public static long getTicks() {
		return tickNum;
	}

	/**
	 * @return the current Scene
	 */
	public static Scene getCurrentScene() {
		return currentScene;
	}

	/**
	 * @return the current Overlay
	 */
	public static Overlay getCurrentOverlay() {
		return currentOverlays[0];
	}
	
	/**
	 * @return the current Overlay
	 */
	public static Overlay getCurrentOverlay(int index) {
		return currentOverlays[index];
	}

	/**
	 * Sets the overlay to specified.
	 */
	public static void setCurrentOverlay(Overlay currentOverlay) {
		SceneManager.currentOverlays[0] = currentOverlay;
	}
	
	/**
	 * Sets the overlay to specified.
	 */
	public static void setCurrentOverlay(Overlay currentOverlay, int index) {
		SceneManager.currentOverlays[index] = currentOverlay;
	}
	
	/**
	 * Clears current overlays.
	 */
	public static void clearCurrentOverlays() {
		for(int i = 0; i < currentOverlays.length; i++)
		{
			currentOverlays[i] = null;
		}
	}
	
	
	
	
}
