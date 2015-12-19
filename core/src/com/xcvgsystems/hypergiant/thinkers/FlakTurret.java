package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.xcvgsystems.hypergiant.exceptions.ThinkerNotFoundException;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.SoundManager;
import com.xcvgsystems.hypergiant.managers.ThinkerManager;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;
import com.xcvgsystems.hypergiant.utils.SpriteHelper;

public class FlakTurret extends ActorThinker {

	final protected ScrollScene CONTEXT;
	
	final String BASE_NAME = "Flak";
	final String GUN_NAME = "FlakB";
	final String MISSILE = "FlakBullet";
	
	final int RADIUS = 32;
	final int DRAW_LAYER = 3;
	final int DRAW_WIDTH = 64;
	final int DRAW_HEIGHT = 64;
	final int GUN_OFFSET = 16;
	final int DEATH_FRAMES = 5;
	final int FIRE_FRAMES = 5;
	final int FIRE_RATE = 30; // every x tics
	final int FIRE_CHANCE = 10; //guaranteed to fire
	final int FIRE_VELOCITY = 10;
	final int FIRE_GROSS_HACK = 1;
	final float FIRE_JITTER = 0.1f;
	
	protected int animationFrame; //frame count for any animation
	//protected int stateFrames; //frames for this state; meant for walking

	//protected float angle; //angle the turret is pointed at
	
	protected Sprite[] baseSprites = new Sprite[8]; //rows: ABCDEFGH
	protected Sprite[] gunSprites = new Sprite[8]; //rows: ABCDEFGH
	
	
	public FlakTurret(int x, int y, ScrollScene context) {
		this.CONTEXT = context;
		
		flags = EnumSet.of(ActorFlag.SPAWNFLOOR, ActorFlag.ISMONSTER);
		
		this.x = x;
		this.y = y;
		this.facing = 0;
		this.solid = true;
		
		
		SpriteHelper.getSprites(BASE_NAME, baseSprites);
		SpriteHelper.getSprites(GUN_NAME, gunSprites);
				
		velocityX = (CONTEXT.getSCROLL_RATE()) * -1;
		
		this.state = ActorState.IDLING;
	}
	
	@Override
	public void tick() {
		
		Sprite currentBaseSprite = baseSprites[0];
		Sprite currentGunSprite = gunSprites[0]; //more fault tolerant
		
		int currentFrame;
		
		x += velocityX;
		y += velocityY;
		
		switch(state)
		{
		case DEAD:
			//dead: this thing is dead, waiting for cleanup
			
			//enabled = false;
			currentBaseSprite = baseSprites[1];
			currentGunSprite = null;

			break;
		case DYING:
			//dying: handle dying animation
			
			currentFrame = animationFrame / DEATH_FRAMES;
			
			animationFrame++;
			
			if(currentFrame < 4)
			{
				currentBaseSprite = baseSprites[currentFrame + 4];
				currentGunSprite = null;
			}
			else
			{
				state = ActorState.DEAD;
				//enabled = false;
				currentBaseSprite = baseSprites[1]; //a gross hack which I shall fix later
				currentGunSprite = null;
				animationFrame = 0;
			}
			
			

			break;
		case IDLING:
			//idling: track target
			
			//track target: trig
			angle = MathUtils.atan2(CONTEXT.getPlayer().getY() - (y - GUN_OFFSET), CONTEXT.getPlayer().getX() - x);
			
			//fire gun on interval
			
			//state = ActorState.FIRING;
			if(SceneManager.getTicks() % FIRE_FRAMES == 0 && MathUtils.random(255) < FIRE_CHANCE)
			{
				fireMissile();
				state = ActorState.FIRING;
			}
			//fireMissile();
			
			//set base frame
			currentBaseSprite = baseSprites[0];
			
			//set barrel angle and frame
			currentGunSprite = gunSprites[0];
			//currentGunSprite.setOriginCenter();
			//currentGunSprite.setRotation((angle * MathUtils.radiansToDegrees) + 90);

			break;		
		case FIRING:
			//firing: play firing animation
			
			//should go through ABCD
			
			//track target: trig
			angle = MathUtils.atan2(CONTEXT.getPlayer().getY() - (y - GUN_OFFSET), CONTEXT.getPlayer().getX() - x);
			
			currentFrame = animationFrame / FIRE_FRAMES;
			
			animationFrame++;
			
			//System.err.println(currentFrame);
			
			if(currentFrame < 3)
			{
				currentGunSprite = gunSprites[currentFrame + 1];
				currentBaseSprite = baseSprites[0];
			}
			else
			{
				state = ActorState.IDLING;
				//enabled = false;
				currentGunSprite = gunSprites[0]; //a gross hack which I shall fix later
				currentBaseSprite = baseSprites[0];
				animationFrame = 0;
			}						
			
			
			
			
			
			break;
		default:
			break;

		}
		
		//add sprites to draw list
		currentBaseSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);
		currentBaseSprite.setCenter(x, y);		
		//CONTEXT.drawSprite(currentBaseSprite, DRAW_LAYER);
		if(currentGunSprite != null) //handling for death state
		{
			currentGunSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);			
			currentGunSprite.setOriginCenter();
			currentGunSprite.setRotation((angle * MathUtils.radiansToDegrees) + 90.0f);
			currentGunSprite.setCenter(x, y - GUN_OFFSET);
			CONTEXT.drawSprite(currentGunSprite, DRAW_LAYER);
		}
		CONTEXT.drawSprite(currentBaseSprite, DRAW_LAYER);

	}

	private void fireMissile()
	{
		
		//TODO: this needs cleanup very badly
		//TODO: do this with more specific Projectile instead of Thinker
		
		try {
			//System.err.println("FIRE" + x + " " + y);
			Thinker t = ThinkerManager.makeThinker(CONTEXT, MISSILE, this.x, this.y - GUN_OFFSET, 0);
			//t.angle = angle;
			t.angle = angle + MathUtils.random(FIRE_JITTER * -1.0f, FIRE_JITTER);
			
			//TODO: put velocity in the projectile thinker
			t.velocityX = MathUtils.cos(t.angle) * FIRE_VELOCITY - CONTEXT.SCROLL_RATE + FIRE_GROSS_HACK; //still has issues?
			t.velocityY = MathUtils.sin(t.angle) * FIRE_VELOCITY;

			SoundManager.play("FlakFire");
			
			CONTEXT.addThinker(t);
		} catch (ThinkerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void die()
	{
		System.err.println("DIE!" + this); //I think this is actually halfway legit in Java
		
		solid = false;
		state = ActorState.DYING;
		animationFrame = 0;
		SoundManager.play("EXPL01");
	}
	
	@Override
	public void hit(Thinker other) {
		// TODO Auto-generated method stub
		
		/*
		//check if the other thinker is an actor (type safety)
		if(other instanceof ActorThinker)
		{
			//then check its flags
			if(((ActorThinker)other).getFlags().contains(ActorFlag.HURTSMONSTER))
			{
				die();
			}
		}
		 */
	}

	@Override
	public int getRADIUS() {
		return RADIUS;
	}

	

}
