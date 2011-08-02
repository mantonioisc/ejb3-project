package examples.stateless;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless
@LocalBean
public class StatelessEjbImpl implements StatelessEjb {
	private static final Log log = LogFactory.getLog(StatelessEjbImpl.class);
	/* (non-Javadoc)
	 * @see examples.stateless.StatelessEjb#doIt()
	 */
	@Override
	public void doIt(){
		log.debug("YEAH!");
	}
}
