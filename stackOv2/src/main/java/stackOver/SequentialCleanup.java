package stackOver;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SequentialCleanup {
  // it is (obviously) important that this executor has one thread only
  ExecutorService executorCleanup = Executors.newFixedThreadPool(1);
  ExecutorService workerEs = Executors.newFixedThreadPool(20);

  public static void main(String[] args) throws Exception {
    SequentialCleanup o = new SequentialCleanup();
    o.go();
  }

  private void go() throws InterruptedException, ExecutionException {
    TicketProvider tp = new TicketProvider(executorCleanup);
    EventListener el = new EventListener(tp, workerEs);

    for (int i=0; i<20;i++) {
      el.onEvent();
    }
    Thread.sleep(10_000L);
    executorCleanup.shutdown();
    workerEs.shutdown();
  }
}

class EventListener {
  private TicketProvider tp;
  private ExecutorService workers;
  public EventListener(TicketProvider tp, ExecutorService workers) {
    this.tp = tp;
    this.workers = workers;
  }
  public CompletionStage<Void> onEvent() {
    Ticket ticket = tp.takeTicket();
    return runAsyncCode().thenRun(
        () -> {
          // on finish, have the cleanup service run our cleanup
          ticket.onWorkerFinish( ()-> {
            // put cleanup code here
            System.out.println("cleanups are orderer by ticket="+ticket);
          });
    });
  }
  private CompletionStage<Void> runAsyncCode() {
    CompletableFuture<Void> res = new CompletableFuture<>();
    workers.submit(
        ()-> {
          System.out.println("doing some work..");
          try { Thread.sleep(1000+(long)(Math.random()*1000)); } catch (Exception e) { }
          System.out.println("done");
          res.complete(null);
        }
        );
    return res;
  }
}

class Ticket {
  private int number;
  private CountDownLatch workerHasFinished = new CountDownLatch(1);
  private volatile Runnable cleanup;

  public Ticket(int number) {
    this.number = number;
  }
  // after the worker has finished the main task, it calls onWorkerFinish()
  public void onWorkerFinish(Runnable cleanup) {
    this.cleanup = cleanup;
    workerHasFinished.countDown();
  }

  // awaits end of the job, then cleans up
  public void waitThenCleanup() {
    try {
      if (workerHasFinished.await(2000L, TimeUnit.MILLISECONDS)) {
        System.out.println("cleanup ticket num=" + number);
        cleanup.run();
        System.out.println("end cleanup num=" + number);
      }
      else {
        System.out.println("cleanup skipped for ticket=" + number + ", time elapsed");
      }
    } catch (Exception e) {
    }
  }
  @Override
  public String toString() {
    return "Ticket["+number+"]";
  }
}

class TicketProvider {
  int ticketCounter = 0;
  private ExecutorService esCleanup;

  public TicketProvider(ExecutorService es) {
    this.esCleanup = es;
  }

  public synchronized Ticket takeTicket() {
    System.out.println("returning ticket " + ticketCounter);
    Ticket ticket = new Ticket(ticketCounter++);
    // enqueue for the cleanup
    esCleanup.submit(ticket::waitThenCleanup);
    return ticket;
  }
}