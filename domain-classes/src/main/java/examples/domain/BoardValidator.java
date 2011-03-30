package examples.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Validates {@link Board} instances produced
 * by the factory. This is created since we 
 * want lengthy process for the purpose of 
 * creating a example for stateless EJB. <br>
 * It validates that each snakes & ladders found
 * are valid. It also validates some rules
 * for a good game, checks for loops in the 
 * board, and other abnormal conditions 
 * using these rules: <br>
 * 	<ol>
 * 		<li>snake tail bigger than snake head</li>
 * 		<li>ladder bottom less than ladder top</li>
 * 
 * 		<li>1st cell no ladder bottom: this will start player further</li>
 * 		<li>last cell no tail of snake: this will avoid game ending</li>
 *
 * 		<li>a snake head can not be the tail of other snake: to avoid double falls</li>
 * 		<li>a ladder bottom can not be the top of other ladder: to avoid double jumps</li>
 * 
 *  	<li>a snake tail can not be in the same cell as a ladder bottom: which way will take in this case?</li>		 
 * 	</ol>
 * Actually having an snake head and a ladder top 
 * at the same cell doesn't cause any trouble. <br>
 * 
 * If any of this conditions is violated an exception 
 * is thrown, discarding the board completely, instead 
 * of trying to fix it. That's how we attempt to create 
 * a lengthy process.
 */
public class BoardValidator {
	private static final Log log = LogFactory.getLog(BoardValidator.class);
	/**
	 * Validates the board, if completes successfully then board
	 * is valid, is an exception is thrown then the board is
	 * invalid and should be discarded.
	 * @param board the board to be tested
	 * @throws IllegalStateException if an error in the board is found
	 */
	public void validateBoard(Board board) throws IllegalStateException{
		try{
			validateBoardDelegate(board);
			log.debug("Validation passed for board");
			board.describeBoard();
		}catch(IllegalStateException e){
			log.debug("Validation failed for board: " + e.getMessage());
			throw e;
		}
	}
	
	private void validateBoardDelegate(Board board) throws IllegalStateException{
		//1st test for board size
		if(board.getSize() <= 2){//the minimum valid, not really an exiting game
			throw new IllegalStateException("Board to small to be valid");
		}
		
		//2nd test snakes correctness
		for(SnakeElement snake : board.getSnakes()){
			if(snake.getTail() <= snake.getHead()){
				throw new IllegalStateException("Invalid snake definition: " + snake);
			}
		}
		
		//3rd test ladder correctness
		for(LadderElement ladder : board.getLadders()){
			if(ladder.getBottom() >= ladder.getTop()){
				throw new IllegalStateException("Invalid ladder definition: " + ladder);
			}
		}
		
		//4th, test no ladder at the beginning
		for(LadderElement ladder : board.getLadders()){
			if(ladder.getBottom() == 1){
				throw new IllegalStateException("There is an invalid ladder at the beginning");
			}
		}
		
		//5th, test no snake at the end
		for(SnakeElement snake : board.getSnakes()){
			if(snake.getTail() == board.getSize()){
				throw new IllegalStateException("There is an invalid snake at the end:");
			}
		}
		
		//6th, check for double fall
		for(SnakeElement outerSnake : board.getSnakes()){
			for(SnakeElement innerSnake : board.getSnakes()){
				if(outerSnake.getHead() == innerSnake.getTail()){
					throw new IllegalStateException("A snake can't start when other ends: " + outerSnake + " VS " + innerSnake);
				}
			}
		}
		
		//7th, check for double jump
		for(LadderElement outerLadder : board.getLadders()){
			for(LadderElement innerLadder : board.getLadders()){
				if(outerLadder.getBottom() == innerLadder.getTop()){
					throw new IllegalStateException("A ladder can't start when other ends: " + innerLadder + "VS " + outerLadder);
				}
			}
		}
		
		//8th, check for a fall and jump in the same cell
		for(LadderElement ladder : board.getLadders()){
			for(SnakeElement snake : board.getSnakes()){
				if(ladder.getBottom() == snake.getTail()){
					throw new IllegalStateException("We can't have the start of a snake and a ladder in the same place:" + snake + " VS " +ladder);
				}
			}
		}
		
		//looks valid
	}
}
