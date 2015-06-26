import java.util.concurrent.Semaphore;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks
    int numParties, arrived;
    Semaphore mutex, exec;
    Semaphore[] s;

  public CyclicBarrier(int parties) {
    // TODO: The constructor for this CyclicBarrier
    this.numParties = parties;
    this.arrived = parties ; 

    mutex = new Semaphore(1);
    exec = new Semaphore(1);
    s = new Semaphore[numParties];

    for(int i=0; i<numParties; i++) {
        s[i] = new Semaphore(0); 
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

    int idx; //LOCAL,UNIQUE TO EACH THREAD
    
    mutex.acquire(); 
    arrived--; //Per description above 
    idx = arrived;
    mutex.release();
    
    if (idx == 0) { 
        //System.out.println("Barrier Tripped; arrived = " + arrived);        
        //Release all semaphores, grab exec.
        exec.acquire();
        for(int i=1; i<numParties; i++) { //s[0] will never be used
            s[i].release();
        }
    }
    else {
        //System.out.println("arrived =  " + arrived);
        s[idx].acquire(); //Clever ... Make threads wait 
    }
    
    mutex.acquire();
    arrived++;
    if (arrived == numParties) //All threads are done
        exec.release(); //make sure it's ready for reuse
    mutex.release();
    
    return idx; 
  } 

    
}
