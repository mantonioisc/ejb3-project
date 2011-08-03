package examples.stateful.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.Collections;
import examples.domain.Board;
import examples.domain.ChipColor;
import examples.domain.Player;
import examples.domain.SnakesAndLaddersGame;
import examples.singleton.Dice;
import examples.stateful.SnakesAndLaddersLocal;
import examples.stateful.SnakesAndLaddersRemote;
import examples.stateless.BoardGenerator;

@Stateful
public class SnakesAndLaddersBean implements SnakesAndLaddersLocal,
		SnakesAndLaddersRemote {
	private static final Log log = LogFactory.getLog(SnakesAndLaddersBean.class);
	
	private boolean hasGameStarted = false;
	
	private List<Player> players = null;
	
	private Board board = null;
	
	private SnakesAndLaddersGame game;
	
	@EJB
	private BoardGenerator boardGenerator;
	
	@EJB
	private Dice dice;


	@Override
	public List<Player> startGame(List<String> playersName) {
		List<Player> players = new ArrayList<Player>();
		
		//do validation checks
		if(hasGameStarted){
			throw new IllegalStateException("The game has already started");
		}
		
		if(playersName.size() > ChipColor.values().length){
			throw new IllegalArgumentException("More players than chips");
		}
		
		//create Player objects
		for(int i = 0; i < ChipColor.values().length; i++){
			if(i > (playersName.size() - 1)){
				break;
			}
			Player player = new Player(playersName.get(i), ChipColor.values()[i]);
			players.add(player);
		}
		this.players = players;
		
		log.debug("Players: " + players);
		
		//create board
		board = boardGenerator.createBoard();
		log.debug("Board: ");
		board.describeBoard();
		game = new SnakesAndLaddersGame(board, players);
		hasGameStarted = true;
		
		return Collections.unmodifiableList(players);
	}

	@Override
	public int takeTurn(Player player) {
		int number = dice.throwDice();
		log.debug("Player : " + player.getName() + " got " + number);
		Integer oldLocation = game.getPlaces().get(player.getChip());
		if(oldLocation == null){
			oldLocation = 0;//it's the first shot
		}
		//TODO rename this method and implement it, so it considers the snakes and ladders
		game.setChipLocation(player.getChip(), number);
		int newLocation = game.getPlaces().get(player.getChip());
		
		log.debug("Player " + player.getName() + " passed from " + oldLocation + " to " + newLocation);
		
		return newLocation - oldLocation;
	}

	@Override
	public boolean isThereAWinner() {
		return game.isGameFinished();
	}

	@Override
	public Player getWinner() {
		Player winner = null;
		
		ChipColor winnerChip = game.getChipAt(game.getBoard().getSize());
		
		if(winnerChip != null){
			for(Player player:players){
				if(winnerChip.equals(player.getChip())){
					winner = player;
					break;
				}
			}
		}
		
		return winner;
	}

	/**
	 * This method should remove the EJB from the pool
	 */
	@Remove
	@Override
	public void endGame() {
		log.debug("Removing EJB instance from pool");
	}

}
