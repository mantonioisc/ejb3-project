package examples.singleton.impl;

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import examples.singleton.DiceLocal;
import examples.singleton.DiceRemote;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.WRITE)
public class DiceBean implements DiceLocal, DiceRemote {
	private static final Log log = LogFactory.getLog(DiceBean.class);
	
	
	private Random random;
	
	@PostConstruct
	public void init(){
		log.debug("init()");
		random = new Random();
	}

	@Override
	public int throwDice() {
		int value = random.nextInt(DICE_FACES);
		value++;
		log.debug("throwDice()=" + value);
		return value;
	}

	@Override
	public int throwTwoDices() {
		int value = random.nextInt(DICE_FACES) + random.nextInt(DICE_FACES);
		value++;
		value++;
		log.debug("throwTwoDices()=" + value);
		return value;
	}

}
