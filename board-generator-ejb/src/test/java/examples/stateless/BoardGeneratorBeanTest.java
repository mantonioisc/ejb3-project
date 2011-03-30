package examples.stateless;

import static org.junit.Assert.*;

import javax.ejb.EJB;

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

import examples.domain.Board;
import examples.stateless.impl.BoardGeneratorBean;

@RunWith(Arquillian.class)
public class BoardGeneratorBeanTest {
	@EJB
	private BoardGeneratorLocal boardGeneratorLocal;
	
	@EJB
	private BoardGeneratorRemote boardGeneratorRemote;
	
	private static final String JAR_FILE_NAME = BoardGeneratorBean.class.getSimpleName() + ".jar";
	private static final Class<?>[] CLASSES = {
		BoardGenerator.class, BoardGeneratorLocal.class, BoardGeneratorRemote.class, BoardGeneratorBean.class
	};
	private static final Log log = LogFactory.getLog(BoardGeneratorBeanTest.class);
	
	@Deployment
	public static JavaArchive deploy(){
		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class, JAR_FILE_NAME);
		javaArchive.addClasses(CLASSES);
		javaArchive.addPackages(true, Board.class.getPackage());
		System.out.println(javaArchive.toString(true));
		log.debug(javaArchive.toString(true));
		return javaArchive;
	}
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
		
	}
	
	@Test
	public void testCreateBoardLocal(){
		log.debug("#testCreateBoardLocal()");
		assertNotNull(boardGeneratorLocal);
		Board board = boardGeneratorLocal.createBoard();
		assertNotNull(board);
	}
	
	@Test
	public void testCreateBoardRemote(){
		log.debug("#testCreateBoardRemote()");
		assertNotNull(boardGeneratorRemote);
		Board board = boardGeneratorRemote.createBoard();
		assertNotNull(board);
	}
	
	@Test
	public void testCreateBoardTimeoutLocal(){
		log.debug("#testCreateBoardTimeoutLocal()");
		assertNotNull(boardGeneratorLocal);
		Board board = boardGeneratorLocal.createBoard(30, 20, 20);
		assertNotNull(board);
	}
	
	@Test
	public void testCreateBoardTimeoutRemote(){
		log.debug("#testCreateBoardTimeoutRemote()");
		assertNotNull(boardGeneratorRemote);
		Board board = boardGeneratorRemote.createBoard(30, 20, 20);
		assertNotNull(board);
	}
}
