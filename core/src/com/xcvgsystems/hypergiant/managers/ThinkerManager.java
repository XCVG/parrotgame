package com.xcvgsystems.hypergiant.managers;

import com.xcvgsystems.hypergiant.exceptions.ThinkerNotFoundException;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;
import com.xcvgsystems.hypergiant.thinkers.*;

/**
 * Provides Thinker services.
 * @author Chris
 *
 */
public class ThinkerManager {

	private final static int DEFAULT_LAYER = 2;
	private final static int DEFAULT_FACING = 0;
	
	/**
	 * Initialize the ThinkerManager.
	 */
	public static void init()
	{
		System.out.print("ThinkerManager.init...");
		
		System.out.println("done!");
	}
	
	/**
	 * Dispose of the ThinkerManager.
	 */
	public static void dispose()
	{
		System.out.print("ThinkerManager.dispose...");
		
		System.out.println("done!");
	}
	
	/**
	 * Create a new Thinker.
	 * @param tname the thinker to create
	 * @return the new Thinker
	 * @throws ThinkerNotFoundException if no matching Thinker was found
	 */
	public static Thinker makeThinker(ScrollScene context, String tname) throws ThinkerNotFoundException
	{
		return makeThinker(context, tname, 0, 0, DEFAULT_FACING);
	}
	
	/**
	 * Create a new Thinker
	 * @param tname the thinker to create
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param facing the direction to face
	 * @param layer the layer to be in
	 * @param id the Thinker id
	 * @param params any parameters
	 * @return the new Thinker
	 * @throws ThinkerNotFoundException if no matching Thinker was found
	 */
	public static Thinker makeThinker(ScrollScene context, String tname, int x, int y, int facing) throws ThinkerNotFoundException
	{	
		
		Thinker thinker;
		
		//else ifs because switching on strings didn't exist yet
		if(tname.equalsIgnoreCase("KamikazeBird"))
		{
			thinker = new KamikazeBird(x, y, context);
			
		}
		else if(tname.equalsIgnoreCase("KamikazeFalcon"))
		{
			thinker = new KamikazeFalcon(x, y, context);
			
		}
		else if(tname.equalsIgnoreCase("FlakTurret"))
		{
			thinker = new FlakTurret(x, y, context);
		}
		else if(tname.equalsIgnoreCase("FlakBullet"))
		{
			thinker = new FlakBullet(x, y, context);
		}
		else if(tname.equalsIgnoreCase("GunBullet"))
		{
			thinker = new GunBullet(x, y, context);
		}
		else if(tname.equalsIgnoreCase("InvisibleFlakTurret"))
		{
			thinker = new InvisibleFlakTurret(x, y, context);
		}
		else if(tname.equalsIgnoreCase("TreeBranch"))
		{
			thinker = new TreeBranch(x, y, context);
		}
		else if(tname.equalsIgnoreCase("PropBuilding"))
		{
			thinker = new PropBuilding(x, y, context);
		}
		else
		{
			throw new ThinkerNotFoundException();
		}
		
		thinker.setFacing(facing);
		
		return thinker;
		
	}
	

	
}
