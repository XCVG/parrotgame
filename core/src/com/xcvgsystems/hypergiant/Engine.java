package com.xcvgsystems.hypergiant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Gdx;
import com.xcvgsystems.hypergiant.managers.*;
import com.xcvgsystems.hypergiant.scenes.*;

/**
 * Engine class defines basic static engine things.
 * @author Chris
 *
 */
public class Engine {
	
	private static BufferedReader keyboard;
	private static long time;

	public static void init()
	{
		
		System.out.print("Engine.init");
		System.out.println("...");
		
		if(Gdx.app.getType() == ApplicationType.Desktop)
			keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//initialize components
		InputManager.init();
		
		MusicManager.init();
		
		SoundManager.init();	
		
		TextureManager.init();
		
		FontManager.init();
		
		ThinkerManager.init();
		
		ModelManager.init();
		
		SceneManager.init();
		
		StateManager.init();
		
		System.out.print("...");
		System.out.println("done!");		
		
	}
	
	public static void start()
	{
		time = TimeUtils.millis();
		
		//logger = new FPSLogger();
		
		SceneManager.setDrawing(true);
		SceneManager.setRunning(true);
		
		System.out.println("Engine Startup!");
	}
	
	public static void run()
	{
		// loop here(?!)
		if(EVars.DEBUG)
			parseConsole();

		InputManager.poll();

		//30TPS tickrate limiting hopefully
		//There's something wrong with this.
		//fixed now
		if(TimeUtils.timeSinceMillis(time) >= EVars.TICK_DELAY)
		{
			time = TimeUtils.millis();

			//System.err.println("TICK");
			SceneManager.tick();

		}

		SceneManager.draw();

		//System.err.println("DRAW");
		//logger.log();
	}
	
	public static void dispose()
	{
		
		System.out.print("Engine.dispose");
		System.out.println("...");
		
		if(keyboard != null)
			try
			{
				keyboard.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		keyboard = null;
		
		StateManager.dispose();
	
		SceneManager.dispose();	
		
		ModelManager.dispose();
		
		ThinkerManager.dispose();
		
		FontManager.dispose();
		
		TextureManager.dispose();
		
		SoundManager.dispose();
		
		MusicManager.dispose();
			
		InputManager.dispose();
		
		System.out.print("...");
		System.out.println("done!");
		
	}
	
	public static void parseConsole()
	{
		if(keyboard == null)
			return;
		
		try
		{
			if(keyboard.ready())
			{		
				String[] cmd = keyboard.readLine().trim().split("\\s+");
				
				if(cmd.length <= 0)
					return;
				
				String command = cmd[0];
				
				//System.err.println(command);
				
				if(command.equalsIgnoreCase("map"))
				{
					//System.err.println(cmd[1]);
					SceneManager.changeScene(cmd[1]);
				}
				else if(command.equalsIgnoreCase("listtex"))
				{
					System.out.println(TextureManager.listTextures());
				}
				else if(command.equalsIgnoreCase("echo"))
				{
					FontManager.write(cmd[1], 320, 240);
				}
				else if(command.equalsIgnoreCase("exit"))
				{
					Gdx.app.exit();
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
