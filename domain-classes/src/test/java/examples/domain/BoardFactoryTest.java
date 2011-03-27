package examples.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;


public class BoardFactoryTest {
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
	}
	
	private BoardFactory boardFactory = new BoardFactory();
	
	@Test
	public void testBoardGeneration(){
		int size = 50;
		Board board = boardFactory.createBoard(size);
		
		assertEquals(size, board.getSize());
		assertNotNull(board.getSnakes());
		assertFalse(board.getSnakes().isEmpty());
		assertNotNull(board.getLadders());
		assertFalse(board.getLadders().isEmpty());
		
		board.describeBoard();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testImmutableSnakeSet(){
		Board board = boardFactory.createBoard();
		
		board.getSnakes().clear();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testImmutableLaddersSet(){
		Board board = boardFactory.createBoard();
		
		board.getLadders().clear();
	}
}
