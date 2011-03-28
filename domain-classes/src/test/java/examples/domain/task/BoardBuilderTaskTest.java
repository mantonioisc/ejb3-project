package examples.domain.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import examples.domain.Board;
import examples.domain.BoardValidator;

public class BoardBuilderTaskTest {
	
	private static final Log log = LogFactory.getLog(BoardBuilderTaskTest.class);
	
	private static ExecutorService executorService;
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
		
		executorService = Executors.newSingleThreadExecutor();
	}
	
	@AfterClass
	public static void tearDown(){
		executorService.shutdown();
	}
	
	@Test
	public void testSucessfullBoardCreation() throws InterruptedException, ExecutionException{
		log.debug("Submiting task");
		
		Future<Board> future = executorService.submit(new BoardBuilderTask());
		
		log.debug("Waiting for task to end");
		
		Board board = future.get();
		
		log.debug("Task ended");
		
		assertNotNull(board);
		
		BoardValidator validator = new BoardValidator();
		
		log.debug("About to validate board");
		
		try{//make sure board received is valid
			validator.validateBoard(board);	
		}catch(IllegalStateException e){
			fail("Board returned from service should be valid");
		}
		
		log.debug("Board successfuly created");
	}
	
	/**
	 * Tests task cancellation using Thread.interrupt
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testCancelBoardCreation() throws InterruptedException, ExecutionException{
		log.debug("Submiting task with very unrealistic arguments");
		
		Future<Board> future = executorService.submit(new BoardBuilderTask(30, 15, 15));
		
		log.debug("Giving few seconds to task to finish");
		
		TimeUnit.SECONDS.sleep(1);
		
		assertFalse(future.isDone());
		
		log.debug("Task will never end, so we cancell it");
		
		boolean cancelled = future.cancel(true);//yes, interrupt it
		
		assertTrue(cancelled);
		
		assertTrue(future.isCancelled());
		
		log.debug("Board canceled successfuly");
	}
	
}
