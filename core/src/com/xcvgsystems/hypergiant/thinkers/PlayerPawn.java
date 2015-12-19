package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xcvgsystems.hypergiant.managers.InputManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.managers.InputManager.InputDirection;
import com.xcvgsystems.hypergiant.managers.TextureManager;
import com.xcvgsystems.hypergiant.scenes.Scene;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;
import com.xcvgsystems.hypergiant.utils.SpriteHelper;

/**
 * PlayerPawn: A special Thinker that represents the player.
 * 
 * @author Chris
 *
 */

//TODO: completely redo this for the parrot game

public class PlayerPawn extends ActorThinker {
	
	final protected ScrollScene CONTEXT;
	
	final String GRAPHIC_NAME = "Player";
	
	final int RADIUS = 16;
	final int DRAW_WIDTH = 64;
	final int DRAW_HEIGHT = 64;
	
	final int SPEED = 5;
	final int SPEED_RUNNING = 10;
	final int DEATH_FALL_RATE = 5;
	
	//technically, all these frames are ticks... sorry lol
	final int IDLE_FRAMES = 6;
	final int WALK_FRAMES = 4; 
	final int WALK_FRAMES_RUN = 2;
	final int DEATH_FRAMES = 8;
	
	//protected ActorState state;
	
	protected int animationFrame; //frame count for any animation
	protected int stateFrames; //frames for this state; meant for walking
	protected boolean running;
	
	protected Sprite[][] sprites = new Sprite[8][8]; //rows: ABCDEFGH cols: 1357
	
	//temporary
	protected Sprite currentSprite;
	
	
	
	public PlayerPawn(int x, int y, ScrollScene context)
	{
		this.x = x;
		this.y = y;
		this.facing = 7;
		this.CONTEXT = context;
			
		SpriteHelper.getSprites(GRAPHIC_NAME, sprites);
		
		currentSprite = sprites[0][1];
		
		this.state = ActorState.IDLING;
		this.flags = EnumSet.of(ActorFlag.DONTSCROLL, ActorFlag.HURTSMONSTER, ActorFlag.KEEPOFFSCREEN, ActorFlag.NOGRAVITY);
		
	}

	@Override
	public void tick() {
		//I am a player, I do nothing here
		//or should I put player movement handling here?
		
		//MOVEMENT HANDLING
		//TODO: bounds checking
		
		x += velocityX;
		y += velocityY;
		
		//do different things based on state
		switch(state)
		{
		case DEAD:
			//dead: player is dead, do nothing
				velocityX = 0;
				velocityY = 0;
				
				x = CONTEXT.getWIDTH()/2;
				y = CONTEXT.getHEIGHT()/2;
				
				currentSprite = null;
			break;
		case DORMANT:
			//dormant: do, uh, nothing?
			
			break;
		case DYING:
			//dying: handle dying animation
				if(y >= CONTEXT.HEIGHT)
				{
					state = ActorState.DEAD;
					break;
				}
			
				animateDying();
			break;
		case IDLING:
			//idling: check player input and do stuff, handle idle animation
			
			//new: handle dying
			if(StateManager.getPlayer().getHp() <= 0)
			{
				state = ActorState.DYING;
				velocityX /= 2;
				velocityY = DEATH_FALL_RATE;
				break;
			}
			
			if(InputManager.isDirectionPressed())
			{
				//we want to move somewhere							
				handleMovement();
				animateMoving();
			}
			else
			{
				//animate idling
				animateIdling();
			}
			
			break;
		case MOVING:
			//moving: handle moving animation
			
			//handle the actual animation of frames in the draw
			//handle only the moving of the object here
			
			
			//finish moving if direction is not pressed... this is in the wrong place I know

			handleMovement();
			animateMoving();
			
			
			break;
		case SPAWNING:
			//spawning: handle spawn animation
			
			break;
		default:
			break;
		
		}
		
		//yes, this is a gross hack
		//if(currentSprite == null)
		//	currentSprite = sprites[0][1];
		
		
		//VERY TEMPORARY (or not)
		//currentSprite.setPosition(x, y);
		//currentSprite.setOriginCenter();
		if(currentSprite != null)
		{
			currentSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);
			currentSprite.setCenter(x, y);
			CONTEXT.drawSprite(currentSprite, 5);
		}
		
		return;
	}

	//new method to replace old broken ones
	private void handleMovement()
	{
		//zero velocity
		velocityX = 0;
		velocityY = 0;	
		
		//deal with velocity
		float speed;
		running = InputManager.isShiftPressed();
		if(running)
		{
			speed = SPEED_RUNNING;
			stateFrames = WALK_FRAMES_RUN;
		}
		else
		{
			speed = SPEED;
			stateFrames = WALK_FRAMES;
		}
		
		//set velocity
		if(InputManager.isDirectionPressed(InputDirection.UP))
		{
			//edge collision detection
			if(!(y < 0 + RADIUS))
				velocityY -= speed;
		}
		else if(InputManager.isDirectionPressed(InputDirection.DOWN))
		{
			//edge collision detection
			if(!(y > CONTEXT.getHEIGHT() - RADIUS - CONTEXT.FLOOR_SPAWN_OFFSET))
				velocityY += speed;
		}
		
		if(InputManager.isDirectionPressed(InputDirection.RIGHT))
		{
			if(!(x > CONTEXT.getWIDTH() - RADIUS))
				velocityX += speed;
			facing = 7;
		}
		else if(InputManager.isDirectionPressed(InputDirection.LEFT))
		{
			if(!(x < 0 + RADIUS))
				velocityX -= speed;
			facing = 3;
		}
		
		//set state
		if(velocityX == 0f && velocityY == 0f)		
			this.state = ActorState.IDLING;
		else
			this.state = ActorState.MOVING;
		
		
	}

	private void animateMoving() {
		//play animation
		animationFrame++;
		if(animationFrame < stateFrames)
		{
			currentSprite = sprites[1][facing];
		}
		else if(animationFrame < stateFrames * 2)
		{
			currentSprite = sprites[2][facing];
		}
		else if(animationFrame < stateFrames * 3)
		{
			currentSprite = sprites[3][facing];
		}
		else if(animationFrame < stateFrames * 4)
		{
			currentSprite = sprites[4][facing];
		}
		else
		{
			animationFrame = 0;
		}
	}

	private void animateIdling() {
		facing = 7;
		
		//play animation
		animationFrame++;
		if(animationFrame < IDLE_FRAMES)
		{
			currentSprite = sprites[1][facing];
		}
		else if(animationFrame < IDLE_FRAMES * 2)
		{
			currentSprite = sprites[2][facing];
		}
		else if(animationFrame < IDLE_FRAMES * 3)
		{
			currentSprite = sprites[3][facing];
		}
		else if(animationFrame < IDLE_FRAMES * 4)
		{
			currentSprite = sprites[4][facing];
		}
		else
		{
			animationFrame = 0;
		}
		
	}
	
	//animate player dying
	private void animateDying() {
		// TODO Auto-generated method stub
		animationFrame++;
		if(animationFrame < DEATH_FRAMES)
		{
			currentSprite = sprites[5][facing];
		}
		else if(animationFrame < DEATH_FRAMES * 2)
		{
			currentSprite = sprites[6][facing];
		}
		else
		{
			animationFrame = 0;
		}
	}

	@Override
	public void hit(Thinker other) {
		
		//TODO: collision handling
		
		/*
		System.err.println("HIT!");
		
		if(other == null)
		{
			//if other is null, then we've hit the BLOCKMAP
			this.x = oldX;
			this.y = oldY;
			finishMoving();
			return;
		}
		
		//I am the player, I will rely on the other Thinker to do something
		//although I will arrest my motion(?)
		
		return;
		*/
	}

	@Override
	public int getRADIUS() {
		return RADIUS;
	}

}
