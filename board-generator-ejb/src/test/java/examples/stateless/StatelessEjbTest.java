package examples.stateless;

import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class StatelessEjbTest {
	private static final Log log = LogFactory.getLog(StatelessEjbTest.class);
	
	@EJB
	private StatelessEjb statelessEjb;
	
	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
	}
	
	@Test
	public void testDoIt(){
		log.debug("TEST ME");
		assertNotNull(statelessEjb);
		statelessEjb.doIt();
	}
	
	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive javaArchive =
			ShrinkWrap.create(JavaArchive.class, StatelessEjb.class.getSimpleName() + ".jar");
		
		javaArchive.addPackage(StatelessEjbImpl.class.getPackage());
		
		log.debug(javaArchive);
		
		return javaArchive;
	}
}
