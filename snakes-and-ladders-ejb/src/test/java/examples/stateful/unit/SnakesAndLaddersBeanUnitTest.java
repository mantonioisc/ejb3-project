package examples.stateful.unit;

import examples.domain.Player;
import examples.singleton.impl.DiceBean;
import examples.stateful.impl.SnakesAndLaddersBean;
import examples.stateless.impl.BoardGeneratorBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SnakesAndLaddersBeanUnitTest {
	private static final Log LOG = LogFactory.getLog(SnakesAndLaddersBeanUnitTest.class);

	private static final List<String> players = new ArrayList<String>() {{
		add("Solid Snake");
		add("Kratos");
		add("Nathan Drake");
		add("Lara Croft");
		add("John Marston");
	}};

	private static final DiceBean DICE = new DiceBean();

	private static final BoardGeneratorBean BOARD_GENERATOR_BEAN = new BoardGeneratorBean();


	@BeforeClass
	public static void setUp() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));

		DICE.init();
		BOARD_GENERATOR_BEAN.init();
	}

	@Test
	public void testStartGame() {
		SnakesAndLaddersBean snakesAndLaddersBean = createSnakesAndLaddersBean();

		snakesAndLaddersBean.startGame(players);

		boolean winner = snakesAndLaddersBean.isThereAWinner();

		assertFalse(winner);

		Player winner1 = snakesAndLaddersBean.getWinner();

		assertNull(winner1);
	}

	@Test(expected = IllegalStateException.class)
	public void testDoNotRestartAGame() {
		SnakesAndLaddersBean snakesAndLaddersBean = createSnakesAndLaddersBean();

		try {
			snakesAndLaddersBean.startGame(players);
		} catch (Exception e) {
			fail("the first method call shouldn't fail");
		}

		snakesAndLaddersBean.startGame(players);

		fail("no start game to times");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoMorePlayersThanChips() {
		SnakesAndLaddersBean snakesAndLaddersBean = createSnakesAndLaddersBean();

		List<String> tooManyPlayers = new ArrayList<String>(players);
		tooManyPlayers.addAll(players);
		tooManyPlayers.addAll(players);
		snakesAndLaddersBean.startGame(tooManyPlayers);
	}

	@Test
	public void testTakeTurn() {
		SnakesAndLaddersBean snakesAndLaddersBean = createSnakesAndLaddersBean();

		List<Player> playerList = snakesAndLaddersBean.startGame(players);

		for (Player player : playerList) {
			int movement = snakesAndLaddersBean.takeTurn(player);
			assertTrue(movement != 0);
			LOG.debug(player.getName() + " moved " + movement);
		}

	}

	@Ignore
	@Test
	public void testFullGame() {
		SnakesAndLaddersBean snakesAndLaddersBean = createSnakesAndLaddersBean();

		List<Player> playerList = snakesAndLaddersBean.startGame(players);

		while (!snakesAndLaddersBean.isThereAWinner()) {
			for (Player player : playerList) {
				snakesAndLaddersBean.takeTurn(player);
			}
		}

		Player winner = snakesAndLaddersBean.getWinner();

		assertNotNull(winner);

		LOG.debug("Winner! " + winner);
	}

	/**
	 * Use reflection to avoid creating setter methods for {@link SnakesAndLaddersBean}
	 *
	 * @return an instance with their dependencies set
	 */
	private SnakesAndLaddersBean createSnakesAndLaddersBean() {
		try {
			SnakesAndLaddersBean snakesAndLaddersBean = new SnakesAndLaddersBean();

			Class<?> beanClass = SnakesAndLaddersBean.class;

			Field boardGenerator = beanClass.getDeclaredField("boardGenerator");
			boardGenerator.setAccessible(true);
			boardGenerator.set(snakesAndLaddersBean, BOARD_GENERATOR_BEAN);

			Field dice = beanClass.getDeclaredField("dice");
			dice.setAccessible(true);
			dice.set(snakesAndLaddersBean, DICE);

			return snakesAndLaddersBean;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@AfterClass
	public static void tearDown() {
		BOARD_GENERATOR_BEAN.destroy();
	}
}
