package com.dkhalife.projects;

import java.util.Random;

import org.newdawn.slick.geom.Point;

/**
 * 
 * This class implements a factory that constructs Tetris pieces
 * 
 * @author Dany Khalife
 * 
 */
public abstract class PieceFactory {
	// The factory knows each piece it can construct, and its possible rotation
	// states matrix
	static Point[][] TMatrix = { { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(-1, 0) }, { new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(0, 1) },
			{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 0) } };

	static Point[][] SMatrix = { { new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(-1, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, -1) }, { new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(-1, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, -1) } };

	static Point[][] ZMatrix = { { new Point(0, 0), new Point(-1, 0), new Point(0, -1), new Point(1, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(-1, 0), new Point(-1, -1) },
			{ new Point(0, 0), new Point(-1, 0), new Point(0, -1), new Point(1, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(-1, 0), new Point(-1, -1) } };

	static Point[][] OMatrix = { { new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(1, -1) },
			{ new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(1, -1) }, { new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(1, -1) },
			{ new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(1, -1) } };

	static Point[][] IMatrix = { { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
			{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) }, { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
			{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) } };

	static Point[][] LMatrix = { { new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, -1) },
			{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 1) }, { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(1, -1) } };

	static Point[][] JMatrix = { { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, -1) },
			{ new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(-1, -1) },
			{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, 1) }, { new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 1) } };

	/**
	 * 
	 * This method generates a random piece
	 * 
	 * @return A randomly generated piece
	 * 
	 */
	public static Piece generateRandomPiece() {
		Random r = new Random();

		// Pick a number from 0-7 and generate the correspoding piece
		switch (r.nextInt(7)) {
			case 0:
				return new Piece(IMatrix);

			case 1:
				return new Piece(JMatrix);

			case 2:
				return new Piece(LMatrix);

			case 3:
				return new Piece(SMatrix);

			case 4:
				return new Piece(TMatrix);

			case 5:
				return new Piece(OMatrix);

			case 6:
				return new Piece(ZMatrix);
		}

		return null;
	}
}
