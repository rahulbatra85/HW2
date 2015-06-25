// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol3 implements BathroomProtocol {
	volatile int inF, inM, waitF, waitM;
	static final boolean MALE = false, FEMALE = true;
	volatile boolean turn;

	SyncBathroomProtocol3(){
		inF = 0;
		inM = 0;
		waitF = 0;
		waitM = 0;
		turn = FEMALE;
	}
   
    public void enterMale() {
		SEnterMale();
    }

    public void leaveMale() {
		SLeaveMale();
    }

    public void enterFemale() {
		SEnterFemale();
    }

    public void leaveFemale() {
		SLeaveFemale();
    }

	synchronized void SEnterMale(){
		try {
			waitM++;

			//Conditions not ok to enter
			// inUseFemale
			// inUseMale && female waiting and femaleTurn
			// Empty     && female waiting and femaleTurn
			while( (inF > 0) || (inM > 0 && waitF > 0 && turn == FEMALE) || 
			       (inM == 0 && inF == 0 && waitF > 0 && turn == FEMALE) ) {
				wait();
			}

			//Inside bathroom
			waitM--;//Decrement male waiting count
			inM++;	//Increment male inside count	
			turn = FEMALE;
		} catch(InterruptedException e) {};

	}    

	synchronized void SLeaveMale(){
		inM--;	//Decrement male inside count
		if (inM == 0) notifyAll();
	}    

	synchronized void SEnterFemale(){
		try {
			waitF++; 	//Increment female waiting count

			//Conditions not ok to enter
			// inUseMale
			// inUseFemale && male waiting and maleTurn
			// Empty       && male waiting and maleTurn
			while((inM > 0) || (inF > 0 && waitM > 0 && turn == MALE) || 
			      (inM == 0 && inF == 0 && waitM > 0 && turn == MALE)){
				wait();
			}

			waitF--;//Decrement female waiting count
			inF++;	//Increment female inside count	
			turn = MALE;
		} catch(InterruptedException e) {};
	}    

	synchronized void SLeaveFemale(){
		inF--;	//Decrement female inside count
		if (inF == 0) notifyAll();
	}    
}
