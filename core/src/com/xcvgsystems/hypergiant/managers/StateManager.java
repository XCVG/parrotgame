package com.xcvgsystems.hypergiant.managers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.xcvgsystems.hypergiant.models.Player;

/**
 * Provides state (variable) services. Won't do much for the parrot game.
 * @author Chris
 *
 */
public class StateManager {
	
	//TODO: handling player state and the whole quest system
	private static Player player;
	
	
	static Map<String, Integer> varInt;
	static Map<String, String> varStr;
	
	/**
	 * Initialize the StateManager.
	 */
	public static void init()
	{
		System.out.print("StateManager.init...");
		player = new Player();
		
		varInt = new HashMap<String, Integer>();
		varStr = new HashMap<String, String>();
		
		System.out.println("done!");
	}
	
	/**
	 * Dispose of the SceneManager.
	 */
	public static void dispose()
	{
		System.out.print("StateManager.dispose...");
		
		//TODO: clean dispose
		
		varInt = null;
		varStr = null;
		
		System.out.println("done!");
	}
	
	/**
	 * Resets state (ie restarting the game)
	 */
	public static void reset()
	{
		player = new Player();
	}

	/**
	 * Load game state (variables) from an input stream.
	 * @param input the input stream
	 */
	void loadState(InputStream input)
	{
		//Scanner scan = new Scanner(input);
	}
	
	/**
	 * Save game state (variables) to an output stream.
	 * @param output the output stream
	 */
	void saveState(OutputStream output)
	{
		//PrintWriter pr = new PrintWriter(output);

	}
	
	/**
	 * Get an integer variable
	 * @param key the name of the variable
	 * @return the variable
	 */
	public int get(String key)
	{
		return varInt.get(key.toUpperCase(Locale.ROOT));
	}
	
	/**
	 * Modifies an integer variable
	 * @param key the name of the variable
	 * @param value the new value of the variable
	 */
	public void put(String key, int value)
	{
		varInt.put(key.toUpperCase(Locale.ROOT), value);
	}
	
	/**
	 * Get a string variable
	 * @param key the name of the variable
	 * @return the variable
	 */
	public String getString(String key)
	{
		return varStr.get(key.toUpperCase(Locale.ROOT));
	}
	
	/**
	 * Modifies a string variable
	 * @param key the name of the variable
	 * @param value the new value of the variable
	 */
	public void putString(String key, String value)
	{
		varStr.put(key.toUpperCase(Locale.ROOT), value);
	}
	
	/**
	 * Gets the current Player object
	 * @return the current Player
	 */
	public static Player getPlayer() {
		return player;
	}

}
