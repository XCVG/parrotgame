package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xcvgsystems.hypergiant.managers.SoundManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;
import com.xcvgsystems.hypergiant.scenes.ScrollScene;

public class TreeBranch extends ActorThinker {

	final protected ScrollScene CONTEXT;
	
	final String GRAPHIC_NAME = "Branch";
	
	final int RADIUS = 32;
	final int DRAW_WIDTH = 96;
	final int DRAW_HEIGHT = 96;
	
	Sprite currentSprite;
	
	boolean alreadyHit = false;
	
	public TreeBranch(int x, int y, ScrollScene context) {
		
		CONTEXT = context;
		this.x = x;
		this.y = y;
		this.facing = 0;
		this.solid = true;
		flags = EnumSet.of(ActorFlag.NOGRAVITY, ActorFlag.HURTSPLAYER);
		
		currentSprite = new Sprite(TextureManager.get(GRAPHIC_NAME+"A0"));
		
		velocityX = (CONTEXT.getSCROLL_RATE()) * -1;
		
	}

	@Override
	public void tick() {
		
		x += velocityX;
		y += velocityY;
		
		//draw sprite
		currentSprite.setSize(DRAW_WIDTH, DRAW_HEIGHT);
		currentSprite.setCenter(x, y);
		CONTEXT.drawSprite(currentSprite, 1);
	}
	
	@Override
	public void hit(Thinker other) {
		if(!alreadyHit && other instanceof PlayerPawn)
		{
			//damage player, play sound, make this not solid

			StateManager.getPlayer().changeHP(-1);
			SoundManager.play("GENHIT");
			//this.solid = false;
			
			//if player is moving, slow them down!
			//if(other.getVelocityX() > 0.1f)
			
			alreadyHit = true;
				
		}
		
		//slowdown effect because the other one makes the game 
		if(other instanceof PlayerPawn)
		{
			other.setVelocityX(other.getVelocityX() / 5f);
			other.setVelocityY(other.getVelocityY() / 5f);
		}

	}

	@Override
	public int getRADIUS() {
		// TODO Auto-generated method stub
		return RADIUS;
	}

	

}
