package com.xcvgsystems.hypergiant.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;
import com.xcvgsystems.hypergiant.GVars;

/**
 * Provides sound services.
 * @author Chris
 *
 */
public class SoundManager {

	static Map<String, Sound> sounds;
	
	/**
	 * Initialize the SoundManager, loading all sounds in the game sounds path. 
	 */
	public static void init()
	{
		System.out.print("SoundManager.init...");
		
		sounds = new HashMap<String, Sound>();
		
		//load all the music
		FileHandle gamePath = Gdx.files.internal(GVars.GAME_PATH);
		FileHandle soundsPath = gamePath.child("sounds");
		loadAll(soundsPath);
		
		System.out.println("done!");
	}

	/**
	 * Dispose of the SoundManager, unloading all sounds.
	 */
	public static void dispose()
	{
		System.out.print("SoundManager.dispose...");
		
		Set<Map.Entry<String,Sound>> entrySet = sounds.entrySet();
		Iterator<Entry<String, Sound>> it = entrySet.iterator();
		
		while(it.hasNext())
		{
			it.next().getValue().dispose();
			it.remove();
		}
		
		sounds = null;
		
		System.out.println("done!");
	}
	
	/**
	 * Loads a sound file.
	 * @param name the name of the sound
	 * @param file the file to load the sound from
	 */
	public static void load(String name, FileHandle file)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try
		{
			sounds.put(name, Gdx.audio.newSound(file));
		}
		catch (Exception e)
		{
			System.err.println("Could not load " + file.toString());
		}
	}
	
	/**
	 * Loads all sound files in the specified path.
	 * @param dir the folder to load sound from
	 */
	public static void loadAll(FileHandle dir)
	{
		FileHandle[] files = dir.list();
		
		for(FileHandle file : files)
		{
			load(file.nameWithoutExtension(), file);
		}
	}
	
	/**
	 * Plays a sound that's in the manager.
	 * @param sound the sound to play
	 */
	public static void play(String sound)
	{
		play(sound, 1.0f);
	}
	
	/**
	 * Plays a sound that's in the manager.
	 * @param sound the sound to play
	 * @param volume how loud to play the sound
	 */
	public static void play(String sound, float volume)
	{
		sound = sound.toUpperCase();
		
		if(sounds.containsKey(sound))
		{
			sounds.get(sound).play(volume);
		}
	}
	
}
