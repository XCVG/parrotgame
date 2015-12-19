package com.xcvgsystems.hypergiant.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.exceptions.ThinkerNotFoundException;
import com.xcvgsystems.hypergiant.managers.*;
import com.xcvgsystems.hypergiant.overlays.NextMapOverlay;
import com.xcvgsystems.hypergiant.overlays.PauseOverlay;
import com.xcvgsystems.hypergiant.thinkers.*;

public class ScrollScene extends Scene {
	
	public static final int D_SCROLL_RATE = 4;
	public static final int D_BG_SCROLL_RATE = 2;
	public static final int D_FG_SCROLL_RATE = 4;
	public static final int D_FG_HEIGHT = 64;
	public static final int D_TIME = 60;
	
	public static final String D_NAME = "UNNAMED";
	public static final String D_MUSIC = "";	
	public static final String D_END_TEXT = "";
	public static final String D_NEXT = "MainMenu";
	
	public static final String D_BACKGROUND = "SCROLLBACK";
	public static final String D_FOREGROUND = "GRASSBACK";
	
	public final int GRAVITY = 8;
	public final int CULL_THRESHOLD = 64;
	public final int FG_HEIGHT;
	public final int WIDTH, HEIGHT;
	public final int SPAWN_CALL_RATE = 30;
	
	
	public final int SCROLL_RATE;
	public final int BG_SCROLL_RATE;
	public final int FG_SCROLL_RATE;
	public final int FLOOR_SPAWN_OFFSET;
	public final int TIME;
	
	public final String NAME;
	public final String MUSIC;	
	public final String END_TEXT;
	public final String NEXT;
	public final String NEXT_END;
	public final String SPECIAL;
	
	public final String BACKGROUND;
	public final String FOREGROUND;
	
	protected TextureRegion background;
	protected TextureRegion foreground;
	protected int bgPos;
	protected int fgPos;
	
	protected int timeElapsed;
	
	protected boolean ended;
	protected boolean spawning;
	protected String nextScene;
	
	protected Map<String, Integer> spawnList; //name and spawn rate
	
	protected List<Sprite>[] drawLists; //SEEMS LEGIT
	
	protected List<Thinker> thinkers;
	protected List<Thinker> newThinkers;
	PlayerPawn player;

	@SuppressWarnings("unchecked")
	public ScrollScene(String name, String music, int time, String background, String foreground, int fgheight, int bgscroll, int fgscroll, int speed, String next, String endnext, String endtext, String special, Map<String, Integer> spawnList)
	{
		WIDTH = EVars.WINDOW_X; //these are suboptimal I know
		HEIGHT = EVars.WINDOW_Y;
		
		NAME = name;
		MUSIC = music;
		NEXT = next;
		NEXT_END = endnext;
		END_TEXT = endtext;
		SPECIAL = special;
		SCROLL_RATE = speed;
		BG_SCROLL_RATE = bgscroll;
		FG_SCROLL_RATE = fgscroll;
		
		FLOOR_SPAWN_OFFSET = fgheight;
		BACKGROUND = background;
		FOREGROUND = foreground;
		TIME = time * EVars.TICK_RATE;
		
		this.spawnList = spawnList;
		
		//setup camera
		camera= new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(true, WIDTH, HEIGHT);

		//create draw lists
		drawLists = (List<Sprite>[]) new List[8]; //this is terrible
		for(int i = 0; i < drawLists.length; i++)
		{
			drawLists[i] = new ArrayList<Sprite>();
		}

		
		
		//System.err.println(SPECIAL);
		
		if(SPECIAL != null)
		{
			if(SPECIAL.equalsIgnoreCase("drawforeground"))
			{
				FG_HEIGHT = HEIGHT;
			}
			else
			{
				FG_HEIGHT = fgheight;
			}
		}
		else
		{
			FG_HEIGHT = fgheight;
		}
	}
	
	public ScrollScene() {
		
		this(D_NAME,D_MUSIC,D_TIME,D_BACKGROUND,D_FOREGROUND, D_FG_HEIGHT, D_BG_SCROLL_RATE, D_FG_SCROLL_RATE, D_SCROLL_RATE, D_NEXT, SceneManager.SCENE_END, D_END_TEXT, null, new HashMap<String, Integer>());
		
		//create default spawns
		spawnList.put("FlakTurret", 100);
		spawnList.put("KamikazeBird", 150);

	}

	@Override
	public void enter() {
		
		//create thinker list
		thinkers = new ArrayList<Thinker>();
		newThinkers = new ArrayList<Thinker>();	
		
		SceneManager.setCurrentOverlay(SceneManager.OVERLAY_DEFAULT_HUD); //TODO: make this less messy
		
		timeElapsed = 0;
		
		//init spritebatch
		batch = new SpriteBatch();
		
		//init background
		background = TextureManager.get(BACKGROUND);
		foreground = TextureManager.get(FOREGROUND);
		//background.setBounds(0, 0, WIDTH, HEIGHT);
		
		//create playerpawn
		player = new PlayerPawn(WIDTH / 2, HEIGHT / 2, this);
		thinkers.add(player);
		
		//init music
		MusicManager.play(MUSIC, true);
		
		spawning = true;
		ended = false;
		
		//TODO: handle SPAWNONCE thinkers

	}

	@Override
	public void tick() {
		
		//handle pause (TODO: move this to a smarter spot)
		if(!ended && InputManager.isEscJustPressed())
		{
			SceneManager.setCurrentOverlay(new PauseOverlay(), 2);
			SceneManager.setRunning(false);
			return;
		}
		
		//handle map specials
		if(SPECIAL != null && !ended)
		{
			
			//MAP08 endmap special
			if(SPECIAL.equalsIgnoreCase("endmap"))
			{
				//deal with player death
				//if(StateManager.getPlayer().getHp() <= 0)
				if(player.getState().equals(ActorState.DEAD))
				{
					if(timeElapsed > 3600)
					{
						//player is dead after end threshold
						//SceneManager.changeScene("EndMiddle");
						SceneManager.setCurrentOverlay(new NextMapOverlay("The End", "EndMiddle"), 1);
						ended = true;
						//SceneManager.setRunning(false);
						return;
					}
					else
					{
						//player is dead before end threshold
						SceneManager.setCurrentOverlay(new NextMapOverlay("The End", "EndBad"), 1);
						ended = true;
						//SceneManager.setRunning(false);
						//SceneManager.changeScene("EndBad");
						return;
					}
				}
			}					
		}
		
		//tick timeElapsed and transition to next scene on completion
		if(!ended)
		{
			timeElapsed++;
			
			if(timeElapsed > TIME)
			{
				//SceneManager.changeScene(NEXT);
				SceneManager.setCurrentOverlay(new NextMapOverlay(END_TEXT, NEXT), 1);
				SceneManager.setRunning(false);
				ended = true;
			}
		}
			
		
		//check if player is dead and change map
		if(player.isEnabled() && player.getState().equals(ActorState.DEAD)) // && StateManager.getPlayer().getHp() <= 0)
		{
			//SceneManager.changeScene(NEXT_END);
			SceneManager.setCurrentOverlay(new NextMapOverlay("You died.",NEXT_END), 1);
			//SceneManager.setRunning(false);
			player.setEnabled(false);
			ended = true;
		}
		
		//scroll background and foreground
		bgPos -= BG_SCROLL_RATE;
		if(bgPos < 0)
			bgPos = WIDTH;

		fgPos -= FG_SCROLL_RATE;
		if(fgPos < 0)
			fgPos = WIDTH;

		//clear the draw lists
		for(int i = 0; i < drawLists.length; i++)
		{
			drawLists[i].clear();
		}

		//cull dead thinkers
		Iterator<Thinker> tIt = thinkers.iterator();
		while(tIt.hasNext())
		{
			
			Thinker t = tIt.next();
			
			if(!t.isEnabled())
				tIt.remove();
			
			//no, it doesn't check flags like it should
			//TODO: check flags
			//happy?
			if(t.getX() < (0 - CULL_THRESHOLD) || t.getX() > (WIDTH + CULL_THRESHOLD) || t.getY() < (0 - CULL_THRESHOLD) || t.getY() > (HEIGHT + CULL_THRESHOLD))
			{
				tIt.remove();
				//System.err.println("CULL (SCREEN) - " + t);
			}
			
		}
		
		//TODO: cull thinkers that have left the screen
		
		//spawn new thinkers
		if(SceneManager.getTicks() % SPAWN_CALL_RATE == 0)
			spawnThinkers();
		
		//tick all thinkers
		for(Thinker t : thinkers)
		{
			if(t instanceof TTickable && t.isEnabled())
				((TTickable)t).tick();
		}
		
		//hit all thinkers
		//this is horrible
		//Very slow. I have some ideas for optimization but it's too early to try implementing  them.
		for(Thinker t1 : thinkers)
		{
			if(t1 instanceof THittable && t1.isSolid())
			{
				for(Thinker t2 : thinkers)
				{
					if(t2 instanceof THittable && t2.isSolid())
					{
						if(t1 != t2)
						{
							//if(t1.getX() == t2.getX() && t1.getY() == t2.getY()) //TODO: fix this code; it's designed for tiles!
							if( ( Math.abs((t1.getX() - t2.getX())) <= ((THittable)t1).getRADIUS() + ((THittable)t2).getRADIUS()) && ( Math.abs((t1.getY() - t2.getY())) <= ((THittable)t1).getRADIUS() + ((THittable)t2).getRADIUS()) )
							{
								((THittable) t1).hit(t2);
								//System.err.println("HIT " + t1 + " " + t2);
								//System.err.println(t1.getX() - t2.getX());
							}
						}
					}
				}
			}
		}
		
		//add new thinkers to main list
		for(Thinker t : newThinkers)
		{
			thinkers.add(t);
		}
		newThinkers.clear();

	}

	@Override
	public void draw() {
		
		//setup draw
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		//draw background
		
		batch.draw(background, bgPos, 0, WIDTH, HEIGHT);
		batch.draw(background, bgPos - WIDTH, 0, WIDTH, HEIGHT);
		
		//background.setPosition(bgPos, 0);
		//background.draw(batch);
		batch.flush();
		//background.setPosition(bgPos, 0);
		//background.draw(batch);
		
		//don't you just love nested loops?
		for(int i = 0; i < drawLists.length; i++)
		{
			//draw the list			
			
			for(Sprite spr : drawLists[i])
			{
				spr.draw(batch);
			}
			
			batch.flush();
		}
		
		//draw foreground
		batch.draw(foreground, fgPos, HEIGHT - FG_HEIGHT, WIDTH, FG_HEIGHT);
		batch.draw(foreground, fgPos - WIDTH, HEIGHT - FG_HEIGHT, WIDTH, FG_HEIGHT);
		
		batch.end();

	}

	@Override
	public void exit() {
		
		//destroy player
		thinkers.remove(player);
		player = null;		
		
		//SceneManager.setCurrentOverlay(null);
		SceneManager.clearCurrentOverlays();
		
		batch.dispose();
		batch = null;
		
		MusicManager.stop();
		
	}
	
	protected void spawnThinkers()
	{
		
		if(!spawning)
			return;
		
		Iterator<Entry<String, Integer>> sIt = spawnList.entrySet().iterator();
		while(sIt.hasNext())
		{
			Entry<String, Integer> entry = sIt.next();
			
			String name = entry.getKey();
			int chance = entry.getValue();
			if(MathUtils.random(255) < chance)
			{
				//gross hacks caused by bad design decisions
				//don't do this at home, kids
				try
				{
					Thinker t = ThinkerManager.makeThinker(this, name);
					t.setX(WIDTH);
					if( t instanceof ActorThinker && ((ActorThinker) t).checkFlag(ActorFlag.SPAWNFLOOR))
					{
						t.setX(WIDTH);
						t.setY(HEIGHT - FLOOR_SPAWN_OFFSET - ((ActorThinker)t).getRADIUS());
					}
					else
					{
						t.setX(WIDTH);
						t.setY(MathUtils.random(16, HEIGHT-FLOOR_SPAWN_OFFSET));
					}
					t.setFacing(3);
					
					thinkers.add(t);
					break;
				}
				catch (ThinkerNotFoundException e)
				{					
					//FOR DEBUGGING ONLY
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//technically this should be "add sprite to draw list"
	public void drawSprite(Sprite spr, int layer)
	{
		drawLists[layer].add(spr);
	}
	
	//technically just adds a thinker
	public void addThinker(Thinker t)
	{
		newThinkers.add(t);
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getSCROLL_RATE() {
		return SCROLL_RATE;
	}
	
	public int getGRAVITY() {
		return GRAVITY;
	}
	
	public PlayerPawn getPlayer(){
		return player;
	}

	public int getTIME() {
		return TIME;
	}
	
	public int getTimeElapsed()	{
		return timeElapsed;
	}
	
	
	
	

}
