// Use locks and condition variables to implement this bathroom protocol
import java.util.concurrent.locks.*;

public class LockBathroomProtocol implements BathroomProtocol {
  // declare the lock and conditions here
	ReentrantLock monitorLock = new ReentrantLock();
	Condition maleQ, femaleQ;

	//State variables
	volatile int nF, wF, nM, wM; //females inside, waiting females, males inside, waiting males
	volatile boolean femaleTurn, maleTurn;

	LockBathroomProtocol(){
		monitorLock = new ReentrantLock();
		maleQ = monitorLock.newCondition();
		femaleQ = monitorLock.newCondition();

		nF = 0;
		wF = 0;
		nM = 0;
		wM = 0;
		femaleTurn = true;//Females first
		maleTurn = false;
	}

  public void enterMale() {
	monitorLock.lock(); //Acquire Lock
	
	try {
		try {
			wM = wM + 1;//Increment male waiting count

			//Conditions not ok to enter
			// inUseFemale
			// inUseMale && female waiting and femaleTurn
			// !inUseMale and !inUseFemale and female waiting and femaleTurn
			while((nF > 0) || (nM > 0 && wF > 0 && femaleTurn) || (nM == 0 && nF == 0 && wF > 0 && femaleTurn)){
				maleQ.await();
			}

			//Inside bathroom
			wM--; 	//Decrement male waiting count
			nM++;	//Increment male inside count	
			femaleTurn = true;
			maleTurn = false;
		} finally {
			//System.out.println("Enter Male EXIT: " + getState());
			monitorLock.unlock();//Release Lock
		}
	} catch(InterruptedException e) {};
  }

  public void leaveMale() {
	monitorLock.lock(); 	//Acquire Lock
	
	nM--;	//Decrement male inside count
	if (nM == 0) femaleQ.signalAll();
	
	monitorLock.unlock();	//Release Lock
  }

  public void enterFemale() {
	//Acquire Lock
	monitorLock.lock();

	try{
		try {
			wF++; 	//Increment female waiting count

			//Conditions not ok to enter
			// inUseMale
			// inUseFemale && male waiting and maleTurn
			// !inUseMale and !inUseFemale and male waiting and maleTurn
			while((nM > 0) || (nF > 0 && wM > 0 && maleTurn) || (nM == 0 && nF == 0 && wM > 0 && maleTurn)){
				femaleQ.await();
			}

			wF--; 	//Decrement female waiting count
			nF++;	//Increment female inside count	
			maleTurn = true;
			femaleTurn = false;
		} 
		finally {
			monitorLock.unlock(); 	//Release Lock
		}
	} catch(InterruptedException e) {};
  }

  public void leaveFemale() {
	monitorLock.lock(); 	//Acquire Lock
	
	nF--;	//Decrement female inside count
	if (nF == 0) maleQ.signalAll(); //Signal male queue if all females are out
	
	monitorLock.unlock();	//Release Lock
  }

  public String getState(){
	return "nF,nM,wF,wM,femaleTurn,maleTurn " + nF + "," + nM + "," + wF + "," + wM + "," + femaleTurn + "," + maleTurn;
  }
}
