package com.dkhalife.projects;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Dany Khalife
 * @since 26 December 2012
 * 
 */
public class Tetris extends StateBasedGame {
	// We'll have two states so we'll need an ID for each of them
	public static final int MAINMENU_STATE = 0;
	public static final int GAMEPLAY_STATE = 1;

	/**
	 * 
	 * The game is constructed by constructing a StateBasedGame with both of the
	 * states
	 * 
	 */
	public Tetris() throws SlickException {
		super("Tetris");

		// Add our two states
		addState(new MainMenuState(MAINMENU_STATE));
		addState(new PlayingState(GAMEPLAY_STATE));

		// Load our resources
		ResourceManager.loadResources("res/resources.xml");

		// And load the main menu state
		enterState(MAINMENU_STATE);
	}

	/**
	 * 
	 * This method initialises both states
	 * 
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(MAINMENU_STATE).init(gc, this);
		this.getState(GAMEPLAY_STATE).init(gc, this);
	}

	// This main method launches the game
	public static void main(String args[]) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Tetris());

		// Limit the update calls and fps so that CPU wont explode
		app.setMinimumLogicUpdateInterval(25);
		app.setTargetFrameRate(30);
		// We'll set the display as 800x600
		app.setDisplayMode(800, 600, false);
		app.start();
	}
}
