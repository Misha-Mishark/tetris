package com.dkhalife.projects;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

/**
 * 
 * This class represents the pit or playing area in Tetris
 * 
 * @author Dany Khalife
 * 
 */
public class Pit {
	// We'll need to remember the pit's width and height
	private int height;
	private int width;

	// And we'll also keep track of what is inside the pit
	private ArrayList<int[]> pit = null;

	/**
	 * 
	 * To construct a pit, one needs to know only its width and height
	 * 
	 * @param width The pit's width
	 * @param height The pit's height or depth
	 * 
	 */
	public Pit(int width, int height) {
		this.width = width;
		this.height = height;

		pit = new ArrayList<>();

		// We'll initialise the pit
		clear();
	}

	/**
	 * 
	 * Getter for the height
	 * 
	 * @return The pit's height
	 * 
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * Getter for the width
	 * 
	 * @return The pit's width
	 * 
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * Getter for a block
	 * 
	 * @param x The X coordinate of the block
	 * @param y The Y coordinate of the block
	 * 
	 * @return The corresponding block
	 * 
	 */
	public int getBlockAt(int x, int y) {
		return pit.get(y)[x];
	}

	/**
	 * 
	 * Setter for a block
	 * 
	 * @param x The X coordinate of the block
	 * @param y The Y coordinate of the block
	 * 
	 */
	private final void setBlockAt(int x, int y) {
		pit.get(y)[x] = 1;
	}

	/**
	 * 
	 * This method clears the pit
	 * 
	 */
	public void clear() {
		pit.clear();

		for (int y = 0; y < height; ++y) {
			addEmptyLine();
		}
	}

	/**
	 * 
	 * This method adds an empty line in the pit
	 * 
	 */
	private void addEmptyLine() {
		// By default java initialises the array's cells to 0
		pit.add(new int[width]);
	}

	/**
	 * 
	 * This method removes a line from the pit
	 * 
	 * @param index The index of the line to remove
	 * 
	 */
	public void destroy(int index) {
		// We remove the line
		pit.remove(index);

		// And replace it with another
		addEmptyLine();
	}

	/**
	 * 
	 * This method tests whether a piece fits at the given position
	 * 
	 * @param matrix The matrix for the current piece
	 * @param x The X coordinate of the test point
	 * @param y The Y coordinate of the test point
	 * @return True if the piece fits at that position. False otherwise
	 * 
	 */
	public boolean doesPieceFitAt(Point[] matrix, int x, int y) {
		for (int block = 0; block < 4; ++block) {
			Point blockPos = matrix[block];

			if (x + blockPos.getX() < 0) {
				return false;
			}

			if (x + blockPos.getX() >= width) {
				return false;
			}

			if (y + blockPos.getY() < 0) {
				return false;
			}

			if (y + blockPos.getY() >= height) {
				return false;
			}

			if (getBlockAt(x + (int) blockPos.getX(), y + (int) blockPos.getY()) == 1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * This method inserts a piece at the given position
	 * 
	 * @param piece The piece to tests if it fits
	 * @param x The X coordinate of the insertion point
	 * @param y The Y coordinate of the insertion point
	 * @return True if the insertion succeeded. False otherwise
	 * 
	 */
	public boolean insertPieceAt(Piece piece, int x, int y) {
		Point[] rotation = piece.getMatrix();

		if (!doesPieceFitAt(rotation, x, y)) {
			return false;
		}

		for (int block = 0; block < 4; ++block) {
			setBlockAt((int) rotation[block].getX() + x, (int) rotation[block].getY() + y);
		}

		return true;
	}

	/**
	 * 
	 * This method checks if a given line is full
	 * 
	 * @param line The index of the line
	 * @return True if the line is full. False otherwise
	 * 
	 */
	public boolean isLineFull(int line) {
		for (int i = 0; i < width; ++i) {
			if (getBlockAt(i, line) == 0) {
				return false;
			}
		}

		return true;
	}
}