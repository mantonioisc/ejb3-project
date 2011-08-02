package examples.stateful;


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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class StatefulEjbTest {
	private static final Log log = LogFactory.getLog(StatefulEjbTest.class);

	@EJB
	private StatefulEjb statefulEjb;

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive javaArchive =
			ShrinkWrap.create(JavaArchive.class, StatefulEjbImpl.class.getSimpleName() + ".jar");

		javaArchive.addPackage(StatefulEjbImpl.class.getPackage());

		log.debug(javaArchive);

		return javaArchive;
	}

	@BeforeClass
	public static void setUp(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		PropertyConfigurator.configure(cl.getResource("log4j.properties"));
	}

	@Test
	public void testDoIt() {
		assertNotNull(statefulEjb);

		statefulEjb.doIT();

		statefulEjb.doIT();

		statefulEjb.doIT();
	}

	@Test(expected = NoSuchEJBException.class)
	public void testRemove() {
		assertNotNull(statefulEjb);

		statefulEjb.doIT();

		statefulEjb.close();

		statefulEjb.doIT();

		fail("It should not reach the end of the test");
	}

}
