package examples.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Board extends Serializable{

	/**
	 * Return snakes locations
	 * @return unmodifiable map
	 */
	public abstract Set<SnakeElement> getSnakes();
	/**
	 * Return ladders location
	 * @return unmodifiable map
	 */
	public abstract Set<LadderElement> getLadders();

	/**
	 * Returns the board size
	 * @return
	 */
	public abstract int getSize();
	
	/**
	 * Prints board information, for debugging purposes
	 */
	public abstract void describeBoard();
	
}