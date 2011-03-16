package examples.singleton;


public interface Dice {
	static final int DICE_FACES = 6;
	int throwDice();
	int throwTwoDices();
}
