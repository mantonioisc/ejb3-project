package examples.stateful;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
@LocalBean
public class StatefulEjbImpl implements StatefulEjb {
	private static final Log log = LogFactory.getLog(Stateful.class);

	private int counter = 0;

	@Override
	public void doIT() {
		String yeah = "YEAH!";
		for (int i = 0; i < counter; i++) {
			yeah += yeah;
		}
		log.debug(yeah);
		counter++;
	}

	@Remove
	@Override
	public void close() {
		log.debug("Goodbye...");
	}
}
