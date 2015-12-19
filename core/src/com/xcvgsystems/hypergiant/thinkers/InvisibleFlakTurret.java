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

public class InvisibleFlakTurret extends ActorThinker {

	final protected ScrollScene CONTEXT;
	
	final String MISSILE = "GunBullet";
	
	final int RADIUS = 16;
	final int DRAW_LAYER = 3;
	final int DRAW_WIDTH = 64;
	final int DRAW_HEIGHT = 64;
	final int DEATH_FRAMES = 5;
	final int FIRE_FRAMES = 5;
	final int FIRE_RATE = 30; // every x tics
	final int FIRE_CHANCE = 10; //guaranteed to fire
	final int FIRE_VELOCITY = 10;
	final float FIRE_JITTER = 0.5f;
	
	protected int animationFrame; //frame count for any animation
	//protected int stateFrames; //frames for this state; meant for walking

	//protected float angle; //angle the turret is pointed at
	
	
	public InvisibleFlakTurret(int x, int y, ScrollScene context) {
		this.CONTEXT = context;
		
		flags = EnumSet.of(ActorFlag.SPAWNFLOOR, ActorFlag.ISMONSTER);
		
		this.x = x;
		this.y = y;
		this.facing = 0;
		this.solid = true;
		

		velocityX = (CONTEXT.getSCROLL_RATE()) * -1;
		
		this.state = ActorState.IDLING;
	}
	
	@Override
	public void tick() {

		
		int currentFrame;
		
		x += velocityX;
		y += velocityY;
		
		switch(state)
		{
		case DEAD:
			//dead: this thing is dead, waiting for cleanup

			break;
		case DYING:
			//dying: handle dying animation
			
			currentFrame = animationFrame / DEATH_FRAMES;
			
			animationFrame++;
			
			if(currentFrame < 4)
			{

			}
			else
			{
				state = ActorState.DEAD;
				//enabled = false;

				animationFrame = 0;
			}
			
			

			break;
		case IDLING:
			//idling: track target
			
			//track target: trig
			angle = MathUtils.atan2(CONTEXT.getPlayer().getY() - y, CONTEXT.getPlayer().getX() - x);
			
			//fire gun on interval
			
			//state = ActorState.FIRING;
			if(SceneManager.getTicks() % FIRE_FRAMES == 0 && MathUtils.random(255) < FIRE_CHANCE)
			{
				fireMissile();
				state = ActorState.FIRING;
			}
			//fireMissile();
			
			//set base frame

			//currentGunSprite.setOriginCenter();
			//currentGunSprite.setRotation((angle * MathUtils.radiansToDegrees) + 90);

			break;		
		case FIRING:
			//firing: play firing animation
			
			//should go through ABCD
			
			currentFrame = animationFrame / FIRE_FRAMES;
			
			animationFrame++;
			
			//System.err.println(currentFrame);
			
			if(currentFrame < 4)
			{

			}
			else
			{
				state = ActorState.IDLING;
				//enabled = false;

				animationFrame = 0;
			}						
			
			
			
			
			
			break;
		default:
			break;

		}
		

	}

	private void fireMissile()
	{
		
		//TODO: this needs cleanup very badly
		//TODO: do this with more specific Projectile instead of Thinker
		
		try {
			//System.err.println("FIRE" + x + " " + y);
			Thinker t = ThinkerManager.makeThinker(CONTEXT, MISSILE, this.x, this.y, 0);
			//t.angle = angle;
			t.angle = angle + MathUtils.random(FIRE_JITTER * -1.0f, FIRE_JITTER);
			
			//TODO: put velocity in the projectile thinker
			t.velocityX = MathUtils.cos(t.angle) * FIRE_VELOCITY - CONTEXT.SCROLL_RATE;
			t.velocityY = MathUtils.sin(t.angle) * FIRE_VELOCITY;
			//t.velocityX = 2; //TODO: actual velocity
			//t.velocityY = 2;
			CONTEXT.addThinker(t);
			
			SoundManager.play("GunFire");
			
		} catch (ThinkerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void die()
	{
		//System.err.println("DIE!" + this); //I think this is actually halfway legit in Java
		
		solid = false;
		state = ActorState.DYING;
		animationFrame = 0;
		SoundManager.play("EXPL01");
	}
	
	@Override
	public void hit(Thinker other) {
		// TODO Auto-generated method stub
		
		//check if the other thinker is an actor (type safety)
		if(other instanceof ActorThinker)
		{
			//then check its flags
			if(((ActorThinker)other).getFlags().contains(ActorFlag.HURTSMONSTER))
			{
				die();
			}
		}

	}

	@Override
	public int getRADIUS() {
		return RADIUS;
	}

	

}
