package com.dkhalife.projects;

import org.newdawn.slick.geom.Point;

/**
 * 
 * A piece is a set of 4 blocks aka tetranoid
 * 
 * @author Dany Khalife
 * 
 */
public class Piece {
	// We'll keep track of all the states for each rotation of this piece
	Point[][] rotations;

	// We'll also keep the current rotation ID, 0 being the first
	int currentRotation = 0;

	/**
	 * 
	 * To construct a piece we'll only need its rotations matrix
	 * 
	 * @param rotations The piece's rotation matrix
	 * 
	 */
	public Piece(Point[][] rotations) {
		this.rotations = rotations;
	}

	/**
	 * 
	 * Getter for the matrix
	 * 
	 * @return The current matrix for the piece
	 * 
	 */
	public Point[] getMatrix() {
		return rotations[currentRotation];
	}

	/**
	 * 
	 * Getter for the rotation
	 * 
	 * @return The current rotation
	 * 
	 */
	public int getRotation() {
		return currentRotation;
	}

	/**
	 * 
	 * Getter for the matrix
	 * 
	 * @param rotation The rotation for which to get the matrix
	 * @return The rotation matrix for that rotation
	 * 
	 */
	public Point[] getMatrix(int rotation) {
		return rotations[rotation % rotations.length];
	}

	/**
	 * 
	 * This method rotates the piece clockwise
	 * 
	 */
	public void rotate() {
		// We'll just increase the rotation index
		currentRotation = ++currentRotation % rotations.length;
	}
}