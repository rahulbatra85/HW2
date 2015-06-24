import java.util.concurrent.Semaphore;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks 
	int numParties, arrived;
	Semaphore mutex;
	Semaphore barrier1, barrier2;
	
  public CyclicBarrier(int parties) {
    // TODO: The constructor for this CyclicBarrier
	this.numParties = parties; //Total number of threads
	this.arrived = 0;	//Initially zero
	mutex = new Semaphore(1);	//Ensured that arrive count is incremented atomically
	barrier1 = new Semaphore(0); //Initially Closed
	barrier2 = new Semaphore(1); //Initially Open
  }

  public int await() throws InterruptedException {
    // Waits until all parties have invoked await on this barrier.
    // If the current thread is not the last to arrive then it is
    // disabled for thread scheduling purposes and lies dormant until
    // the last thread arrives.

    // Returns: the arrival index of the current thread, where index
    // (parties - 1) indicates the first to arrive and zero indicates
    // the last to arrive.

	int idx;
	idx = phase1(); //Phase 1 ensures that all threads wait at the barrier
	phase2(); //Phase 2 resets the Cyclic barrier for reuse
	return idx;
  }

	int phase1() throws InterruptedException {
		int idx = 0;
		mutex.acquire();
		idx = arrived;
		arrived++;
	
		//Nth thread opens barrier 1 and closes barrier2	
		if(arrived == numParties){
			barrier2.acquire(); 
			barrier1.release(); //Release one thread 
		}
		mutex.release();

		barrier1.acquire(); //All the N threads will wait here
		barrier1.release(); //Each thread that passes through will signal one other thread
		return idx;
	}

	void phase2() throws InterruptedException {
		mutex.acquire();
		arrived--; //All threads decrement, resetting by one
		
		//Nth thread closes barrier1 and opens barrier 2(resets Cyclic Barrier)
		if(arrived == 0){
			barrier1.acquire(); //Close barrier 1
			barrier2.release(); //Let one thread go through barrier
		}
		mutex.release();

		barrier2.acquire(); //All the N thread will wait here after resetting
		barrier2.release(); //Each thread passes through will signal one other thread
	}

}
