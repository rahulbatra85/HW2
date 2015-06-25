// TODO
// Use locks and condition variables to implement this bathroom protocol
public class LockBathroomProtocol implements BathroomProtocol {
  // declare the lock and conditions here
	ReentrantLock monitorLock = new ReentrantLock();
	Condition maleW, femaleW;

	//State variables
	volatile int nF, wF, nM, wM; //females inside, waiting females, males inside, waiting males
	boolean femaleTurn, maleTurn;

	LockBathroomProtocol(){
		monitorLock = new ReentrantLock();
		maleW = monitorLock.newCondition();
		femaleW = monitorLock.newCondition();

		nF = 0;
		wF = 0;
		nM = 0;
		wM = 0;
		femaleTurn = true;
		maleTurn = true;
	}

	
	//Conditions ok to enter	
	// inUseMale && no female waiting
	// !inUseMale and !inUseFemale, and no female waiting
	// !inUseMale and !inUseFemale and female waiting and maleTurn

	//Conditions not ok to enter
	// inUseFemale
	// inUseMale && female waiting and femaleTurn
	// !inUseMale and !inUseFemale and female waiting and femaleTurn
  public void enterMale() {
	monitorLock.lock(); //Acquire Lock

	wM = wM + 1;//Increment male waiting count

	//Conditions not ok to enter
	// inUseFemale
	// inUseMale && female waiting and femaleTurn
	// !inUseMale and !inUseFemale and female waiting and femaleTurn
	while((nF > 0) || (nM > 0 && wF > 0 && femaleTurn) || (nM == 0 && nF == 0 and wF > 0 and femaleTurn)){
		maleW.await();
	}

	//Inside bathroom
	wM--; 	//Decrement male waiting count
	nM++;	//Increment male inside count	
	femaleTurn = true;

	monitorLock.unlock();//Release Lock
  }

  public void leaveMale() {
	monitorLock.lock(); //Acquire Lock
	//Decrement male inside count
	//If male inside==0 
		//inUseMale = false
		//signalAll females
	
	//Release Lock
  }

  public void enterFemale() {
	//Acquire Lock
	monitorLock.lock()

	//Increment female waiting count
	//Conditions ok to enter	
	// inUseFemale && no male waiting
	// !inUseMale and !inUseFemale, and no male waiting
	// !inUseMale and !inUseFemale and male waiting and femaleTurn

	//Conditions not ok to enter
	// inUseMale
	// inUseFemale && male waiting and maleTurn
	// !inUseMale and !inUseFemale and male waiting and maleTurn

	//Decrement female waiting count
	//Increment female inside count	
	//maleTurn = true

	//Release Lock
    }
  public void leaveFemale() {
	//Acquire Lock
	//Decrement female inside count
	//If female inside==0 
		//inUseFemale = false
		//signalAll male
	//Release Lock

  }
}
