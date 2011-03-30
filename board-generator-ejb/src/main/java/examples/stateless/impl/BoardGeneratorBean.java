package examples.stateless.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import examples.domain.Board;
import examples.domain.task.BoardBuilderTask;
import examples.stateless.BoardGeneratorLocal;
import examples.stateless.BoardGeneratorRemote;

@Stateless
public class BoardGeneratorBean implements BoardGeneratorLocal,
		BoardGeneratorRemote {
	private static final Log log = LogFactory.getLog(BoardGeneratorBean.class);

    public static final int MAX_WAITING_TIME = 15;
	
	private ExecutorService executor;
	
	@PostConstruct
	public void init(){
		log.debug("On init()");
		
		executor = Executors.newFixedThreadPool(3);
		
		log.debug("executor created");
	}

    @Override
    public Board createBoard() {
        return startBoardBuilderTask(new BoardBuilderTask());
    }

    @Override
    public Board createBoard(int size) {
        return startBoardBuilderTask(new BoardBuilderTask(size));
    }

    @Override
	public Board createBoard(int size, int snakes, int ladders) {
		return startBoardBuilderTask(new BoardBuilderTask(size,snakes, ladders));
	}

    private Board startBoardBuilderTask(BoardBuilderTask boardBuilderTask){
        log.debug("Starting board creation task");
        Future<Board> future = executor.submit(boardBuilderTask);

        Board board = null;

        try {
            board = future.get(MAX_WAITING_TIME, TimeUnit.SECONDS);
            log.debug("Board task completed successfully");
        } catch (InterruptedException e) {
            log.warn(e, e);
			Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            log.warn("Process failed abnormally", cause);
        } catch (TimeoutException e) {
            log.debug("Task took to long, canceling");
            cancelFutureTask(future);
            log.debug("Returning a board with default settings");
            board = startBoardBuilderTask(new BoardBuilderTask());
        }

        return board;
    }

    private void cancelFutureTask(Future<Board> future){
        boolean canceled = future.cancel(true);
        log.debug("Cancellation request issued: " + canceled);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.warn(e, e);
        }
        log.debug("already canceled? " + future.isCancelled());
    }
	
	@PreDestroy
	public void destroy(){
		log.debug("destroy()");
		
		log.debug("executor shut down");
		
		executor.shutdown();
		
		if(executor.isTerminated()){
			log.debug("Executor already terminated");
		}else{
			log.debug("Executor still working, waiting to shutdown 5 secs");
			try {
				boolean success = executor.awaitTermination(5, TimeUnit.SECONDS);
				log.debug("Executor terminated? " + success);
				if(!success){
					log.debug("Forcing shutdown");
					executor.shutdownNow();
					executor.awaitTermination(5, TimeUnit.SECONDS);
				}
			} catch (InterruptedException e) {
				log.warn("While waiting executor to stop", e);
				Thread.currentThread().interrupt();
			}
		}
		
	}

}
