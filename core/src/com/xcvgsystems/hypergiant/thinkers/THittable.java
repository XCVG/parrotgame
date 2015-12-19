package com.xcvgsystems.hypergiant.thinkers;

import com.xcvgsystems.hypergiant.scenes.ScrollScene;

public interface THittable {
	
	public void hit(Thinker other);
	
	public int getRADIUS();

}
