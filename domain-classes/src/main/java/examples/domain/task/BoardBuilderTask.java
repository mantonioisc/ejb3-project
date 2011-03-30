package examples.domain.task;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import examples.domain.Board;
import examples.domain.BoardFactory;
import examples.domain.BoardValidator;

/**
 * This {@link Callable} task is used to abstract the long process
 * of creating a valid {@link Board}. It crates it using the random
 * {@link BoardFactory} and then validating it using the 
 * {@link BoardValidator} since depending the values passed to
 * the process it could never create a valid board, in that case the
 * task should be canceled. <br>
 * This task in able to be canceled being sensitive to thread 
 * interrupted status checking it using {@link Thread#isInterrupted()}
 * <br>
 * It also logs some information about the process 
 */
public class BoardBuilderTask implements Callable<Board> {
	private static final Log log = LogFactory.getLog(BoardBuilderTask.class);
	
	private static BoardFactory boardFactory = new BoardFactory();;
	private static BoardValidator boardValidator = new BoardValidator();
	
	private int size;
	private int snakes;
	private int ladders;
	
	public BoardBuilderTask(){
		
	}
	
	public BoardBuilderTask(int size){
		this.size = size;
	}

	public BoardBuilderTask(int size, int snakes, int ladders) {
		this.size = size;
		this.snakes = snakes;
		this.ladders = ladders;
	}

	@Override
	public Board call() throws Exception {
		Board board = null;
		boolean isValid = false;
		long attempts = 1;
		long start = System.currentTimeMillis();
		
		while(!isValid && !checkCancelStatus()){
			board = buildBoard();
			
			try{
				boardValidator.validateBoard(board);
				isValid = true;
				
				log.debug("SUCESS, board created after attemp #" + attempts);
				log.debug("Time taken to generate a valid board = " + (System.currentTimeMillis() - start));
			}catch(IllegalStateException ise){
				log.debug("Attempt failed #" + attempts);
				attempts = attempts + 1;
			}
		}
		
		return board;
	}
	
	private boolean checkCancelStatus(){
		boolean cancel = Thread.currentThread().isInterrupted();
		if(cancel) log.debug("!!! Task is canceled");
		return cancel;
	}
	
	private Board buildBoard(){
		Board board = null;
		
		if(size != 0 & snakes != 0 & ladders != 0){
			log.debug("Creating custom board");
			board = boardFactory.createBoard(size, snakes, ladders);
		}else if(size != 0){
			log.debug("Crating board with custom size: " + size);
			board = boardFactory.createBoard(size);
		}else{
			log.debug("Creating board with defaults");
			board = boardFactory.createBoard();
		}
		
		return board;
	}

}
