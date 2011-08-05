package examples.stateful.integration;

import examples.domain.Player;
import examples.singleton.Dice;
import examples.singleton.DiceLocal;
import examples.singleton.impl.DiceBean;
import examples.stateful.SnakesAndLadders;
import examples.stateful.SnakesAndLaddersLocal;
import examples.stateful.SnakesAndLaddersRemote;
import examples.stateful.impl.SnakesAndLaddersBean;
import examples.stateless.BoardGenerator;
import examples.stateless.BoardGeneratorLocal;
import examples.stateless.impl.BoardGeneratorBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.NoSuchEJBException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class SnakesAndLaddersBeanIntegrationTest {
	private static final Log LOG = LogFactory.getLog(SnakesAndLaddersBeanIntegrationTest.class);

	private static final List<String> players = new ArrayList<String>() {{
		add("Solid Snake");
		add("Kratos");
		add("Nathan Drake");
		add("Lara Croft");
		add("John Marston");
	}};

	@EJB
	private SnakesAndLaddersLocal gameLocal;

	@EJB
	private SnakesAndLaddersRemote gameRemote;

	@Deployment
	public static JavaArchive deploy() {
		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class, SnakesAndLaddersBean.class.getSimpleName() + ".jar");

		javaArchive.addClasses(SnakesAndLadders.class, SnakesAndLaddersLocal.class, SnakesAndLaddersRemote.class, SnakesAndLaddersBean.class);
		javaArchive.addClasses(Dice.class, DiceLocal.class, DiceBean.class);
		javaArchive.addClasses(BoardGenerator.class, BoardGeneratorLocal.class, BoardGeneratorBean.class);

		LOG.debug(javaArchive);
		return javaArchive;
	}

	@BeforeClass
	public static void setUp() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
	}

	@Test
	public void test() {
		assertNotNull(gameLocal);
		assertNotNull(gameRemote);
	}

	@Test
	public void testLocalEjb() {

		List<Player> playerList = gameLocal.startGame(players);

		while (!gameLocal.isThereAWinner()) {
			assertFalse(gameLocal.isThereAWinner());
			assertNull(gameLocal.getWinner());
			for (Player player : playerList) {
				gameLocal.takeTurn(player);
			}
		}

		Player winner = gameLocal.getWinner();

		assertNotNull(winner);

		LOG.debug("Winner! " + winner);

		gameLocal.endGame();

		try {
			gameLocal.startGame(players);
			fail("this bean should already being disposed");
		} catch (NoSuchEJBException e) {
			LOG.debug("local bean test success");
		}
	}

	@Test
	public void testRemoteEjb() {

		List<Player> playerList = gameRemote.startGame(players);

		while (!gameRemote.isThereAWinner()) {
			assertFalse(gameRemote.isThereAWinner());
			assertNull(gameRemote.getWinner());
			for (Player player : playerList) {
				gameRemote.takeTurn(player);
			}
		}

		Player winner = gameRemote.getWinner();

		assertNotNull(winner);

		LOG.debug("Winner! " + winner);

		gameRemote.endGame();

		try {
			gameRemote.startGame(players);
			fail("this bean should already being disposed");
		} catch (NoSuchEJBException e) {
			LOG.debug("remote bean test success");
		}
	}


}
