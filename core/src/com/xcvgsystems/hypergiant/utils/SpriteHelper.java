package com.xcvgsystems.hypergiant.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xcvgsystems.hypergiant.managers.TextureManager;

/**
 * Grabs all sprites for a thinker. Not fault tolerant.
 * @author Chris
 *
 */
public class SpriteHelper {
	
	/**
	 * All letters corresponding to frame numbers.
	 */
	public static final char[] LETTERS = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	/**
	 * All four available directions (I regret using 1357 instead of 1234 like a human)
	 */
	public static final int[] DIRECTIONS = {1, 3, 5, 7};
	
	/**
	 * Fills the given array with graphic names.
	 * @param tname The name of the actor's graphics.
	 * @param names The array to fill with names.
	 */
	public static void getNames(String tname, String[][] names)
	{
		for(int row = 0; row < names.length; row++)
		{
			String fname = tname + LETTERS[row];
			for(int i = 0; i < DIRECTIONS.length; i++)
			{
				int fnum = DIRECTIONS[i];
				names[row][fnum] = fname + fnum;
			}
		}
	}
	
	private static String[] getFrameNames(String tname, int frames)
	{
		String[] names = new String[frames];
		
		for(int i=0; i < names.length; i++)
		{
			names[i] = tname + LETTERS[i];
		}
		
		return names;
	}
	
	/**
	 * Fills the given array with textures.
	 * @param tname The name of the actor's graphics.
	 * @param textures The array to fill with textures.
	 */
	public static void getTextures(String tname, TextureRegion[][] textures)
	{
		for(int row = 0; row < textures.length; row++)
		{
			String fname = tname + LETTERS[row];
			for(int i = 0; i < DIRECTIONS.length; i++)
			{
				int fnum = DIRECTIONS[i];
				textures[row][fnum] = TextureManager.get(fname + fnum);
			}
		}
	}
	
	/**
	 * Fills the given array with sprites.
	 * @param tname The name of the actor's graphics.
	 * @param sprites The array to fill with sprites.
	 */
	public static void getSprites(String tname, Sprite[][] sprites)
	{
		for(int row = 0; row < sprites.length; row++)
		{
			String fname = tname + LETTERS[row];
			for(int i = 0; i < DIRECTIONS.length; i++)
			{
				int fnum = DIRECTIONS[i];
				if(TextureManager.find(fname + fnum))
					sprites[row][fnum] = new Sprite(TextureManager.get(fname + fnum));
				else
					sprites[row][fnum] = new Sprite(TextureManager.get(fname + 0));
			}
		}
	}
	
	/**
	 * Fills the given array with sprites (no facing).
	 * @param tname The name of the actor's graphics.
	 * @param sprites The array to fill with sprites.
	 */
	public static void getSprites(String tname, Sprite[] sprites)
	{
		for(int row = 0; row < sprites.length; row++)
		{
			String fname = tname + LETTERS[row] + "0";
			sprites[row] = new Sprite(TextureManager.get(fname));
		}
	}
	
	

}
