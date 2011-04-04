package examples.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import examples.domain.task.BoardBuilderTask;

public class SnakesAndLaddersGameTest {
	private static Board board = null;
	private static List<Player> players = null;
	
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
		//2nd crate a board
		FutureTask<Board> task = new FutureTask<Board>(new BoardBuilderTask());
		task.run();
		board = task.get();
	}
	
	@Test
	public void testDrawStatistics(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		
		game.setChipLocation(players.get(0).getChip(), board.getSize()/2);
		game.setChipLocation(players.get(1).getChip(), board.getSize()/3);
		game.setChipLocation(players.get(3).getChip(), board.getSize()/4);
		game.setChipLocation(players.get(4).getChip(), board.getSize()/4);
		
		game.drawStatics();
	}
	
	@Test
	public void testSetChipLocation(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		
		game.setChipLocation(players.get(0).getChip(), board.getSize()/2);
		
		assertTrue(game.getPlaces().containsKey(players.get(0).getChip()));
		
		assertEquals(new Integer(board.getSize()/2), game.getPlaces().get(players.get(0).getChip()));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testInmutableGetPlaces(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		game.getPlaces().clear();
	}
	
	@Test
	public void testIsGameFinished(){
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(board, players);
		
		assertFalse(game.isGameFinished());
		
		game.setChipLocation(players.get(0).getChip(), board.getSize());
		
		assertTrue(game.isGameFinished());
		
		Map<ChipColor, Integer> copyBefore = new HashMap<ChipColor, Integer>(game.getPlaces());
		
		game.setChipLocation(players.get(1).getChip(), board.getSize());
		
		assertEquals(copyBefore, game.getPlaces());
	}
}
