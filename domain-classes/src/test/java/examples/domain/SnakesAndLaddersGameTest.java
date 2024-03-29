package examples.domain;

import examples.domain.task.BoardBuilderTask;
import org.apache.log4j.PropertyConfigurator;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;

/**
 * 
 * @version 2 modified to test snake & ladder effect on game
 * also {@link #testSetChipLocation()} modified to take into account this effect
 */
public class SnakesAndLaddersGameTest {
	private static Board board = null;
	private static Board verySmallBoard = null;
	private static List<Player> players = null;
	private static Player player1 = null;
	
	@BeforeClass
	public static void setUp() throws InterruptedException, ExecutionException{
		//1st setup log4j
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
		//2nd create players
		players = new ArrayList<Player>();
		for(ChipColor chip:ChipColor.values()){
			Player player = new Player("Player " + (players.size() + 1), chip);
			players.add(player);
		}
		player1 = players.get(0);
		//2nd crate a board
		FutureTask<Board> task = new FutureTask<Board>(new BoardBuilderTask());
		task.run();
		board = task.get();

		FutureTask<Board> task1 = new FutureTask<Board>(new BoardBuilderTask(6, 1, 1));
		task1.run();
		verySmallBoard = task1.get();
	}
	
	@Test
	public void testDrawStatistics(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		
		game.setChipLocation(players.get(0).getChip(), 1);
		game.setChipLocation(players.get(1).getChip(), 2);
		game.setChipLocation(players.get(3).getChip(), 3);
		game.setChipLocation(players.get(4).getChip(), 4);
		
		game.drawStatics();
	}
	
	@Test
	public void testSetChipLocation(){
		int size = 15;
		int firstDiceThrow = 6;
		
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.<SnakeElement>emptySet());//just return something empty to move through
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.<LadderElement>emptySet());
		EasyMock.expect(mockBoard.getSize()).andReturn(size).anyTimes();
		EasyMock.replay(mockBoard);

		SnakesAndLaddersGame game = new SnakesAndLaddersGame(mockBoard, players);

		game.setChipLocation(player1.getChip(), firstDiceThrow);
		
		assertTrue(game.getPlaces().containsKey(player1.getChip()));
		
		assertEquals(new Integer(firstDiceThrow), game.getPlaces().get(player1.getChip()));
	}

	@Test
	public void testGetChipLocation(){
		int size = 15;
		int firstDiceThrow = 6;

		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.<SnakeElement>emptySet());//just return something empty to move through
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.<LadderElement>emptySet());
		EasyMock.expect(mockBoard.getSize()).andReturn(size).anyTimes();
		EasyMock.replay(mockBoard);

		SnakesAndLaddersGame game = new SnakesAndLaddersGame(mockBoard, players);

		game.setChipLocation(player1.getChip(), firstDiceThrow);

		assertEquals(firstDiceThrow, game.getChipLocation(player1.getChip()));

	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testInmutableGetPlaces(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		game.getPlaces().clear();
	}

	/**
	 * We need to use a really small board to get to the end in one dice throw
	 */
	@Test
	public void testIsGameFinished(){
		int diceThrow = 6;

		SnakesAndLaddersGame game = new SnakesAndLaddersGame(verySmallBoard, players);

		assertFalse(game.isGameFinished());

		game.setChipLocation(player1.getChip(), diceThrow);

		assertTrue(game.isGameFinished());

		//check that after game is finished new moves are ignored
		Map<ChipColor, Integer> copyBefore = new HashMap<ChipColor, Integer>(game.getPlaces());
		
		game.setChipLocation(players.get(1).getChip(), diceThrow);
		
		assertEquals(copyBefore, game.getPlaces());
	}

	@Test
	public void testSnakeEffect() {
		int snakeHead = 5;
		int snakeTail = 10;
		int size = 15;
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.singleton(new SnakeElement(snakeTail, snakeHead))).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.<LadderElement>emptySet()).anyTimes();
		EasyMock.expect(mockBoard.getSize()).andReturn(size).anyTimes();
		EasyMock.replay(mockBoard);

		SnakesAndLaddersGame game = new SnakesAndLaddersGame(mockBoard, players);

		game.setChipLocation(player1.getChip(), 5);
		game.setChipLocation(player1.getChip(), 5);


		ChipColor chipAt = game.getChipAt(snakeHead);

		assertEquals(player1.getChip(), chipAt);
	}

	@Test
	public void testLadderEffect(){
		int ladderBottom = 5;
		int ladderTop = 10;
		int size = 15;
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.<SnakeElement>emptySet()).anyTimes();//just return something empty to move through
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.singleton(new LadderElement(ladderBottom, ladderTop))).anyTimes();
		EasyMock.expect(mockBoard.getSize()).andReturn(size).anyTimes();
		EasyMock.replay(mockBoard);

		SnakesAndLaddersGame game = new SnakesAndLaddersGame(mockBoard, players);

		game.setChipLocation(player1.getChip(), 3);
		game.setChipLocation(player1.getChip(), 2);


		ChipColor chipAt = game.getChipAt(ladderTop);

		assertEquals(player1.getChip(), chipAt);

	}
}
