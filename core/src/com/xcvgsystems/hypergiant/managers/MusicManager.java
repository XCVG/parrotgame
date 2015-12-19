package com.xcvgsystems.hypergiant.managers;

import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.xcvgsystems.hypergiant.GVars;

/**
 * Provides music services.
 * @author Chris
 *
 */
public class MusicManager {

	static Map<String, Music> musics;

	private static Music currentMusic;
	
	/**
	 * Initialize the MusicManager, loading all music in the game music path. 
	 */
	public static void init()
	{
		System.out.print("MusicManager.init...");
		
		musics = new HashMap<String, Music>();
		
		//load all the music
		FileHandle gamePath = Gdx.files.internal(GVars.GAME_PATH);
		FileHandle musicPath = gamePath.child("music");
		loadAll(musicPath);
				
		System.out.println("done!");
	}
	
	/**
	 * Dispose of the MusicManager, unloading all music.
	 */
	public static void dispose()
	{
		
		stop();
		currentMusic = null;
		
		Set<Map.Entry<String,Music>> entrySet = musics.entrySet();
		Iterator<Entry<String, Music>> it = entrySet.iterator();
		
		while(it.hasNext())
		{
			it.next().getValue().dispose();
			it.remove();
		}
		
		musics = null;
	}

	/**
	 * Loads a music file.
	 * @param name the name of the music
	 * @param file the file to load the music from
	 */
	public static void load(String name, FileHandle file)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try
		{
			musics.put(name, Gdx.audio.newMusic(file));
		}
		catch (Exception e)
		{
			System.err.println("Could not load " + file.toString());
		}
	}
	
	/**
	 * Loads all music files in the specified path.
	 * @param dir the folder to load music from
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
	 * Play a music track that is in the manager.
	 * -if the new music is the same, it will do nothing
	 * -if the new music does not exist, it will stop the existing track and play nothing
	 * 
	 * @param music
	 * @param looping
	 */
	public static void play(String music, boolean looping)
	{
		
		music = music.toUpperCase(Locale.ROOT);
		
		Music newMusic;
		
		newMusic = musics.get(music);
		
		//this is actual useful at times
		if(newMusic != null && newMusic == currentMusic)
		{
			return;
		}
	
		//stop current music if it's there
		if(currentMusic != null)
		{
			currentMusic.stop();
		}		
				
		if(newMusic != null)
		{
			newMusic.setLooping(looping);
			newMusic.play();
		}
		
		currentMusic = newMusic;
		
	}
	
	/**
	 * Plays a music track, forcing the track to restart regardless of the previous track.
	 * 
	 * @param music
	 * @param looping
	 */
	public static void playForceRestart(String music, boolean looping)
	{
		stop();
		play(music, looping);
	}
	
	/**
	 * Stop playing the current music.
	 */
	public static void stop()
	{
		if(currentMusic != null)
			currentMusic.stop();
		currentMusic = null;
	}
	
	/**
	 * Check if music is playing.
	 * @return if music is playing
	 */
	public static boolean isPlaying()
	{
		return (currentMusic != null);
	}
	
}
