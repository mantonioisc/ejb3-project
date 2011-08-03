package examples.stateful;

import java.util.List;

import examples.domain.Player;

public interface SnakesAndLadders {
	/**
	 * Starts a game with a random board and also
	 * assigns randomly chips to players
	 * @param playersName a list of names
	 * @return a list of players
	 */
	List<Player> startGame(List<String> playersName);
	/**
	 * Throw a dice for a player
	 * @param player
	 * @return the number of places the player advanced, 
	 * or negative if he went back
	 */
	int takeTurn(Player player);
	/**
	 * To check if someone has already win
	 * @return true if the game has ended
	 */
	boolean isThereAWinner();
	/**
	 * The player that won the game
	 * @return null if no winner yet
	 */
	Player getWinner();
	/**
	 * finish the game and return resources
	 */
	void endGame();
}
