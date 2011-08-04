package examples.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Once created the board must not be able to change in the game
 */
public class SnakesAndLaddersGame {
	private static final Log log = LogFactory.getLog(SnakesAndLaddersGame.class);
	
	private Board board;
	/**
	 * Saves the location of a players chip within the
	 * table. 
	 */
	private Map<ChipColor, Integer> places;
	//TODO change the map from chip to player
	//TODO double index by place, list of player
	private List<Player> players;
	
	public SnakesAndLaddersGame(Board board, List<Player> players) {
		this.board = board;
		this.players = players;
		places = new HashMap<ChipColor, Integer>();
	}

	public Board getBoard() {
		return board;
	}

	/**
	 * Returns the player places in the board. Only
	 * modifiable with {@link #setChipLocation(ChipColor, int)}
	 * @return unmodifiable map
	 */
	public Map<ChipColor, Integer> getPlaces() {
		return Collections.unmodifiableMap(places);
	}
	
	public final List<Player> getPlayers() {
		return players;
	}

	/**
	 * Prints the players location within the board, for 
	 * debugging purposes. 
	 */
	public void drawStatics() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("RANK").append("\t-\t").append("PLAYER").append("\t-\t").append("PLACE").append("\n");
		
		//TODO change the map for other collection
		TreeMap<ChipColor, Integer> orderedPlaces = 
			new TreeMap<ChipColor, Integer>(new Comparator<ChipColor>() {

				Map<ChipColor, Integer> unorderedMap = places;
				@Override
				public int compare(ChipColor player1, ChipColor player2) {
					int place1 = unorderedMap.get(player1);
					int place2 = unorderedMap.get(player2);
					return place2 - place1;
				}
				
			});
		
		log.debug(places);
		orderedPlaces.putAll(places);
		log.debug(orderedPlaces);
		
		int i = 1;
		for(Map.Entry<ChipColor, Integer> entry:orderedPlaces.entrySet()){
			sb.append(i).append("\t-\t").append(entry.getKey()).append("\t-\t").append(entry.getValue()).append("\n");
			i = i + 1;
		}
		
		log.debug(sb.toString());
	}
	
	/**
	 * Sets a player location in the board. Does nothing if game has finished.
	 * @param chip the color of the chip
	 * @param diceValue the value to sum to the current place in the board to get the new place of the chip
	 * @throws IllegalArgumentException if the diceValue is bigger
	 * 		than 6 or less than 1
	 */
	public void setChipLocation(ChipColor chip, int diceValue)
			throws IllegalArgumentException {
		if(diceValue < 1 || diceValue > 6){
			log.debug("Invalid dice value: " + diceValue);
			throw new IllegalArgumentException("Invalid dice value: " + diceValue);
		}
		
		if(isGameFinished()){
			log.debug("The game is already finished. Ignoring move");
		}else{
			int currentLocation = 0;
			if(places.containsKey(chip)){
				currentLocation = places.get(chip);
			}
			log.debug("Previous location for chip(" + chip + "): " + currentLocation);
			log.debug("Dice value: " + diceValue);

			//sum the actual place and the dice value!!! How did I forget that?
			int newLocation = currentLocation + diceValue;

			//check if the movement is bigger, then bounce from the end of the board
			if(newLocation > getBoard().getSize()){
				newLocation = getBoard().getSize() - (newLocation - getBoard().getSize());
				log.debug("Bouncing back to: " + newLocation);
			}
			
			//take in count the snakes & ladders
			int effect = checkSnakeAndLadder(newLocation, board);
			if(effect != 0){
				log.debug("effect on chip location: " + effect);
				newLocation = newLocation + effect;
			}

			places.put(chip, newLocation);

			log.debug("New location for chip(" + chip + "): " + places.get(chip));
		}
		
	}

	/**
	 * Gets the location of the chip where 1 is the first cell and the last is the boar size
	 * @return 0 if the chip passed is not in the game
	 */
	public int getChipLocation(ChipColor chipColor){
		Integer location = places.get(chipColor);
		if (location == null) {
			location = 0;
		}
		return location;
	}


	/**
	 * Checks the board for snakes and ladder definitions and get the net
	 * effect on chip location
	 * @param location the current location to check
	 * @param board to get a reference to the snakes and ladders
	 * @return the negative/positive effect on the table
	 */
	private int checkSnakeAndLadder(int location, Board board) {
		int effect = 0;

		for (SnakeElement snakeElement : board.getSnakes()) {
			if(snakeElement.getTail() == location){
				log.debug("Snake is taking place, going down to: " + snakeElement.getHead());
				effect = snakeElement.getHead() - snakeElement.getTail();//where I am minus where I used to be
				return effect;
			}
		}

		for (LadderElement ladderElement : board.getLadders()) {
			if(ladderElement.getBottom() == location){
				log.debug("Ladder is taking effect, going up to: " + ladderElement.getTop());
				effect = ladderElement.getTop() - ladderElement.getBottom();
			}
		}
		
		return effect;
	}

	/**
	 * Returns true if there is a winner.
	 * @return true if the game has ended, false otherwise.
	 */
	public boolean isGameFinished(){
		boolean result = false;
		for(Map.Entry<ChipColor, Integer> place:places.entrySet()){
			if(place.getValue().equals(board.getSize())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public ChipColor getChipAt(int index){
		ChipColor chip = null;
		
		for(Map.Entry<ChipColor, Integer> place:places.entrySet()){
			if(place.getValue().equals(index)){
				chip = place.getKey();
				break;
			}
		}
		return chip;
	}
	
}
