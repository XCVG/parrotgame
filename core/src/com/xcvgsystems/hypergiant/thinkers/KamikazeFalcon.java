package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.SoundManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;
import com.xcvgsystems.hypergiant.utils.SpriteHelper;

public class KamikazeFalcon extends ActorThinker {

	final protected ScrollScene CONTEXT;
	
	final String GRAPHIC_NAME = "BigFalcon";
	
	final int RADIUS = 16;
	final int DRAW_WIDTH = 64;
	final int DRAW_HEIGHT = 64;
	final int DEATH_FALL_RATE = 8;
	final int MOVE_FRAMES = 5;
	final int SPEED = 4;
	
	final int MAX_VELOCITY = 7;
	final int DELTA_V = 1;
	final int SEEK_CHANCE = 100;
	
	protected int animationFrame; //frame count for any animation
	protected int stateFrames; //frames for this state; meant for walking
	protected boolean running;
	
	protected Sprite[][] sprites = new Sprite[8][8]; //rows: ABCDEFGH cols: 1357
	
	//temporary
	protected Sprite currentSprite;

	public KamikazeFalcon(int x, int y, ScrollScene context) {
		
		flags = EnumSet.of(ActorFlag.HURTSPLAYER, ActorFlag.ISMONSTER, ActorFlag.NOGRAVITY); //GOOD JOB ECLIPSE RECOMMENDERS
		
		this.x = x;
		this.y = y;
		this.facing = 7;
		this.solid = true;
		this.CONTEXT = context;
			
		SpriteHelper.getSprites(GRAPHIC_NAME, sprites);
		
		currentSprite = sprites[0][1];
		
		velocityX = (CONTEXT.getSCROLL_RATE() + SPEED) * -1;
		
		this.state = ActorState.SPAWNING;
	}	

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		x += velocityX;
		y += velocityY;
		
		//CAAAW on spawn
		if(state == ActorState.SPAWNING)
		{
			SoundManager.play("FALCACT");
			state = ActorState.MOVING;
		}
		
		//do different things based on state
		switch(state)
		{
		case DEAD:
			//dead: this thing is dead, show an animation
			
			enabled = false;

			break;
		case DORMANT:
			//dormant: do, uh, nothing?

			break;
		case DYING:
			//dying: handle dying animation
			
			animateDying();

			break;
		case IDLING:
			//idling: this enemy should never be idling
			currentSprite = sprites[0][facing];

			break;
		case MOVING:
			//moving: handle moving animation
			
			//seeking
			if(MathUtils.random(255) < SEEK_CHANCE)
			{
				if(CONTEXT.getPlayer().getY() > this.y)
				{
					velocityY += DELTA_V;
				}
				else if(CONTEXT.getPlayer().getY() < this.y)
				{
					velocityY -= DELTA_V;
				}
				
				if(velocityY > MAX_VELOCITY)
				{
					velocityY = MAX_VELOCITY;
				}
				
				if(velocityY < -MAX_VELOCITY)
				{
					velocityY = -MAX_VELOCITY;
				}
				
			}
			
			//bounds checking
			if(this.y + RADIUS >= CONTEXT.HEIGHT - CONTEXT.FLOOR_SPAWN_OFFSET || this.y - RADIUS <= 0)
			{
				velocityY = 0;
			}

			animateMoving();

			break;
		case SPAWNING:
			//spawning: handle spawn animation

			break;
		default:
			break;

		}
		
		//draw sprite
		currentSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);
		currentSprite.setCenter(x, y);
		CONTEXT.drawSprite(currentSprite, 4);

	}
	
	private void animateDying() {
		// TODO Auto-generated method stub
		
		animationFrame++;
		if(animationFrame < stateFrames)
		{
			currentSprite = sprites[5][facing];
		}
		else if(animationFrame < stateFrames * 2)
		{
			currentSprite = sprites[6][facing];
		}
		else
		{
			currentSprite = sprites[7][facing];
		}
		
		if(this.y > CONTEXT.getHEIGHT())
		{
			state = ActorState.DEAD;
		}
		
	}

	private void animateMoving() {
		//play animation
		animationFrame++;
		if(animationFrame < MOVE_FRAMES)
		{
			currentSprite = sprites[3][facing];
		}
		else if(animationFrame < MOVE_FRAMES * 2)
		{
			currentSprite = sprites[4][facing];
		}
		else
		{
			animationFrame = 0;
		}
	}

	@Override
	public void hit(Thinker other) {
		
		if(other instanceof PlayerPawn)
		{
			solid = false;
			StateManager.getPlayer().changeHP(-3);
			state = ActorState.DYING;			
			velocityX /= 2;
			velocityY = CONTEXT.getGRAVITY();
			SoundManager.play("GENHIT");
			//System.err.println("HIT!");
		}

	}

	@Override
	public int getRADIUS() {
		return RADIUS;
	}	

}
