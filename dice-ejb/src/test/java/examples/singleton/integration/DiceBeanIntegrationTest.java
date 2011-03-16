package examples.singleton.integration;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import examples.singleton.Dice;
import examples.singleton.DiceLocal;
import examples.singleton.DiceRemote;
import examples.singleton.impl.DiceBean;

@RunWith(Arquillian.class)
public class DiceBeanIntegrationTest {
	private static final Log log = LogFactory.getLog(DiceBeanIntegrationTest.class);
	private static final String JAR_FILE_NAME = DiceBean.class.getSimpleName() + ".jar";
	private static final Class<?>[] classes = {
		Dice.class, DiceLocal.class, DiceRemote.class, DiceBean.class
	};
	
	@EJB
	private DiceLocal diceLocal;
	
	@EJB
	private DiceRemote diceRemote;
	
	@Deployment
	public static JavaArchive deploy(){
		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class, JAR_FILE_NAME);
		javaArchive.addClasses(classes);
		
		log.debug(javaArchive.toString(true));
		return javaArchive;
	}
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
		
	}
	
	@Test
	public void testThrowDice(){
		int value = 0;
		
		value = diceLocal.throwDice();
		
		assertTrue(value <=6);
		assertTrue(value >=1);
		
		value = diceRemote.throwDice();
		
		assertTrue(value <=6);
		assertTrue(value >=1);
		
	}
	
	@Test
	public void testThrowTwoDices(){
		int value = 0;
		
		value = diceLocal.throwTwoDices();
		
		assertTrue(value <=12);
		assertTrue(value >=2);
		
		value = diceRemote.throwTwoDices();
		
		assertTrue(value <=12);
		assertTrue(value >=2);
	}
	
}
