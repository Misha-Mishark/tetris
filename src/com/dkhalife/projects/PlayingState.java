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
 * This class represents the playing screen
 * 
 * @author Dany Khalife
 * 
 */
public class PlayingState extends BasicGameState {
	// First we define some constants that we will use throughout this class
	private final static int PIT_X = 52;
	private final static int PIT_Y = 18;
	private final static int BLOCK_SIZE = 28;
	private final static int INPUT_TIMETOUT = 100;

	// We need to store the state for this screen
	private int stateID;

	// We'll keep track of the resources being used
	private Image hud = null;
	private UnicodeFont font = null;

	// We'll keep track of the pit
	private Pit pit = new Pit(10, 20);

	// We need a score and a multiplier
	private int score;
	private int scoreGained = 0;
	private int multiplier = 1;
	private boolean comboStreak = false;

	// And the current (ordinal) level and scores to reach to pass to the next
	// level
	private int level = 0;
	private int[] milestones = { 2000, 5000, 10000, 30000, 50000, 100000, 150000, 350000, 600000, 1000000 };

	// These two variables are used to limit the number of consecutive user
	// interactions
	private int inputDelta = 0;
	private int dropTimer = 0;
	private int[] dropTimeouts = { 1000, 900, 850, 800, 750, 700, 650, 600, 550, 500 };

	// We'll also keep an eye on the current and next piece
	private Piece nextPiece = null;
	private Piece currentPiece = null;

	// The coordinates for the current piece
	private int pieceX;
	private int pieceY;
	private int shadowPieceY;

	// Do we want shadow?
	private boolean enableShadow = true;
	
	// We'll keep track of the game logic with a state machine
	private STATE currentState = null;

	private enum STATE {
		START_GAME, NEW_PIECE, MOVING_PIECE, LINE_DESTRUCTION, PAUSE_GAME, GAME_OVER
	}

	/**
	 * 
	 * To construct a PlayingState all you need is a unique id
	 * 
	 * @param stateID The ID set for this state
	 * 
	 */
	public PlayingState(int stateID) {
		this.stateID = stateID;
	}

	/**
	 * 
	 * Getter for the current state ID
	 * 
	 */
	public int getID() {
		return stateID;
	}

	/**
	 * 
	 * This method gets called whenever the game enters the playing state
	 * 
	 */
	public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.enter(gc, sb);

		// And reset the state machine to the first state
		currentState = STATE.START_GAME;
	}

	/**
	 * 
	 * This method initialises the game for this state
	 * 
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		hud = ResourceManager.getImage("HUD");
	}

	/**
	 * 
	 * This method updates the viewport so that it corresponds to the user's
	 * input
	 * 
	 */
	@SuppressWarnings("unchecked")
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
			if (font == null) {
				font = new UnicodeFont("res/fonts/ITCKRIST.ttf", 20, true, false);
				font.addAsciiGlyphs();
				font.getEffects().add(new ColorEffect(java.awt.Color.white));
				font.addAsciiGlyphs();
				font.loadGlyphs();
			}

			// Then it's just a matter of doing the right action at the right
			// state
			switch (currentState) {
				case START_GAME:
					// Play the game start sound
					ResourceManager.getSound("GAME_START").play();

					// Here we'll just empty our pit
					pit.clear();

					// Reset the score and level
					score = 0;
					level = 0;
					scoreGained = 0;
					multiplier = level + 1;

					// And reset the timer
					dropTimer = dropTimeouts[level];

					// And move to the next state
					currentState = STATE.NEW_PIECE;
				break;

				case NEW_PIECE:
					// Here we'll need to generate a new piece
					generateNewPiece();

					// And find the location of its shadow
					calculateShadowPiece();
				break;

				case MOVING_PIECE:
					// Here we'll just move the piece
					movePiece(gc, sb, deltaT);

					// And find the location of its shadow
					calculateShadowPiece();
				break;

				case LINE_DESTRUCTION:
					// Here we'll check if any lines are full
					checkForFullLines();

					// And move to the next state
					currentState = STATE.NEW_PIECE;
				break;

				case PAUSE_GAME:
					if (gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
						currentState = STATE.MOVING_PIECE;
					}
				break;

				case GAME_OVER:
					// Play the game over sound
					ResourceManager.getSound("GAME_OVER").play();

					// Here we'll just save the high score
					Highscores.getInstance().addScore(score);

					// And go back to the main menu
					sb.enterState(Tetris.MAINMENU_STATE);
				break;
			}
		}
	}

	/**
	 * 
	 * This method draws a piece at a specific coordinate
	 * 
	 * @param p The piece to draw
	 * @param x The X coordinate of the piece
	 * @param y The Y coordinate of the piece
	 * 
	 */
	public void drawPieceAt(Piece p, int x, int y, boolean shadow) {
		if (p == null)
			return;

		Point[] rotation = p.getMatrix();

		// Draw each of the blocks for the piece
		for (int i = 0; i < 4; i++) {
			ResourceManager.getImage(shadow ? "TRANSPARENT_BLOCK" : "BLOCK").draw(PIT_X + (rotation[i].getX() + x) * BLOCK_SIZE,
					PIT_Y + (pit.getHeight() - 1 - (rotation[i].getY() + y)) * BLOCK_SIZE);
		}
	}

	/**
	 * 
	 * This method draws the shadow of the current piece on the pit
	 * 
	 */
	private void calculateShadowPiece() {
		shadowPieceY = pieceY;

		while (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, shadowPieceY)) {
			--shadowPieceY;
		}

		++shadowPieceY;
	}

	/**
	 * 
	 * This method renders the game viewport
	 * 
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// First we'll draw the hud, score and level
		hud.draw(0, 0);

		font.drawString(600, 25, String.valueOf(score), Color.orange);
		font.drawString(430, 190, String.valueOf(level + 1), Color.orange);
		font.drawString(530, 223, (level < milestones.length ? String.valueOf(milestones[level]) : "-"), Color.orange);
		font.drawString(480, 255, String.valueOf(multiplier), Color.orange);
		font.drawString(560, 290, String.valueOf(scoreGained), Color.orange);

		// We'll draw the pieces on top of it
		for (int line = 0; line < pit.getHeight(); line++) {
			for (int col = 0; col < pit.getWidth(); col++) {
				int block = pit.getBlockAt(col, line);

				if (block == 0)
					continue;

				ResourceManager.getImage("BLOCK").draw(PIT_X + col * BLOCK_SIZE, PIT_Y + (BLOCK_SIZE * (pit.getHeight() - line - 1)));
			}
		}

		// Draw the shadow only if we want it and it is far from the real piece
		if (enableShadow && pieceY - shadowPieceY > 6) {
			drawPieceAt(currentPiece, pieceX, shadowPieceY, true);
		}

		// Draw the current piece and the next one
		drawPieceAt(currentPiece, pieceX, pieceY, false);
		drawPieceAt(nextPiece, pit.getWidth() + 2, pit.getHeight() - 2, false);
	}

	/**
	 * 
	 * This method checks for valid piece movements and drops the piece at a
	 * regular time interval
	 * 
	 * @param gc The game container
	 * @param sb The state based game
	 * @param deltaT The time difference between the last update
	 * 
	 */
	private void movePiece(GameContainer gc, StateBasedGame sb, int deltaT) {
		// Decrease both our timers
		dropTimer -= deltaT;
		inputDelta -= deltaT;

		// Is it time to drop the piece?
		if (dropTimer < 0) {
			// Ok it is, but does the pit fit below?
			if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY - 1)) {
				--pieceY;
				ResourceManager.getSound("PIECE_FALL").play();
			}
			else {
				pit.insertPieceAt(currentPiece, pieceX, pieceY);
				currentState = STATE.LINE_DESTRUCTION;
			}

			// Reset the drop timeout
			dropTimer = dropTimeouts[level];
		}

		// Is it time to accept the user input?
		if (inputDelta < 0) {
			Input input = gc.getInput();

			// Are we trying to pause the game ?
			if (input.isKeyDown(Input.KEY_ESCAPE)) {
				currentState = STATE.PAUSE_GAME;
			}
			
			// Are we toggling shadow?
			if (input.isKeyDown(Input.KEY_S)) {
				enableShadow = !enableShadow;
				inputDelta = INPUT_TIMETOUT;
			}

			// Did we press left
			if (input.isKeyDown(Input.KEY_LEFT)) {
				// We'll try to go left
				if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX - 1, pieceY)) {
					--pieceX;
					inputDelta = INPUT_TIMETOUT;
					ResourceManager.getSound("PIECE_MOVE").play();
				}
			}

			// Did we press right
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				// We'll try to go right
				if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX + 1, pieceY)) {
					++pieceX;
					inputDelta = INPUT_TIMETOUT;

					ResourceManager.getSound("PIECE_MOVE").play();
				}
			}

			// Did we press down
			if (input.isKeyDown(Input.KEY_DOWN)) {
				// We'll ttry to go down
				if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY - 1)) {
					--pieceY;
					inputDelta = INPUT_TIMETOUT / 2;
				}
				else {
					// We tried to go down but we can't anymore so lets block
					// this piece and move on

					pit.insertPieceAt(currentPiece, pieceX, pieceY);
					currentState = STATE.LINE_DESTRUCTION;
					ResourceManager.getSound("PIECE_TOUCH").play();
				}
			}

			// Did we press up
			if (input.isKeyDown(Input.KEY_UP)) {
				// We'll try to rotate
				if (pit.doesPieceFitAt(currentPiece.getMatrix(currentPiece.getRotation() + 1), pieceX, pieceY)) {
					inputDelta = 3 * INPUT_TIMETOUT / 2;
					currentPiece.rotate();

					ResourceManager.getSound("PIECE_ROTATE").play();
				}
			}

			// Did we press space
			if (input.isKeyDown(Input.KEY_SPACE)) {
				// Drop the piece
				inputDelta = INPUT_TIMETOUT;

				pieceY = shadowPieceY;
				pit.insertPieceAt(currentPiece, pieceX, pieceY);
				currentState = STATE.LINE_DESTRUCTION;
				ResourceManager.getSound("PIECE_TOUCH").play();
			}
		}
	}

	/**
	 * 
	 * This method generates a new piece
	 * 
	 */
	private void generateNewPiece() {
		// If we don't have a current piece, we'll need to set a next one
		if (currentPiece == null)
			nextPiece = PieceFactory.generateRandomPiece();

		// Now we'll change the piece
		currentPiece = nextPiece;

		// We'll reset the position of the piece
		pieceX = 5;
		pieceY = 19;

		// If the piece fits there
		if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY)) {
			// We'll generate a new piece as next piece
			nextPiece = PieceFactory.generateRandomPiece();
			currentState = STATE.MOVING_PIECE;
		}
		else {
			// Otherwise it's game over
			currentState = STATE.GAME_OVER;
		}
	}

	/**
	 * 
	 * This method checks to see if there are any full lines and clears them
	 * while adding points to the player's score
	 * 
	 */
	private void checkForFullLines() {
		// We'll keep track of how many lines were destroyed
		int linesDestroyed = 0;

		// We'll try to destroy lines from the bottom to the top
		for (int line = 0; line < pit.getHeight();) {
			if (pit.isLineFull(line)) {
				pit.destroy(line);
				++linesDestroyed;
			}
			else {
				++line;
			}
		}

		// Depending on how many lines were destroyed we give out a score bonus
		// and a play a sound
		switch (linesDestroyed) {
			case 0:
				score += 10;

				// Reset multiplier to minimum
				multiplier = level + 1;
				comboStreak = false;
			break;

			case 1:
				scoreGained = 100;
				ResourceManager.getSound("SINGLE").play();
			break;

			case 2:
				scoreGained = 300;
				ResourceManager.getSound("DOUBLE").play();
			break;

			case 3:
				scoreGained = 600;
				ResourceManager.getSound("TRIPLE").play();
			break;

			case 4:
				scoreGained = 1000;
				ResourceManager.getSound("TETRIS").play();
			break;
		}

		if (linesDestroyed > 0) {
			// Increase multiplier on combo
			if (comboStreak) {
				multiplier += 1;
			}

			scoreGained *= multiplier;
			score += scoreGained;

			if (level < milestones.length && score > milestones[level]) {
				ResourceManager.getSound("LEVEL_UP").play();
				++level;
			}

			comboStreak = true;
		}
	}
}