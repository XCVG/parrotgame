package com.xcvgsystems.hypergiant.managers;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.GVars;
import com.xcvgsystems.hypergiant.managers.TextureManager.NumberingType;

/**
 * Integrated texture manager.
 * @author Chris
 *
 */
public class TextureManager {

	//TODO: caching and better handling of context is coming later

	private final static String[] colornames = new String[] {"!BLACK", "!BLUE", "!GREEN", "!CYAN", "!RED", "!MAGENTA", "!BROWN", "!LIGHTGRAY", "!GRAY", "!LIGHTBLUE", "!LIGHTGREEN", "!LIGHTCYAN", "!LIGHTRED", "!LIGHTMAGENTA", "!YELLOW", "!WHITE"};

	/**
	 * Numbering types for loading atlases and such.
	 * @author Chris
	 *
	 */
	public enum NumberingType
	{
		DECIMAL_XY, DECIMAL_SEQUENTIAL, HEX_XY, HEX_SEQUENTIAL
	}

	//nonexistent texture
	private static TextureRegion NULL_TEX;
	
	private static FileHandle BASE_TEX_PATH; 
	
	/**
	 * ALL the textures!
	 */
	static Map<String, TextureRegion> textures;
	
	/**
	 * Base/raw textures that are loaded.
	 */
	static Map<String, Texture> rawtextures;
	
	public static void init()
	{
		System.out.print("TextureManager.init...");
		
		BASE_TEX_PATH = Gdx.files.internal(EVars.BASE_PATH + "/textures/");
		FileHandle gamePath = Gdx.files.internal(GVars.GAME_PATH);
		
		rawtextures = new HashMap<String, Texture>();
		textures = new HashMap<String, TextureRegion>();
		
		//load null (transparent) and colour textures
		initColors();
		
		//load all raw textures in the patches folder
		FileHandle patchesPath = gamePath.child("patches");
		loadRaw(patchesPath);
		
		//load all textures defined in the TEXTURES file
		loadTextures(gamePath.child("data").child("TEXTURES.json"));
		
		//load all textures in teh textures folder and sprites folder		
		FileHandle texturesPath = gamePath.child("textures");
		FileHandle spritesPath = gamePath.child("sprites");
		loadAll(texturesPath);
		loadSprites(spritesPath);
		
		if(EVars.DEBUG)
			System.out.println(listTextures());
			
		System.out.println("done!");
	}
	
	private static void initColors()
	{
		//handle NULL_TEX
		Texture nt;
		
		nt = new Texture(BASE_TEX_PATH.child("NULLTEX.png"));
		rawtextures.put("NULLTEX", nt);
		NULL_TEX = new TextureRegion(nt);
		NULL_TEX.flip(false, true);
		
		//handle special !NULL
		nt = new Texture(BASE_TEX_PATH.child("TRANTEX.png"));
		textures.put("!NULL", new TextureRegion(nt));
		
		//TODO replace this with loading from a file
		
		//load colours!
		
		Texture ct = new Texture(BASE_TEX_PATH.child("COLORTEX.png"));
		rawtextures.put("COLORTEX", ct);
		
		for(int i = 0; i < colornames.length; i++)
		{
			textures.put(colornames[i], new TextureRegion(ct,i,0,1,1));
		}		
		
	}
	
	//TODO: load methods!
	
	/**
	 * Texture load method. Loads a libGDX Texture.
	 * @param name The name of the texture.
	 * @param texture The texture (as Texture).
	 */
	public static void load(String name, Texture texture)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			TextureRegion myTex = new TextureRegion(texture);
			myTex.flip(false, true);		
			textures.put(name, myTex);
		} catch (Exception e) {
			System.err.println("Failed to load " + name);
		}
	}
	
	/**
	 * Texture load method. Loads a libGDX TextureRegion.
	 * @param name The name of the texture.
	 * @param texture The texture (as TextureRegion).
	 */
	public static void load(String name, TextureRegion texture)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			TextureRegion myTex = new TextureRegion(texture);
			myTex.flip(false, true);		
			textures.put(name, myTex);
		} catch (Exception e) {
			System.err.println("Failed to load " + name);
		}
	}
	
	/**
	 * Texture load method. Loads a libGDX Pixmap.
	 * @param name The name of the texture.
	 * @param image The texture (as Pixmap).
	 */
	public static void load(String name, Pixmap image)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			TextureRegion myTex = new TextureRegion(new Texture(image));
			myTex.flip(false, true);		
			if(EVars.FILTER)
				myTex.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textures.put(name, myTex);
		} catch (Exception e) {
			System.err.println("Failed to load " + name);
		}
	}
	
	/**
	 * Texture load method. Loads from a libGDX FileHandle.
	 * @param name The name of the texture.
	 * @param file The texture (from FileHandle).
	 */
	private static void load(String name, FileHandle file)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			TextureRegion myTex = new TextureRegion(new Texture(file));
			myTex.flip(false, true);
			if(EVars.FILTER)
				myTex.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textures.put(name, myTex);
		} catch (Exception e) {
			System.err.println("Failed to load " + name);
		}
	}
		
	/**
	 * Loads a texture region.
	 * @param name The name of the texture.
	 * @param baseTex The base texture to use.
	 * @param x The x coordinate of the texture region.
	 * @param y The y coordinate of the texture region.
	 * @param width The width of the texture region.
	 * @param height The height of the texture region.
	 */
	public static void loadRegion(String name, Texture baseTex, int x, int y, int width, int height)
	{
		
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			TextureRegion myTex = new TextureRegion(baseTex, x, y, width, height);
			myTex.flip(false, true);
			if(EVars.FILTER)
				myTex.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			textures.put(name, myTex);
		} catch (Exception e) {
			System.err.println("Failed to load " + name);
		}
	}
	
	/**
	 * Loads a texture atlas from a base texture
	 * @param prefix The base name of the texture
	 * @param baseTex The texture to cut up.
	 * @param width The width of each tile
	 * @param height The height of each tile
	 * @param type The numbering type to use.
	 */
	public static void loadAtlas(String prefix, Texture baseTex, int width, int height, NumberingType type)
	{
		//a hack to make the texture names look right
		prefix = prefix + "_";
		
		//split the texture
		TextureRegion[][] alltex = TextureRegion.split(baseTex, width, height);
		
		if(type == NumberingType.DECIMAL_SEQUENTIAL)
		{
			DecimalFormat df = new DecimalFormat("000");
			
			int i = 0;
			for(TextureRegion[] row : alltex)
			{
				for(TextureRegion tex : row)
				{
					tex.flip(false, true);
					textures.put((prefix + df.format(i)).toUpperCase(Locale.ROOT), tex);
					i++; //I don't trust Java increment operators
				}
			}
		}
		else if(type == NumberingType.HEX_SEQUENTIAL)
		{
			int i = 0;
			for(TextureRegion[] row : alltex)
			{
				for(TextureRegion tex : row)
				{
					tex.flip(false, true);
					textures.put((prefix + Integer.toHexString(i)).toUpperCase(Locale.ROOT), tex);
					i++; //I don't trust Java increment operators
				}
			}
		}
		else if(type == NumberingType.DECIMAL_XY)
		{
			DecimalFormat df = new DecimalFormat("00");
			
			for(int x = 0; x < alltex.length; x++)
			{
				TextureRegion[] row = alltex[x];
				
				for(int y = 0; y < row.length; y++)
				{
					TextureRegion tex = row[y];
					tex.flip(false, true);
					textures.put((prefix + df.format(x) + df.format(y)).toUpperCase(Locale.ROOT), tex);
				}
			}
		}
		else if(type == NumberingType.HEX_XY)
		{
			for(int x = 0; x < alltex.length; x++)
			{
				TextureRegion[] row = alltex[x];
				
				for(int y = 0; y < row.length; y++)
				{
					TextureRegion tex = row[y];
					//System.err.println(prefix + Integer.toHexString(x) + Integer.toHexString(y));
					tex.flip(false, true);
					textures.put((prefix + Integer.toHexString(x) + Integer.toHexString(y)).toUpperCase(Locale.ROOT), tex);
				}
			}
		}
	}
	
	/**
	 * Atlas load method, loads from a libGDX FileHandle.
	 * @param prefix The base name of the texture
	 * @param file The texture (from FileHandle)
	 * @param width The width of each tile
	 * @param height The height of each tile
	 * @param type The numbering type to use.
	 */
	public static void loadAtlas(String prefix, FileHandle file, int width, int height, NumberingType type)
	{
		loadAtlas(prefix, new Texture(file), width, height, type);
	}
	
	/**
	 * Loads a texture atlas from a base texture, calculating tile size automatically
	 * @param prefix The base name of the texture
	 * @param baseTex The texture to cut up.
	 * @param numx The number of tiles horizontally
	 * @param numy The number of tiles vertically
	 * @param type The numbering type to use.
	 */
	public static void loadAtlasNum(String prefix, Texture baseTex, int numx, int numy, NumberingType type)
	{
		int width = baseTex.getWidth() / numx;
		int height = baseTex.getHeight() / numy;
		
		loadAtlas(prefix, baseTex, width, height, type);
	}
	
	/**
	 * Atlas load method, loads from a libGDX FileHandle.
	 * @param prefix The base name of the texture
	 * @param file The texture (from FileHandle)
	 * @param numx The number of tiles horizontally
	 * @param numy The number of tiles vertically
	 * @param type The numbering type to use.
	 */
	public static void loadAtlasNum(String prefix, FileHandle file, int numx, int numy, NumberingType type)
	{
		loadAtlasNum(prefix, new Texture(file), numx, numy, type);
	}	
	
	/**
	 * Sprite (4 dir) load method. Loads from a libGDX FileHandle.
	 * @param name The name of the texture.
	 * @param file The texture (from FileHandle).
	 */
	public static void loadSprite4(String prefix, FileHandle file)
	{
		prefix = prefix.toUpperCase(Locale.ROOT);
		
		Texture tex = new Texture(file);
		if(EVars.FILTER)
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		
		int regionWidth = tex.getWidth() / 3;
		int regionHeight = tex.getHeight() / 3;
		
		//TODO: cleanup/refactor
		TextureRegion up = new TextureRegion(tex, regionWidth, 0, regionWidth, regionHeight);
		textures.put(prefix + "5", up);
		TextureRegion down = new TextureRegion(tex, regionWidth, regionHeight * 2, regionWidth, regionHeight);
		textures.put(prefix + "1", down);
		TextureRegion left = new TextureRegion(tex, 0, regionHeight, regionWidth, regionHeight);
		textures.put(prefix + "3", left);
		TextureRegion right = new TextureRegion(tex, regionWidth * 2, regionHeight, regionWidth, regionHeight);
		textures.put(prefix + "7", right);
		
	}
	
	/**
	 * Sprite (8 dir/center) load method. Loads from a libGDX FileHandle.
	 * @param name The name of the texture.
	 * @param file The texture (from FileHandle).
	 */
	public static void loadSprite9(String prefix, FileHandle file)
	{
		prefix = prefix.toUpperCase(Locale.ROOT);
		
		Texture tex = new Texture(file);
		if(EVars.FILTER)
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		
		int regionWidth = tex.getWidth() / 3;
		int regionHeight = tex.getHeight() / 3;
		
		//TODO: cleanup/refactor
		TextureRegion mid = new TextureRegion(tex, regionWidth, regionHeight, regionWidth, regionHeight);
		textures.put(prefix + "0", mid);
		
		TextureRegion up = new TextureRegion(tex, regionWidth, 0, regionWidth, regionHeight);
		textures.put(prefix + "5", up);
		TextureRegion down = new TextureRegion(tex, regionWidth, regionHeight * 2, regionWidth, regionHeight);
		textures.put(prefix + "1", down);
		TextureRegion left = new TextureRegion(tex, 0, regionHeight, regionWidth, regionHeight);
		textures.put(prefix + "3", left);
		TextureRegion right = new TextureRegion(tex, regionWidth * 2, regionHeight, regionWidth, regionHeight);
		textures.put(prefix + "7", right);
		
		TextureRegion ul = new TextureRegion(tex, 0, 0, regionWidth, regionHeight);
		textures.put(prefix + "4", ul);
		TextureRegion ur = new TextureRegion(tex, regionWidth * 2, 0, regionWidth, regionHeight);
		textures.put(prefix + "6", ur);
		TextureRegion dl = new TextureRegion(tex, 0, regionHeight * 2, regionWidth, regionHeight);
		textures.put(prefix + "2", dl);
		TextureRegion dr = new TextureRegion(tex, regionWidth * 2, regionHeight * 2, regionWidth, regionHeight);
		textures.put(prefix + "8", dr);
		
	}
	
	/**
	 * Loads all files in a directory
	 * @param dir The directory to look in.
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
	 * Loads all files in a directory as sprites
	 * @param dir The directory to look in.
	 */
	public static void loadSprites(FileHandle dir)
	{
		FileHandle[] files = dir.list();
		
		for(FileHandle file : files)
		{
			String name = file.nameWithoutExtension();
			
			if(name.endsWith("$"))
			{
				loadSprite4(name.substring(0, name.length() - 1), file);
			}
			else if(name.endsWith("#"))
			{
				loadSprite9(name.substring(0, name.length() - 1), file);
			}
			else
			{
				load(file.nameWithoutExtension(), file);
			}
		}
	}
	
	/**
	 * Loads all files in a directory as raw textures.
	 * @param dir The directory to look in.
	 */
	public static void loadRaw(FileHandle dir)
	{
		FileHandle[] files = dir.list();
		
		for(FileHandle file : files)
		{
			Texture tex = new Texture(file);
			if(EVars.FILTER)
				tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			rawtextures.put(file.nameWithoutExtension().toUpperCase(Locale.ROOT), tex);
		}
	}
	
	/**
	 * Loads textures based on a TEXTURES file.
	 * @param texdef FileHandle of the TEXTURES.json file
	 */
	public static void loadTextures(FileHandle texdef)
	{
		//System.out.println(texdef.readString());
		String text = texdef.readString();
		
		JsonValue root = new JsonReader().parse(text);
		for(JsonValue child : root)
		{
			//Pokemon exception handling
			try
			{
				//System.out.println(child.toString());
				//these *should be* texture definitions
				//System.out.println(child.getString("type"));
				String type = child.getString("type");
				if(type.equalsIgnoreCase("atlas"))
				{
					//if it's of type atlas, get name, file, and numbering type
					String name = child.getString("name");
					String base = child.getString("file");
					Texture basetex = rawtextures.get(base.toUpperCase(Locale.ROOT));
					
					//decode numbering type
					NumberingType ntype = NumberingType.valueOf(child.getString("numbering").toUpperCase(Locale.ROOT)); 
					
					//if it contains width node, split on sized tiles
					if(child.has("width"))
					{
						int width = child.getInt("width");
						int height = child.getInt("height");				
						
						loadAtlas(name,basetex,width,height,ntype);
					}
					else if(child.has("numx"))
					{
						int numx = child.getInt("numx");
						int numy = child.getInt("numy");
						
						loadAtlasNum(name,basetex,numx,numy,ntype);
					}
					//if it contains numx node, split on number of tiles
					else
					{
						System.err.println("Invalid TEXTURES entry");
					}
				}
				else if(type.equalsIgnoreCase("texture"))
				{
					String name = child.getString("name");
					String base = child.getString("file");
					Texture basetex = rawtextures.get(base.toUpperCase(Locale.ROOT));
					int width = child.getInt("width");
					int height = child.getInt("height");
					int x = child.getInt("x");
					int y = child.getInt("y");
					
					loadRegion(name,basetex,x,y,width,height);
				}
				else
				{
					System.err.println("Unrecognized texture definition: type " + type);
				}
			}
			catch(Exception e)
			{
				System.err.println("Failed to load texture in TEXTURES file!");
				if(EVars.DEBUG)
					e.printStackTrace();
			}
		}
	}
	
	/**
	 * Removes a TextureRegion and disposes of the underlying Texture object
	 */
	public static void dispose(String name)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		try {
			textures.remove(name).getTexture().dispose();
		} catch (Exception e) {
			System.err.println("Failed to remove " + name);
		}
	}
	
	/**
	 * Gets a texture (technically a TextureRegion).
	 * @param name The texture to get.
	 * @return The texture to get.
	 */
	public static TextureRegion get(String name)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		//return textures.getOrDefault(name, NULL_TEX);
		
		TextureRegion tex = textures.get(name);
		if(tex == null)
			return NULL_TEX;
		
		return tex;
		
	}
	
	/**
	 * Finds a texture (technically a TextureRegion).
	 * @param name The texture to get.
	 * @return Whether the texture exists or not
	 */
	public static boolean find(String name)
	{
		name = name.toUpperCase(Locale.ROOT);
		
		//return textures.getOrDefault(name, NULL_TEX);

		return textures.containsKey(name);
		
	}
	
	/**
	 * Returns a list of all textures.
	 * @return a list of all textures
	 */
	public static String listTextures()
	{
		String str = "";
		
		Set<Map.Entry<String,TextureRegion>> entrySet = textures.entrySet();
		Iterator<Entry<String, TextureRegion>> it = entrySet.iterator();
		
		while(it.hasNext())
		{
			//str += it.next().toString() + "\n";
			
			str += it.next().getKey() + "\n";
		}
		
		return str;
	}
	
	/**
	 * Clears the list of textures and disposes of all underlying Texture objects.
	 */
	public static void dispose()
	{

		System.out.print("TextureManager.dispose...");

		//deal with processed textures
		Set<Map.Entry<String,TextureRegion>> entrySet = textures.entrySet();
		Iterator<Entry<String, TextureRegion>> it = entrySet.iterator();
		
		while(it.hasNext())
		{
			it.next().getValue().getTexture().dispose();
			it.remove();
		}
		
		//deal with raw textures
		Set <Map.Entry<String, Texture>> entrySet2 = rawtextures.entrySet();
		Iterator<Entry<String, Texture>> it2 = entrySet2.iterator();
		
		while(it2.hasNext())
		{
			Texture t = it2.next().getValue();
			try
			{
				t.dispose();
			}
			catch(Exception e)
			{
				
			}
			it2.remove();
		}
		
		System.out.println("done!");
	}
	




}
