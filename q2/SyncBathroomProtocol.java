// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol implements BathroomProtocol {

    public enum Gender { 
        MALE, FEMALE;        
        private Gender opposite;

        static {
            MALE.opposite = FEMALE;
            FEMALE.opposite = MALE;
        }

        public Gender getOpposite() {
            return opposite;
        }
    }
    
    boolean inUse;
    volatile int currUsers;
	volatile Gender turn, using;

	SyncBathroomProtocol() {
        using = null;
        inUse = false;
        currUsers = 0;
		turn = Gender.FEMALE;
	}
 
    public void enterMale() {
        increaseUsers(Gender.MALE);
    }

    public void leaveMale() {
        decreaseUsers(Gender.MALE);
    }

    public void enterFemale() {
        increaseUsers(Gender.FEMALE);
    }

    public void leaveFemale() {
        decreaseUsers(Gender.FEMALE);
    }
    
    protected synchronized void increaseUsers (Gender g) { 
        //System.out.println("\t+:U="+currUsers+" G="+using+" Busy="+inUse);
        //while ( inUse && g.getOpposite()==using ) {
        while ( (inUse && g.getOpposite()==using) || (g!=turn && g==using)  ) {
            try { 
                wait();
            }
            catch (Exception e) {}
        }

        inUse = true;
        currUsers++; 
        using = g;
        turn = g.getOpposite(); 
        //System.out.println("\t"+g+"+:numIn="+currUsers+" type"+using+" turn="+turn+" busy="+inUse);
   }
   
   protected synchronized void decreaseUsers(Gender g) { 
        //System.out.println("\t-:U="+currUsers+" G="+using+" Busy="+inUse);
        if (inUse && g == using) { // Only decrease if curr gender is using bathroom
            currUsers--;
            if (currUsers == 0) {
               inUse = false;
                using = null; 
               notifyAll();
            }
        }
        else { System.out.println("ERROR!!!!!"); } //WTF
        //System.out.println("\t"+g+"-:NumIn="+currUsers+" type="+using+" turn="+turn+" busy="+inUse);
   }
   
   
}
