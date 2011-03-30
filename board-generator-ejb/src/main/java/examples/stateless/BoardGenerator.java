package examples.stateless;

import examples.domain.Board;

public interface BoardGenerator {
    Board createBoard();
    Board createBoard(int size);
	Board createBoard(int size, int snakes, int ladders);
}
