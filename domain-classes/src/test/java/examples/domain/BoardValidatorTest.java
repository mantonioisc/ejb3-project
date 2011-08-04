package examples.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class BoardValidatorTest {
	private static final Log log = LogFactory.getLog(BoardValidatorTest.class);
	
	private BoardValidator boardValidator = new BoardValidator();
	
	private Board validBoard = new Board() {
		private static final long serialVersionUID = 4965612897851526961L;
		
		private int size = 30;
		
		private Set<SnakeElement> snakes = Collections.singleton(new SnakeElement(10, 5));
		private Set<LadderElement> ladders = Collections.singleton(new LadderElement(20, 25));
		
		@Override
		public Set<SnakeElement> getSnakes() {
			return snakes;
		}
		
		@Override
		public int getSize() {
			return size;
		}
		
		@Override
		public Set<LadderElement> getLadders() {
			return ladders;
		}
		
		@Override
		public void describeBoard() {
			System.out.println("Test board");
		}
	};
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
	}
	
	@Test
	public void testValidBoard(){
		boardValidator.validateBoard(validBoard);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidBoardSizeValidation(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andReturn(2).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);	
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidSnakeDefinition(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.singleton(new SnakeElement(5, 10))).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidLadderDefinition(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.singleton(new LadderElement(15, 10))).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidLadderAtBeginning(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.singleton(new LadderElement(1, 10))).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidSnakeAtEnd(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.singleton(new SnakeElement(validBoard.getSize(), 1))).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andDelegateTo(validBoard).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	/**
	 * @see #validBoard
	 */
	@Test(expected = IllegalStateException.class)
	public void testInvalidDoubleFall(){
		Set<SnakeElement> snakes = new HashSet<SnakeElement>();
		snakes.add(new SnakeElement(10, 5));
		snakes.add(new SnakeElement(5, 1));
		
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andReturn(snakes).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andDelegateTo(validBoard).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	/**
	 * @see #validBoard
	 */
	@Test(expected = IllegalStateException.class)
	public void testInvalidDoubleJump(){
		Set<LadderElement> ladders = new HashSet<LadderElement>();
		ladders.add(new LadderElement(20, 25));
		ladders.add(new LadderElement(25, validBoard.getSize()));
		
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(ladders).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInvalidJumpAndFall(){
		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andReturn(Collections.singleton(new SnakeElement(5, 1))).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(Collections.singleton(new LadderElement(5, 10))).anyTimes();
		EasyMock.replay(mockBoard);
		
		boardValidator.validateBoard(mockBoard);
	}

	@Test(expected = IllegalStateException.class)
	public void testTwoFallsInTheSameCell(){
		Set<SnakeElement> snakes = new HashSet<SnakeElement>();
		snakes.add(new SnakeElement(10, 5));
		snakes.add(new SnakeElement(10, 1));

		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andReturn(snakes).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andDelegateTo(validBoard).anyTimes();
		mockBoard.describeBoard();
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(mockBoard);

		boardValidator.validateBoard(mockBoard);
	}

	@Test(expected = IllegalStateException.class)
	public void testTwoJumpsInTheSameCell(){
		Set<LadderElement> ladders = new HashSet<LadderElement>();
		ladders.add(new LadderElement(20, 25));
		ladders.add(new LadderElement(20, validBoard.getSize()));

		Board mockBoard = EasyMock.createMock(Board.class);
		EasyMock.expect(mockBoard.getSize()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getSnakes()).andDelegateTo(validBoard).anyTimes();
		EasyMock.expect(mockBoard.getLadders()).andReturn(ladders).anyTimes();
		mockBoard.describeBoard();
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(mockBoard);

		boardValidator.validateBoard(mockBoard);
	}

	/**
	 * Method that shows the basic cycle of a board creation. Were we attempt many times
	 * to create the board until a valid one is created. Be aware that this process can
	 * take quite some time depending on the configuration of the wanted board. The default
	 * takes short, but if some unreal values are chosen the process could hang. You can't
	 * expect to hold to many snakes and ladders in a small board, just doesn't make sense.
	 * <br>
	 * That's why it's annotated with {@link Ignore} because it is a random process and it
	 * can take long
	 */
	@Ignore
	@Test
	public void testBoardCreationAndValidation(){
		long start = System.currentTimeMillis();

		BoardFactory boardFactory = new BoardFactory();

		boolean isValid = false;

		int attempts = 1;

		while(!isValid){
			Board board = boardFactory.createBoard();

			try{
				boardValidator.validateBoard(board);
				log.debug("SUCESS, board created after attemp #" + attempts);
				isValid = true;
			}catch(IllegalStateException e){
				log.debug("Attempt failed #" + attempts++);
			}
		}

		log.debug("Time taken to generate a valid board= " + (System.currentTimeMillis() - start));
	}
	
}
