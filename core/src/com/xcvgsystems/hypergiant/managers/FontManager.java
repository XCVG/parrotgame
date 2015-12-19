package com.xcvgsystems.hypergiant.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.GVars;
import com.xcvgsystems.hypergiant.scenes.Scene;

/**
 * Provides font services.
 * @author Chris
 *
 */
public class FontManager {

	
	private static BitmapFont DEFAULT_FONT;
	
	static Map<String, BitmapFont> fonts;
	
	/**
	 * Initialize the font manager, loading game fonts.
	 */
	public static void init()
	{
		System.out.print("FontManager.init...");

		
		
		FileHandle baseFontPath = Gdx.files.internal(EVars.BASE_PATH + "/fonts/");
		
		DEFAULT_FONT = new BitmapFont(baseFontPath.child("DEFAULT.fnt"),true);
		
		fonts = new HashMap<String, BitmapFont>();
		
		FileHandle gamePath = Gdx.files.internal(GVars.GAME_PATH);
		FileHandle fontPath = gamePath.child("fonts");
		loadAll(fontPath);
		
		System.out.println("done!");
	}
	
	/**
	 * Dispose of the font manager, unloading game fonts.
	 */
	public static void dispose()
	{
		System.out.print("FontManager.dispose...");

		Set<Map.Entry<String,BitmapFont>> entrySet = fonts.entrySet();
		Iterator<Entry<String, BitmapFont>> it = entrySet.iterator();
		
		while(it.hasNext())
		{
			it.next().getValue().dispose();
			it.remove();
		}
		
		DEFAULT_FONT.dispose();
		DEFAULT_FONT = null;
		fonts = null;
		
		System.out.println("done!");
	}
	
	/**
	 * Load a font into the FontManager.
	 * @param name The name of the font to use.
	 * @param font The font file.
	 */
	public static void load(String name, FileHandle font)
	{
		
		name = name.toUpperCase(Locale.ROOT);
		
		BitmapFont myFont = new BitmapFont(font, true);
		fonts.put(name, myFont);
	}

	/**
	 * Load all fonts in a directory.
	 * @param dir The directory containing the fonts.
	 */
	public static void loadAll(FileHandle dir)
	{
		FileHandle[] files = dir.list();
		
		for(FileHandle file : files)
		{
			if(file.extension().equalsIgnoreCase("fnt"))
				load(file.nameWithoutExtension(), file);
		}
	}

	/*
	public static void writeInto(String text, String font, Color color, Batch batch, float x, float y)
	{
		BitmapFont myFont = get(font);
		myFont.setColor(Color.);
		myFont.draw(batch, text, x, y);
	}
	*/
	
	/**
	 * Get a font from the manager.
	 * @param font The name font to get.
	 * @return The font to get, or the default font if it doesn't exist.
	 */
	public static BitmapFont get(String font)
	{
		if(font == null)
			return DEFAULT_FONT;
		
		font = font.toUpperCase(Locale.ROOT);
		
		if(fonts.containsKey(font))
			return fonts.get(font);
		else return DEFAULT_FONT;
	}

	/**
	 * Write some text to the screen using the default font.
	 *  (nearly useless because of the way drawing works)
	 * 
	 * @param text The text to write.
	 * @param x The x coordinate to start drawing at.
	 * @param y The y coordinate to start drawing at.
	 */
	public static void write(String text, float x, float y)
	{
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SpriteBatch batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		DEFAULT_FONT.draw(batch, text, x, y);
		batch.end();
		batch.dispose();
	}
	
	/**
	 * Write some text into a specified Batch using the default font.
	 * 
	 * @param text The text to write.
	 * @param batch The Batch to draw into.
	 * @param x The x coordinate to start drawing at.
	 * @param y The y coordinate to start drawing at.
	 */
	public static void writeInto(String text, Batch batch, float x, float y)
	{
		DEFAULT_FONT.draw(batch, text, x, y);
	}
	
	/**
	 * Write some text into a specified Batch using a specified font.
	 * @param text The text to write.
	 * @param font The font to use.
	 * @param batch The Batch to draw into.
	 * @param x The x coordinate to start drawing at.
	 * @param y The y coordinate to start drawing at.
	 */
	public static void writeInto(String text, String font, Batch batch, float x, float y)
	{
		BitmapFont myFont = get(font.toUpperCase(Locale.ROOT));
		myFont.draw(batch, text, x, y);
	}
	
	/**
	 * Write some text into a specified Batch using a specified font, centered.
	 * @param text The text to write.
	 * @param font The font to use.
	 * @param batch The Batch to draw into.
	 * @param x The x coordinate to start drawing at.
	 * @param y The y coordinate to start drawing at.
	 */
	public static void writeCentered(String text, String font, Batch batch, float x, float y)
	{
		GlyphLayout layout = new GlyphLayout();
		BitmapFont myFont = get(font.toUpperCase(Locale.ROOT));
		
		layout.setText(myFont, text);
		
		myFont.draw(batch, text, x - layout.width / 2, y - layout.height / 2);
	}
	
	/*
	public static void writeInto(String text, String font, Color color, Batch batch, float x, float y)
	{
		BitmapFont myFont = get(font);
		myFont.setColor(Color.);
		myFont.draw(batch, text, x, y);
	}
	*/
	




	
	
}
