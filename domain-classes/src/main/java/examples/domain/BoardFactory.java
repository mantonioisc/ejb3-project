package examples.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
		Set<SnakeElement> randomSnakes = new HashSet<SnakeElement>();
		board.setSnakes(randomSnakes);
		
		for(int i = 0 ; i < snakes ; i++){
			int tail = random.nextInt(size) + 1;
			int head = random.nextInt(size) + 1;
			randomSnakes.add(new SnakeElement(Math.max(tail, head), Math.min(tail, head)));
		}
		
		//add ladders
		log.debug("Adding random ladders #" + ladders);
		Set<LadderElement> randomLadders = new HashSet<LadderElement>();
		board.setLadders(randomLadders);
		
		for(int i = 0 ; i < ladders ; i++){
			int bottom = random.nextInt(size) + 1;
			int top = random.nextInt(size) + 1;
			randomLadders.add(new LadderElement(Math.min(bottom, top), Math.max(bottom, top)));
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
		 * Represents the snakes elements of the board.
		 *  The <b>tail &gt; head</b>
		 */
		private Set<SnakeElement> snakes;
		/**
		 * Represents the ladder elements of the board.
		 * The <b>bottom &lt; top</b>
		 */
		private Set<LadderElement> ladders;
		/**
		 * The size of the board. The board starts with 1
		 */
		private int size;
		
		public BoardImpl() {
			//the snakes & ladders map created and placed externally
		}

		public Set<SnakeElement> getSnakes() {
			return Collections.unmodifiableSet(snakes);
		}
		
		public void setSnakes(Set<SnakeElement> snakes) {
			this.snakes = snakes;
		}
		
		public Set<LadderElement> getLadders() {
			return Collections.unmodifiableSet(ladders);
		}
		
		public void setLadders(Set<LadderElement> ladders) {
			this.ladders = ladders;
		}
		
		public int getSize() {
			return size;
		}
		
		public void setSize(int size) {
			this.size = size;
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
		public void describeBoard() {
			if (log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();

				sb.append("Board size: ").append(getSize()).append("\n\n");

				sb.append("Snakes: ").append(snakes.size()).append("\n");

				for(SnakeElement snake : snakes){
					sb.append("\t").append(snake).append("\n");
				}

				sb.append("\n\n");

				sb.append("Ladders: ").append(ladders.size()).append("\n");

				for(LadderElement ladder : ladders){
					sb.append("\t").append(ladder).append("\n");
				}

				log.debug(sb.toString());
			}
		}

		
	}//end BoardImpl

}
