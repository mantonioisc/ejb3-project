package examples.domain;

import java.io.Serializable;
import java.util.Map;

public interface Board extends Serializable{

	/**
	 * Return snakes locations
	 * @return unmodifiable map
	 */
	public abstract Map<Integer, Integer> getSnakes();
	/**
	 * Return ladders location
	 * @return unmodifiable map
	 */
	public abstract Map<Integer, Integer> getLadders();

	/**
	 * Returns the board size
	 * @return
	 */
	public abstract int getSize();
	
	/**
	 * Returns the player places in the board. Only
	 * modifiable with {@link #setChipLocation(ChipColor, int)}
	 * @return unmodifiable map
	 */
	public abstract Map<ChipColor, Integer> getPlaces();

	/**
	 * Prints the players location within the board, for 
	 * debugging purposes
	 */
	public abstract void drawStatics();
	
	/**
	 * Sets a player location in the board. Does nothing if game has finished.
	 * @param chip the color of the chip
	 * @param location the place in the board to place the chip
	 * @throws IllegalArgumentException if the location is bigger 
	 * 		than the size or less than 1
	 */
	public abstract void setChipLocation(ChipColor chip, int location) throws IllegalArgumentException;
	
	/**
	 * Returns true if there is a winner.
	 * @return true if the game has ended, false otherwise.
	 */
	public abstract boolean isGameFinished();
}