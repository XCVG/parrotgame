package com.xcvgsystems.hypergiant.thinkers;

import java.util.EnumSet;

import com.xcvgsystems.hypergiant.scenes.ScrollScene;

public abstract class ActorThinker extends Thinker implements TDrawable, THittable, TTickable{
	
	protected ActorState state;	
	protected EnumSet<ActorFlag> flags; 
	
	public boolean checkFlag(ActorFlag flag)
	{
		return flags.contains(flag);
	}
	
	public EnumSet<ActorFlag> getFlags()
	{
		return flags;
	}

	public ActorState getState() {
		return state;
	}
	
	
	
	
}
