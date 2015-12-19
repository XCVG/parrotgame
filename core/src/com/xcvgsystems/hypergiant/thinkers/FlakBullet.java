package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.xcvgsystems.hypergiant.managers.SoundManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;
import com.xcvgsystems.hypergiant.utils.SpriteHelper;

public class FlakBullet extends ActorThinker {
	
	//TODO: generalize to Projectile 
	
	final protected ScrollScene CONTEXT;
	final String GRAPHIC_NAME = "Bullet";
	
	final int RADIUS = 8;
	final int DRAW_LAYER = 6;
	final int DRAW_WIDTH = 16;
	final int DRAW_HEIGHT = 16;
	final int DEATH_FRAMES = 1;
	
	protected int animationFrame; //frame count for any animation
	//protected 
	
	protected Sprite[] sprites = new Sprite[8]; //rows: ABCDEFGH

	public FlakBullet(int x, int y, ScrollScene context) {
		// TODO Auto-generated constructor stub
		
		this.CONTEXT = context;
		
		flags = EnumSet.of(ActorFlag.NOGRAVITY, ActorFlag.PROJECTILE, ActorFlag.HURTSPLAYER);
		
		
		
		this.x = x;
		this.y = y;
		
		//System.err.println(this.x + " " + this.y);
		this.facing = 0;
		//this.angle = angle;
		this.solid = true;
		
		SpriteHelper.getSprites(GRAPHIC_NAME, sprites);
		
		state = ActorState.MOVING;
	}
	
	@Override
	public void tick() {
		
		//System.err.println(this.toString() + x + y + velocityX + velocityY);
		
		x += velocityX;
		y += velocityY;
		
		Sprite currentSprite;
		
		//do different things based on state
		switch(state)
		{
		case DEAD:
			//dead: this thing is dead, show an animation
			currentSprite = sprites[7];
			enabled = false;

			break;
		case DYING:
			//dying: handle dying animation
			
			//animateDying();					
			
			int currentFrame = animationFrame / DEATH_FRAMES;
			
			animationFrame++;
			
			//System.err.println(currentFrame);
			
			if(currentFrame < 7)
			{
				currentSprite = sprites[currentFrame + 1];
			}
			else
			{
				state = ActorState.DEAD;
				//enabled = false;
				currentSprite = sprites[7];
				animationFrame = 0;
			}
			
			break;
		default:
			currentSprite = sprites[0];
			break;

		}
		
		//draw sprite
		currentSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);
		currentSprite.setOriginCenter();
		currentSprite.setRotation((angle * MathUtils.radiansToDegrees) + 90.0f);
		
		currentSprite.setCenter(x, y);
		//System.err.println(currentSprite.getX() + " " + currentSprite.getY());
		CONTEXT.drawSprite(currentSprite, DRAW_LAYER);

	}

	@Override
	public void hit(Thinker other) {
		
		if(other instanceof PlayerPawn)
		{
			solid = false;
			StateManager.getPlayer().changeHP(-1);
			state = ActorState.DYING;			
			velocityX = 0;
			velocityY = 0;
			SoundManager.play("EXPL01");
			//System.err.println("HIT!");
		}

	}
	
	@Override
	public int getRADIUS() {
		// TODO Auto-generated method stub
		return RADIUS;
	}

}
