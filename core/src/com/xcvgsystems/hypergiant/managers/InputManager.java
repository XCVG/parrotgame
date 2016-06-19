package com.xcvgsystems.hypergiant.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.*;

//not sure how libGDX deals with input, so... abstract it
//TODO: dear god this is poorly written... refactor it

/**
 * Provides input services.
 * Gross hacks have been added to support a controller.
 * @author Chris
 *
 */
public class InputManager {

	/**
	 * Represents an input direction.
	 * @author Chris
	 *
	 */
	public enum InputDirection
	{

		UP,
		DOWN, 
		LEFT,
		RIGHT
	}
	
	private static InputDirection inputDirection;
	private static int touchX, touchY;
	private static Controller controller;
	
	/**
	 * Initialize the InputManager
	 */
	public static void init()
	{
		System.out.print("InputManager.init...");
		
		/*
		for (Controller controller : Controllers.getControllers()) {
		   	System.out.println(controller.toString() + controller.getName());
		*/
		for (Controller controller : Controllers.getControllers())
		{
			if(controller.getName().equalsIgnoreCase("Logitech Dual Action")) //yes it's fucking hardcoded
			{
				InputManager.controller = controller; //yes I'm dumb
			}
		}
		
		System.out.println("done!");
	}
	
	/**
	 * Poll the input
	 */
	public static void poll()
	{
		getDirectionKeys();
		getTouchCoords();
		getControllerInput();
	}
	
	/**
	 * Dispose of the InputManager
	 */
	public static void dispose()
	{
		System.out.print("InputManager.dispose...");
		
		Controllers.clearListeners();
		
		System.out.println("done!");
	}
	
	/**
	 * Check if the screen is being touched or mouse button pressed.
	 * @return if the screen is being pressed
	 */
	public static boolean isTouchPressed()
	{
		boolean pressed = Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT) || controller.getButton(1);
		//System.err.println(pressed);
		return pressed; 
	}
	
	/**
	 * Check if the screen has just been touched or mouse button pressed.
	 * @return if the screen was just pressed
	 */
	public static boolean isTouchJustPressed()
	{
		boolean pressed = Gdx.input.justTouched(); // || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT);
		//System.err.println(pressed);
		return pressed; 
	}
	
	/**
	 * Gets the x coordinate of the last screen touch.
	 * @return x coordinate of last touch/click
	 */
	public static int getTouchX() {
		return touchX;
	}
	
	/**
	 * Gets the y coordinate of the last screen touch.
	 * @return y coordinate of last touch/click
	 */
	public static int getTouchY() {
		return touchY;
	}
	
	/**
	 * Check if some direction is pressed.
	 * @return if a direction is pressed
	 */
	public static boolean isDirectionPressed()
	{

		if(inputDirection != null)
			return true;
		
		return false;
	}
	
	/**
	 * Check if specific direction is pressed.
	 * @return if a direction is pressed
	 */
	public static boolean isDirectionPressed(InputDirection direction)
	{

		switch(direction)
		{
		case DOWN:
			return Gdx.input.isKeyPressed(Input.Keys.DOWN) || controller.getPov(0).equals(PovDirection.south) || controller.getPov(0).equals(PovDirection.southEast) || controller.getPov(0).equals(PovDirection.southWest);
		case LEFT:
			return Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.getPov(0).equals(PovDirection.west) || controller.getPov(0).equals(PovDirection.southWest) || controller.getPov(0).equals(PovDirection.northWest);
		case RIGHT:
			return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.getPov(0).equals(PovDirection.east) || controller.getPov(0).equals(PovDirection.southEast) || controller.getPov(0).equals(PovDirection.northEast);
		case UP:
			return Gdx.input.isKeyPressed(Input.Keys.UP) || controller.getPov(0).equals(PovDirection.north) || controller.getPov(0).equals(PovDirection.northEast) || controller.getPov(0).equals(PovDirection.northWest);
		default:
			return false;	
		}
		
		
	}
	
	/**
	 * Get the direction that is pressed.
	 * @return the direction that is pressed
	 */
	public static InputDirection getDirectionPressed()
	{
		return inputDirection;
		
	}
	
	/**
	 * Get the direction that is pressed as facing value.
	 * @return the direction that is pressed (as facing value)
	 */
	public static int getFacingPressed()
	{
		return inputDirectionToFacing(inputDirection);
	}
	
	/**
	 * Check if the use key is pressed.
	 * @return if the use key is pressed
	 */
	public static boolean isUsePressed()
	{
		return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER) || controller.getButton(1);
		
	}
	
	/**
	 * Check if the fire key is pressed.
	 * @return if the fire key is pressed
	 */
	public static boolean isFirePressed()
	{
		return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
	}
	
	/**
	 * Check if the escape key is pressed.
	 * @return if the escape key is pressed
	 */
	public static boolean isEscPressed()
	{
		return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);	
	}
	
	/**
	 * Check if the escape key is just pressed.
	 * @return if the escape key is just pressed
	 */
	public static boolean isEscJustPressed()
	{
		return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);	
	}
	
	/**
	 * Check if the shift key is pressed.
	 * @return if the shift key is pressed
	 */
	public static boolean isShiftPressed()
	{
		return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || controller.getButton(1);
	}
	
	/**
	 * Converts an InputDirection enum to a facing value.
	 * @param direction the InputDirection to convert
	 * @return the facing value
	 */
	public static int inputDirectionToFacing(InputDirection direction)
	{
		if(direction == InputDirection.UP)
		{
			return 5;
		}
		else if(direction == InputDirection.DOWN)
		{
			return 1;
		}
		else if(direction == InputDirection.RIGHT)
		{
			return 7;
		}
		else if(direction == InputDirection.LEFT)
		{
			return 3;
		}
		
		return 0;
	}
	
	/**
	 * Converts a facing value to an InputDirection enum.
	 * @param facing the facing value to convert
	 * @return the InputDirection
	 */
	public static InputDirection facingToInputDirection(int facing)
	{
		
		if(facing == 1)
		{
			return InputDirection.DOWN;
		}
		else if(facing == 3)
		{
			return InputDirection.LEFT;
		}
		else if(facing == 5)
		{
			return InputDirection.UP;
		}
		else if(facing == 7)
		{
			return InputDirection.RIGHT;
		}
		
		return null;
	}
	
	private static void getDirectionKeys()
	{
		//do we need to do an override check here?
		
		//are NEW keys pressed?
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			inputDirection = InputDirection.UP;
			return;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
		{
			inputDirection = InputDirection.DOWN;
			return;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
		{
			inputDirection = InputDirection.LEFT;
			return;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
		{
			inputDirection = InputDirection.RIGHT;
			return;
		}
		
		//if no direction keys, null it
		if(!(
				Gdx.input.isKeyPressed(Input.Keys.UP) ||
				Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
				Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
				Gdx.input.isKeyPressed(Input.Keys.RIGHT)
				))
		{
			inputDirection = null;
		}
		
	}
	
	private static void getTouchCoords()
	{
		//probably bad but eh
		if(isTouchPressed() || isTouchJustPressed())
		{
			touchX = Gdx.input.getX();
			touchY = Gdx.input.getY();
		}
	}
	
	private static void getControllerInput()
	{
		//check if input direction is not null so we don't override the keyboard input
		if(inputDirection != null)
			return;
		
		//otherwise look at the controller
		if(controller.getPov(0).equals(PovDirection.north))
		{
			inputDirection = InputDirection.UP;
		}
		else if(controller.getPov(0).equals(PovDirection.south))
		{
			inputDirection = InputDirection.DOWN;
		}
		else if(controller.getPov(0).equals(PovDirection.west))
		{
			inputDirection = InputDirection.LEFT;
		}
		else if(controller.getPov(0).equals(PovDirection.east))
		{
			inputDirection = InputDirection.RIGHT;
		}
		
	}
	

	
}
