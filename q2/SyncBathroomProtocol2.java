// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol2 implements BathroomProtocol {
	volatile int nF, nM, wF, wM;
	final int mTurn = 0, fTurn = 1;
	volatile int turn;

	SyncBathroomProtocol2(){
		nF = 0;
		nM = 0;
		wF = 0;
		wM = 0;
		turn = fTurn;
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
			wM = wM + 1;//Increment male waiting countDefine_Sequence

			//Conditions not ok to enter
			// inUseFemale
			// inUseMale && female waiting and femaleTurn
			// !inUseMale and !inUseFemale and female waiting and femaleTurn
			while((nF > 0) || (nM > 0 && wF > 0 && turn == fTurn) || (nM == 0 && nF == 0 && wF > 0 && turn == fTurn)){
				wait();
			}

			//Inside bathroom
			wM--; 	//Decrement male waiting count
			nM++;	//Increment male inside count	
			turn = fTurn;
		} catch(InterruptedException e) {};

	}    

	synchronized void SLeaveMale(){
		nM--;	//Decrement male inside count
		if (nM == 0) notifyAll();
	}    

	synchronized void SEnterFemale(){
		try {
			wF++; 	//Increment female waiting count

			//Conditions not ok to enter
			// inUseMale
			// inUseFemale && male waiting and maleTurn
			// !inUseMale and !inUseFemale and male waiting and maleTurn
			while((nM > 0) || (nF > 0 && wM > 0 && turn == mTurn) || (nM == 0 && nF == 0 && wM > 0 && turn == mTurn)){
				wait();
			}

			wF--; 	//Decrement female waiting count
			nF++;	//Increment female inside count	
			turn = mTurn;
		} catch(InterruptedException e) {};
	}    

	synchronized void SLeaveFemale(){
		nF--;	//Decrement female inside count
		if (nF == 0) notifyAll();
	}    
}
