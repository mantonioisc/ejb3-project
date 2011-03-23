package examples.stateless;

import examples.domain.Board;

public interface BoardGenerator {
	Board createBoard(int size, int snakes, int ladders);
	boolean validateBoard(Board board);
}
