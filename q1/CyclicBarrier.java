import java.util.concurrent.Semaphore;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks
    int numParties, arrived;
    Semaphore mutex;
    Semaphore[] s;

  public CyclicBarrier(int parties) {
    // TODO: The constructor for this CyclicBarrier
    this.numParties = parties;
    this.arrived = parties ; 

    mutex = new Semaphore(1);
    s = new Semaphore[numParties];

    for(int i=0; i<numParties; i++) {
        s[i] = new Semaphore(0, true); 
    }

  }

  public int await() throws InterruptedException {
    // Waits until all parties have invoked await on this barrier.
    // If the current thread is not the last to arrive then it is
    // disabled for thread scheduling purposes and lies dormant until
    // the last thread arrives.

    // Returns: the arrival index of the current thread, where index
    // (parties - 1) indicates the first to arrive and zero indicates
    // the last to arrive.

    int idx; //LOCAL, UNIQUE TO EACH THREAD
    mutex.acquire(); //Atomically decrease arrived
    
    arrived--; //Per description above //arrived++;
    idx = arrived;

    if (arrived == 0) { //numParties){
        System.out.println("Barrier Tripped; arrived = " + arrived);
        
        //Release all semaphores
        for(int i=1; i<numParties; i++) { //s[0] will never be used
            s[i].release();
        }

        //Reset arrived to numParties again so the barrier can be used again
        arrived = numParties;
        mutex.release();

    }
    else {
        System.out.println("arrived =  " + arrived);
        mutex.release(); // has to be released before setting itself in wait mode
        s[arrived].acquire(); //Clever ... Make threads wait // s[arrived-1].acquire();
    }
    
    return idx; 
  }

}
