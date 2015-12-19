package com.xcvgsystems.hypergiant.scenes;

import com.xcvgsystems.hypergiant.managers.SceneManager;

public class TimedImageScene extends ImageScene {

	private int time;
	
	public TimedImageScene(String image, String music, String nextScene, int time)
	{
		super(image, music, nextScene);
		// TODO Auto-generated constructor stub
		this.time = time;
	}
	
	@Override
	public void tick()
	{
		time--;
		if(time <= 0)
		{
			SceneManager.changeScene(nextScene);
		}
	}

}
