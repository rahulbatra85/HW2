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
	this.arrived = 0;
	mutex = new Semaphore(1);
	s = new Semaphore[numParties];
	for(int i=0; i<numParties; i++){
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

	int idx;
	//Atomically increase partiesArrived
	mutex.acquire();
	idx = arrived;
	arrived++;
	
	if (arrived == numParties){
		//release all semaphores
		for(int i=0; i<numParties; i++){
			s[i].release();
		//	System.out.println("Barrier Tripped");
		}
		mutex.release();
		return idx;
	} else{
		//System.out.println("Idx " + idx + " arrived");
		mutex.release();
		s[arrived-1].acquire();
		return idx;
	}
  }
}
