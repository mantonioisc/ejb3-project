package examples.singleton.unit;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import examples.singleton.impl.DiceBean;


public class DiceBeanUnitTest {
	private static DiceBean diceBean;
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
		diceBean = new DiceBean();
		diceBean.init();
	}
	
	@Test
	public void testThrowDice(){
		int value = diceBean.throwDice();
		assertTrue(value <= 6);
		assertTrue(value >= 1);
	}
	
	@Test
	public void testThrowTwoDices(){
		int value = diceBean.throwTwoDices(); 
		assertTrue(value <= 12);
		assertTrue(value >= 2);
	}

}
