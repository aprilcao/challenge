
package school.dao;

import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import school.dao.JdbiEnrollmentDAO;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class DBQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiEnrollmentDAO.class);

    private final Jdbi jdbi;
    private final BlockingQueue<HandleConsumer> workload = new LinkedBlockingQueue<>();

    public DBQueue(Jdbi jdbi) {
        this.jdbi = jdbi;
        Executors.newSingleThreadExecutor().submit(()->consumeWorkload());
    }

    public void offer(HandleConsumer handleConsumer){
        workload.offer(handleConsumer);
    }

    private void consumeWorkload(){
        while(true) {
            try {
                // this will execute the db command.
                jdbi.useHandle(workload.take());
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }
}