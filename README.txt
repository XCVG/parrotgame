  _____        _____  _____   ____ _______ _____          __  __ ______ 
 |  __ \ /\   |  __ \|  __ \ / __ \__   __/ ____|   /\   |  \/  |  ____|
 | |__) /  \  | |__) | |__) | |  | | | | | |  __   /  \  | \  / | |__   
 |  ___/ /\ \ |  _  /|  _  /| |  | | | | | | |_ | / /\ \ | |\/| |  __|  
 | |  / ____ \| | \ \| | \ \| |__| | | | | |__| |/ ____ \| |  | | |____ 
 |_| /_/    \_\_|  \_\_|  \_\\____/  |_|  \_____/_/    \_\_|  |_|______|

----- INTRODUCTION -----

Parrotgame is a sidescroller created for BCIT Games Development Club in late 2015. You are a military parrot trained to carry messages, and your country is at war. You must survive each level and you can choose different paths between levels.

----- FEATURES -----

* Cross-platform (but only the desktop Java version is playable)
* Keyboard and mouse controls
* Randomly spawning enemies
* Different kinds of enemies!
* A HUD.
* Menus!
* Ten or so maps

----- INSTALLATION -----

You will need the Java Runtime Environment installed on your computer. The latest version of Java is recommended; we used Java 8 for development. However, Java 7 or OpenJDK should work.

Extract the compressed folder and run parrot.jar. You may need to mark the jar file as executable on Unix-like systems.

----- GAMEPLAY -----

Menus are mouse (or touch, in theory) driven. On the main menu, click START to start the game or HELP to display more information (you can click through the help screens). On choice menus, click on any of the choices to pick a path. Other static screens can also be clicked through with the mouse. The game can be closed at any time by just closing the window.

The actual game is controlled with the keyboard. Use the arrow keys to move around and SHIFT to move faster. Avoid getting hit by other birds or shot by bullets and make it to the end.

----- TECHNOLOGY -----

Parrotgame uses the Hypergiant engine, a custom engine built on libGDX. It was originally created as a 2D RPG engine and still retains remnants of this. At this point in development, it is very crude, more or less implementing only enough to get Parrotgame to work.

In theory, it supports all platforms libGDX supports. In practice, only the desktop Java version is playable. It builds for Android with no problems but lacks touch controls or lifecycle management. I don't have a Mac to try building for iOS and I haven't been able to get GWT to work to build HTML5.

Provided it's still up, you can get the source code at https://github.com/XCVG/parrotgame

----- LICENSE -----

The game's source code is distributed under a modified (3-clause) BSD license.

Assets depend on the asset in question- refer to the CREDITS file for details.

You may freely redistribute and modify this game to the extent allowed under those licenses.

----- CREDITS -----

Chris Leclair
	Engine and Game Code

Jaymee Tan
	Graphics, Game Design

Eiko Takeoka
	Graphics, Sound

Andre Culjac
	Ideas

Daniel Amaya
	Ideas
	
For information on all the art assets, refer to the CREDITS.txt file.
