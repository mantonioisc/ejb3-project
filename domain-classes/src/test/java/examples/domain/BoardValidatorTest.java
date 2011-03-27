package examples.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

public class BoardValidatorTest {
	private BoardValidator boardValidator = new BoardValidator();
	
	private Board validBoard = new Board() {
		private static final long serialVersionUID = 4965612897851526961L;
		
		private int size = 30;
		
		private Set<SnakeElement> snakes = Collections.singleton(new SnakeElement(10, 5));
		private Set<LadderElement> ladders = Collections.singleton(new LadderElement(20, 25));

		@Override
		public void setChipLocation(ChipColor chip, int location)
				throws IllegalArgumentException {
			
		}
		
		@Override
		public boolean isGameFinished() {
			return false;
		}
		
		@Override
		public Set<SnakeElement> getSnakes() {
			return snakes;
		}
		
		@Override
		public int getSize() {
			return size;
		}
		
		@Override
		public Map<ChipColor, Integer> getPlaces() {
			return null;
		}
		
		@Override
		public Set<LadderElement> getLadders() {
			return ladders;
		}
		
		@Override
		public void drawStatics() {
			System.out.println("Test board");
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
}
