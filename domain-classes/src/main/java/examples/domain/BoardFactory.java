package examples.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Factory class that creates boards passing the number of elements
 * wanted in the factory methods. It has an internal private class
 * to implement the boards that it builds. 
 *
 */
public class BoardFactory {
	private static final Log log = LogFactory.getLog(BoardFactory.class);
	
	public static final int DEFAULT_SNAKES = 5;
	public static final int DEFAULT_LADDERS = 5;
	public static final int DEFAULT_SIZE = 30;
	
	public BoardFactory(){
		
	}
	
	/**
	 * Constructs a board using a brute force algorithm, it may create an
	 * invalid board.
	 * @param size size of the board, the number of elements on it
	 * @param snakes the number of the snakes in the board
	 * @param ladders the number of ladders to add in the board
	 * @return
	 */
	public Board createBoard(int size, int snakes, int ladders){
		BoardImpl board = new BoardImpl();
		
		//set board size
		log.debug("Creating a board of size: " + size);
		board.setSize(size);
		
		Random random = new Random();
		//add snakes
		log.debug("Adding random snakes #" + snakes);
		Map<Integer, Integer> randomSnakes = new HashMap<Integer, Integer>();
		board.setSnakes(randomSnakes);
		
		for(int i = 0 ; i < snakes ; i++){
			int tail = random.nextInt(size) + 1;
			int head = random.nextInt(size) + 1;
			randomSnakes.put(tail, head);
		}
		
		//add ladders
		log.debug("Adding random ladders #" + ladders);
		Map<Integer, Integer> randomLadders = new HashMap<Integer, Integer>();
		board.setLadders(randomLadders);
		
		for(int i = 0 ; i < ladders ; i++){
			int bottom = random.nextInt(size) + 1;
			int top = random.nextInt(size) + 1;
			randomLadders.put(bottom, top);
		}
		
		return board;
	}
	
	public Board createBoard(int size){
		log.debug("Using default snakes and ladders number");
		return createBoard(size, DEFAULT_SNAKES, DEFAULT_LADDERS);
	}
	
	public Board createBoard(){
		log.debug("Using defaults for everything");
		return createBoard(DEFAULT_SIZE, DEFAULT_SNAKES, DEFAULT_LADDERS);
	}
	
	

	/**
	 * Internal implementation of {@link Board}. The class is private so
	 * we don't expose the internal implementation to the clients, they
	 * may only get an instance of it through the factory.
	 * The setters in this class are for the factory use only. The getters
	 * expose unmodifiable maps, since a board once created it doesn't has
	 * to change
	 */
	private static class BoardImpl implements Board{
		private static final long serialVersionUID = -6731652065092190855L;
		private static final Log log = LogFactory.getLog(BoardImpl.class);
		
		/**
		 * Maps the beginning and end of a snake. The
		 * key is the tail of the snake, and the value
		 * the head. The <b>tail &gt; head</b>
		 */
		private Map<Integer, Integer> snakes;
		/**
		 * Maps the beginning and end of a ladder.
		 * The key is the bottom of the ladder and
		 * the value the top. The <b>bottom &lt; top</b>
		 */
		private Map<Integer, Integer> ladders;
		/**
		 * The size of the board. The board starts with 1
		 */
		private int size;
		/**
		 * Saves the location of a players chip within the
		 * table. 
		 */
		private Map<ChipColor, Integer> places;
		
		public BoardImpl() {
			places = new HashMap<ChipColor, Integer>();
			//the snakes & ladders map created and placed externally
		}

		public Map<Integer, Integer> getSnakes() {
			return Collections.unmodifiableMap(snakes);
		}
		
		public void setSnakes(Map<Integer, Integer> snakes) {
			this.snakes = snakes;
		}
		
		public Map<Integer, Integer> getLadders() {
			return Collections.unmodifiableMap(ladders);
		}
		
		public void setLadders(Map<Integer, Integer> ladders) {
			this.ladders = ladders;
		}
		
		public int getSize() {
			return size;
		}
		
		public void setSize(int size) {
			this.size = size;
		}	
		
		public Map<ChipColor, Integer> getPlaces() {
			return Collections.unmodifiableMap(places);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((ladders == null) ? 0 : ladders.hashCode());
			result = prime * result + size;
			result = prime * result + ((snakes == null) ? 0 : snakes.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BoardImpl other = (BoardImpl) obj;
			if (ladders == null) {
				if (other.ladders != null)
					return false;
			} else if (!ladders.equals(other.ladders))
				return false;
			if (size != other.size)
				return false;
			if (snakes == null) {
				if (other.snakes != null)
					return false;
			} else if (!snakes.equals(other.snakes))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "Board [snakes=" + snakes + ", ladders=" + ladders + ", size="
					+ size + "]";
		}

		@Override
		public void drawStatics() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("RANK").append("\t-\t").append("PLAYER").append("\t-\t").append("PLACE");
			
			TreeMap<ChipColor, Integer> orderedPlaces = 
				new TreeMap<ChipColor, Integer>(new Comparator<ChipColor>() {

					Map<ChipColor, Integer> unorderedMap = places;
					@Override
					public int compare(ChipColor player1, ChipColor palyer2) {
						int place1 = unorderedMap.get(player1);
						int place2 = unorderedMap.get(palyer2);
						return place1 - place2;
					}
					
				});
			
			int i = 1;
			for(Map.Entry<ChipColor, Integer> entry:orderedPlaces.entrySet()){
				sb.append(i).append("\t-\t").append(entry.getKey()).append("\t-\t").append(entry.getValue());
				i = i + 1;
			}
			
			
		}

		@Override
		public void setChipLocation(ChipColor chip, int location)
				throws IllegalArgumentException {
			if(location < 1 || location > size){
				log.debug("Invalid location: " + location);
				throw new IllegalArgumentException("Invalid location: " + location);
			}
			
			if(isGameFinished()){
				log.debug("The game is already finished. Ignoring move");
			}else{
				if(places.containsKey(chip)){
					log.debug("Previous location for chip(" + chip + "): " + places.get(chip));
				}
				
				log.debug("New location for chip(" + chip + "): " + places.get(chip));
				places.put(chip, location);
			}
			
		}

		@Override
		public boolean isGameFinished() {
			boolean result = false;
			for(Map.Entry<ChipColor, Integer> place:places.entrySet()){
				if(place.getValue().equals(this.size)){
					result = true;
					break;
				}
			}
			return result;
		}
		
	}

}
