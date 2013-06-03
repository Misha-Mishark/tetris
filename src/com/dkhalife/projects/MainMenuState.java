package com.dkhalife.projects;

import java.io.IOException;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * This class implements the main menu for the game
 * 
 * @author Dany Khalife
 * 
 */
public class MainMenuState extends BasicGameState {
	// The current screen state id
	private int stateID = -1;

	// The images used
	private Image imBackground = null;
	private Image imStart = null;
	private Image imExit = null;

	// And the font for high scores
	private UnicodeFont font = null;

	// And their positions
	private final static int startX = 410;
	private final static int startY = 160;
	private final static int exitX = 410;
	private final static int exitY = 240;

	// Scaling effect
	private float imStartScale = 1;
	private float imExitScale = 1;
	private float scaleStep = 0.0001f;

	// Did we play the splash sound?
	private boolean splashPlayed = false;

	/**
	 * 
	 * To construct a main menu state we only need to give it an ID
	 * 
	 * @param stateID The ID for this main menu state
	 * 
	 */
	public MainMenuState(int stateID) {
		this.stateID = stateID;
	}

	/**
	 * 
	 * Getter for the ID
	 * 
	 * @return The ID
	 * 
	 */
	public int getID() {
		return stateID;
	}

	/**
	 * 
	 * This method initialises the main menu
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		imBackground = ResourceManager.getImage("MAIN_MENU");
		imStart = ResourceManager.getImage("START_GAME");
		imExit = ResourceManager.getImage("EXIT");
		
		font = new UnicodeFont("res/fonts/ITCKRIST.ttf", 20, true, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addAsciiGlyphs();
		font.loadGlyphs();
	}
	
	/**
	 * 
	 * This method resets the text effects each time the menu is entered
	 * 
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		
		imStartScale = imExitScale = 1.0f;
	}

	/**
	 * 
	 * This method renders the main menu
	 * 
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		imBackground.draw(0, 0);
		imStart.draw(startX, startY, imStartScale);
		imExit.draw(exitX, exitY, imExitScale);

		Color c = new Color(160, 56, 0);
		int y = 270;
		int i = 1;
		for (Integer score : Highscores.getInstance().getScores().keySet()) {
			font.drawString(30, y, String.valueOf(i), c);
			font.drawString(80, y, ":", c);
			font.drawString(120, y, Highscores.getInstance().getScores().get(score), c);
			font.drawString(200, y, "-", c);
			font.drawString(240, y, score.toString(), c);

			y += 30;
			++i;
		}
	}

	/**
	 * 
	 * This method tests to see if the mouse is over a specific image
	 * 
	 * @param mouse The mouse coordinates
	 * @param im The image to test on
	 * @return True if the mouse is over the image. False otherwise
	 * 
	 */
	private boolean isMouseOverImage(Point mouse, Image im) {
		int x;
		int y;

		if (im == imStart) {
			x = startX;
			y = startY;
		}
		else {
			x = exitX;
			y = exitY;
		}

		return mouse.getX() >= x && mouse.getX() <= x + im.getWidth() && mouse.getY() >= y && mouse.getY() <= y + im.getHeight();
	}

	/**
	 * 
	 * This method updates the viewport according to the user's input
	 * 
	 */
	public void update(GameContainer gc, StateBasedGame sb, int deltaT) throws SlickException {
		// First lets load the resources if any are needed
		if (LoadingList.get().getRemainingResources() > 0) {
			try {
				DeferredResource e = LoadingList.get().getNext();

				System.out.println(new Date() + " INFO:" + e.getDescription());

				e.load();
			} catch (IOException e) {
				throw new SlickException("Error loading resource", e);
			}
		}
		else {
			// We'll play the splash sound only once
			if (!splashPlayed) {
				ResourceManager.getSound("GAME_SPLASH").play();
				splashPlayed = true;
			}

			Input input = gc.getInput();
			Point mouse = new Point(input.getMouseX(), input.getMouseY());

			// We'll have a mouse over effect on the button
			if (isMouseOverImage(mouse, imStart)) {
				if (imStartScale < 1.05f)
					imStartScale += scaleStep * deltaT;

				// And if we click on it we'll move to the game screen
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					ResourceManager.getSound("BUTTON_UP").play();

					sb.enterState(Tetris.GAMEPLAY_STATE);
				}
			}
			else {
				if (imStartScale > 1.0f)
					imStartScale -= scaleStep * deltaT;
			}

			// We'll have a mouse over effect on the button
			if (isMouseOverImage(mouse, imExit)) {
				if (imExitScale < 1.05f)
					imExitScale += scaleStep * deltaT;

				// And if we click on it we'll exit
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					gc.exit();
				}
			}
			else {
				if (imExitScale > 1.0f)
					imExitScale -= scaleStep * deltaT;
			}
		}
	}
}
