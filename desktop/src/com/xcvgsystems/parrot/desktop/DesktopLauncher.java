package com.xcvgsystems.parrot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xcvgsystems.hypergiant.EVars;
import com.xcvgsystems.hypergiant.GVars;
import com.xcvgsystems.parrot.ParrotGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = EVars.WINDOW_X;
		config.height = EVars.WINDOW_Y;
		config.title = GVars.GAME_NAME;
		new LwjglApplication(new ParrotGame(new DesktopPVarsData()), config);
	}
}
