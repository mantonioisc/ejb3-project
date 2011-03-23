package examples.stateless.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import examples.domain.Board;
import examples.domain.Board;
import examples.stateless.BoardGeneratorLocal;
import examples.stateless.BoardGeneratorRemote;

public class BoardGeneratorBean implements BoardGeneratorLocal,
		BoardGeneratorRemote {
	private static final Log log = LogFactory.getLog(BoardGeneratorBean.class);
	
	private ExecutorService executor;
	
	private Set<Board> savedBoards;
	
	@PostConstruct
	public void init(){
		log.debug("On init()");
		
		savedBoards = new HashSet<Board>();
		
		executor = Executors.newFixedThreadPool(3);
		
		log.debug("executor created");
	}

	@Override
	public Board createBoard(int size, int snakes, int ladders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateBoard(Board board) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@PreDestroy
	public void destroy(){
		log.debug("destroy()");
		
		log.debug("executor shut down");
		
		executor.shutdown();
		
		if(executor.isShutdown()){
			log.debug("Executor already terminated");
		}else{
			log.debug("Executor still working, waiting to shutdown 5 secs");
			try {
				boolean success = executor.awaitTermination(5, TimeUnit.SECONDS);
				log.debug("Executor terminated? " + success);
				if(!success){
					log.debug("Forcing shutdown");
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				log.warn("While waiting executor to stop", e);
				Thread.currentThread().interrupt();
			}
		}
		
	}

}
