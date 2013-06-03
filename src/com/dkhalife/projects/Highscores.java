package com.dkhalife.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JOptionPane;

/**
 * 
 * This class keeps track of all the High Scores
 * 
 * @author Dany Khalife
 * 
 */
public class Highscores {
	// Since this class is a singleton, we'll keep track of its unique instance
	private static Highscores instance = null;

	// We'll also store the scores
	private static SortedMap<Integer, String> scores = null;

	// And the size of the table
	private int size;

	/**
	 * 
	 * To construct this class, we simply need to know the size of the scores
	 * table
	 * 
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	private Highscores(int size) {
		this.size = size;

		// We'll just initialise our scores
		scores = new TreeMap<>(java.util.Collections.reverseOrder());

		try {
			scores = (TreeMap<Integer, String>) new ObjectInputStream(new FileInputStream(new File("scores.dat"))).readObject();
		} catch (IOException | ClassNotFoundException e) {
		}
	}

	/**
	 * 
	 * Again, since this class is a singleton we need to provide a global access
	 * point to its unique instance
	 * 
	 * @return The unique instance of this class
	 * 
	 */
	public static Highscores getInstance() {
		if (instance == null)
			instance = new Highscores(10);

		return instance;
	}

	/**
	 * 
	 * Getter for the scores
	 * 
	 * @return The scores
	 * 
	 */
	public SortedMap<Integer, String> getScores() {
		return scores;
	}

	/**
	 * 
	 * Setter for a new score
	 * 
	 * @param score The score to add
	 * 
	 */
	public void addScore(Integer score) {
		String name = (String) JOptionPane.showInputDialog(null, "Congratulations! You got a high score! \nEnter your name:", "Tetris", JOptionPane.PLAIN_MESSAGE, null, null, "");
		
		while(name.length() < 3){
			name += " ";
		}
		
		name = name.substring(0, 3).toUpperCase();
		
		scores.put(score, name);

		if (scores.size() > size) {
			scores.remove(scores.lastKey());
		}

		saveScores();
	}

	/**
	 * 
	 * This method saves the scores to the file
	 * 
	 */
	private void saveScores() {
		try {
			new ObjectOutputStream(new FileOutputStream(new File("scores.dat"))).writeObject(scores);
		} catch (IOException e) {
			System.out.println("HighScores file created.");
		}
	}
}