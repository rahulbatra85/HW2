// Use locks and condition variables to implement this bathroom protocol
import java.util.concurrent.locks.*;

public class LockBathroomProtocol3 implements BathroomProtocol {
     // declare the lock and conditions here
	ReentrantLock monitorLock;
	Condition maleQ, femaleQ;

	//State variables
	volatile int inF, inM, waitF, waitM;
	static final boolean MALE = false, FEMALE = true;
	volatile boolean turn;

	LockBathroomProtocol3() {
		monitorLock = new ReentrantLock();
		maleQ = monitorLock.newCondition();
		femaleQ = monitorLock.newCondition();

		inF = 0;
		inM = 0;
		waitF = 0;
		waitM = 0;
		turn = FEMALE;
	}

  public void enterMale() {
	monitorLock.lock(); //Acquire Lock
	
    try {
        waitM++;    //Increment male waiting count

        //Conditions not ok to enter
        // inUseFemale
        // inUseMale && female waiting and femaleTurn
        // !inUseMale and !inUseFemale and female waiting and femaleTurn
        while((inF > 0) || (inM > 0 && waitF > 0 && turn==FEMALE) || 
              (inM == 0 && inF == 0 && waitF > 0 && turn==FEMALE)) {
				maleQ.await();
        }

        //Inside bathroom
        waitM--; 	//Decrement male waiting count
        inM++;	//Increment male inside count	
        turn = FEMALE;
	} 
	catch(InterruptedException e) {
	}
	finally {
        //System.out.println("Enter Male EXIT: " + getState());
        monitorLock.unlock();//Release Lock
    }

  }

  public void leaveMale() {
	monitorLock.lock(); 	//Acquire Lock
	
	inM--;	//Decrement male inside count
	if (inM == 0) femaleQ.signalAll();
	
	monitorLock.unlock();	//Release Lock
  }

  public void enterFemale() {
	//Acquire Lock
	monitorLock.lock();

    try {
		waitF++; 	//Increment female waiting count

		//Conditions not ok to enter
		// inUseMale
		// inUseFemale && male waiting and maleTurn
		// !inUseMale and !inUseFemale and male waiting and maleTurn
		while((inM > 0) || (inF > 0 && waitM > 0 && turn==MALE) || 
		      (inM == 0 && inF == 0 && waitM > 0 && turn==MALE)) {
			femaleQ.await();
		}

		waitF--; 	//Decrement female waiting count
		inF++;	//Increment female inside count	
        turn = FEMALE;
	} 
    catch(InterruptedException e) {
    }
	finally {
		monitorLock.unlock(); 	//Release Lock
		
	} 

  }

  public void leaveFemale() {
	monitorLock.lock(); 	//Acquire Lock
	
	inF--;	//Decrement female inside count
	if (inF == 0) maleQ.signalAll(); //Signal male queue if all females are out
	
	monitorLock.unlock();	//Release Lock
  }

  public String getState(){
	return "inF,inM,waitF,waitM,turn: " + inF + "," + inM + 
	    "," + waitF + "," + waitM + "," + turn;
  }
}
