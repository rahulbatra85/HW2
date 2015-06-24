// TODO
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
    
    Gender using = null;
    boolean inUse = false;
    int currUsers = 0;
    
    public void enterMale() {
    
        increaseUsers(Gender.MALE);
    }

    public void leaveMale() {
        decreaseUsers();
    }

    public void enterFemale() {
    
        increaseUsers(Gender.FEMALE);
    }

    public void leaveFemale() {
        decreaseUsers();
    }
    
    protected synchronized void increaseUsers (Gender g) { 
        //System.out.println("\t+:U="+currUsers+" G="+using+" Busy="+inUse);
        while (inUse && (g.getOpposite() == using)) {
            try { 
                wait();
            }
            catch (Exception e) {}
        }
        
        inUse = true;
        currUsers++; 
        using = g;
        //System.out.println("\t+:U="+currUsers+" G="+using+" Busy="+inUse);
   }
   
   protected synchronized void decreaseUsers() { 
        //System.out.println("\t-:U="+currUsers+" G="+using+" Busy="+inUse);
        currUsers--;
        if (currUsers == 0) {
            inUse = false;
            using = null;
            notifyAll();
        }
        //System.out.println("\t-:U="+currUsers+" G="+using+" Busy="+inUse);

   }
   
   
}
