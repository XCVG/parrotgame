package com.xcvgsystems.hypergiant.menus;

import java.util.ArrayList;
import java.util.List;

import com.xcvgsystems.hypergiant.managers.MusicManager;
import com.xcvgsystems.hypergiant.managers.SceneManager;
import com.xcvgsystems.hypergiant.managers.StateManager;
import com.xcvgsystems.hypergiant.managers.TextureManager;
import com.xcvgsystems.hypergiant.models.MenuChoice;

public class ChoiceMenu extends Menu {
	
	protected static final int CHOICE_OFFSET_ADD = 110;
	protected static final int CHOICE_OFFSET_BASE = 180;
	protected static final int CHOICE_WINDOW_HEIGHT = 100;
	protected static final int CHOICE_WINDOW_WIDTH = 540;
	protected final String D_CMENUBACK = "CMENUBACK"; //TODO: better choice
	protected final String D_CHOICEFRAME = "UI_frame"; //TODO: these are temporary
	protected final String D_CBUTTON = "UI_cbutton"; //TODO: these are temporary
	protected final String D_MUSIC = "M_INTER";
	
	//ordered list of choices, not the cleanest
	protected String music;
	protected List<String> choices;

	public ChoiceMenu(String title, String description, String bgname, String music, List<MenuChoice> choices) {
		super();
		
		//set music
		this.music = music;
		
		//set background
		background = TextureManager.get(bgname);
		
		//create title and description
		objects.add(new MenuText(WIDTH/2, 20, MenuObject.LAYER_OBJECT, "BIGFONT", title, this));
		
		//TODO: multiline
		objects.add(new MenuText(WIDTH/2, 80, MenuObject.LAYER_OBJECT, "VSMALLFNT", description, this));
		
		this.choices = new ArrayList<String>();
		
		for(MenuChoice choice : choices)
		{
			addChoice(choice);
		}
		
		//System.err.println(this.choices);
	}
	
	@Override
	public void enter() {
		super.enter();
		MusicManager.play(music, true);
		
		//heal the player
		StateManager.getPlayer().setHp(StateManager.getPlayer().MAX_HP);
		
	}

	@Override
	public void exit() {
		super.exit();
		
	}
	
	protected void addChoice(MenuChoice choice)
	{
		//adds a choice to the choice list
		//System.err.println(choice.toNiceString());
		
		int yOffset = CHOICE_OFFSET_BASE + choices.size() * CHOICE_OFFSET_ADD;
		
		//TODO: create frame, title text, description text, and choose button
		objects.add(new MenuImage(WIDTH/2, yOffset, CHOICE_WINDOW_WIDTH, CHOICE_WINDOW_HEIGHT, MenuObject.LAYER_WINDOW, D_CHOICEFRAME, this));
		objects.add(new MenuText(WIDTH/2, yOffset - 30, MenuObject.LAYER_OBJECT, "SMALLFNT", choice.getTitle(), this));
		objects.add(new MenuText(WIDTH/2, yOffset - 10, MenuObject.LAYER_OBJECT, "VSMALLFNT", choice.getDescription(), this));
		objects.add(new MenuButton(WIDTH/2, yOffset + 25, 100, 40, MenuObject.LAYER_OBJECT, D_CBUTTON, choices.size(), this));
		
		choices.add(choice.getDestination());
	}

	@Override
	public void update() {
		super.update();
		
		

	}

	@Override
	public void draw() {
		super.draw();

	}

	@Override
	public void invokeEvent(int ev) {
		//System.err.println("CHANGESCENE " + ev);

		//simply changes scene to the one specified by the choice
		//if(choices[ev] != null && !choices[ev].isEmpty())
		//	SceneManager.changeScene(choices[ev]);

		if(!(ev >= choices.size()))
		{
			SceneManager.changeScene(choices.get(ev));
		}

	}



}
