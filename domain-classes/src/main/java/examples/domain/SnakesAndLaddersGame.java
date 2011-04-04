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
				public int compare(ChipColor player1, ChipColor palyer2) {
					int place1 = unorderedMap.get(player1);
					int place2 = unorderedMap.get(palyer2);
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
	 * @param location the place in the board to place the chip
	 * @throws IllegalArgumentException if the location is bigger 
	 * 		than the size or less than 1
	 */
	public void setChipLocation(ChipColor chip, int location)
			throws IllegalArgumentException {
		if(location < 1 || location > board.getSize()){
			log.debug("Invalid location: " + location);
			throw new IllegalArgumentException("Invalid location: " + location);
		}
		
		if(isGameFinished()){
			log.debug("The game is already finished. Ignoring move");
		}else{
			if(places.containsKey(chip)){
				log.debug("Previous location for chip(" + chip + "): " + places.get(chip));
			}
			
			places.put(chip, location);
			log.debug("New location for chip(" + chip + "): " + places.get(chip));
		}
		
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
	
	
}
