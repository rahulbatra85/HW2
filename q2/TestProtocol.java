import java.util.Random;
public class TestProtocol implements Runnable {
    
    public enum Gender { MALE, FEMALE }
	BathroomProtocol BP;
	Gender gen;
	
	TestProtocol(BathroomProtocol BP, Gender gen) {
		this.BP = BP;
		this.gen = gen; 
	}

	public void run() {
	    String gen_out = null;
	    //System.out.println(" "+gen);
	    switch (gen) {
	        case MALE:
	           // System.out.println("in male");
	            BP.enterMale();
	            //do something manly
	            BP.leaveMale();
	            gen_out="Man";
	            break;
	        case FEMALE:
	            //System.out.println("in fem");
	        	BP.enterFemale();
	            //do something girly 
	            BP.leaveFemale();
	            gen_out="Fem";
	            break;
	    }  

        System.out.println("  "+gen_out+" thread done.");
	}
    

	public static void main(String[] args) {
        int N = 250;
        Gender g;
        Random rand = new Random();
        
		BathroomProtocol bp = new SyncBathroomProtocol();
        Thread[] t = new Thread[N];
		
		for(int x=0; x<N; x++) {
            System.out.println ("Iteration "+x);

		    for(int i=0; i<N; i++) {
		    
		        if (rand.nextInt(2) > 0) g = Gender.MALE;
		        else g = Gender.FEMALE;
		        
                t[i] = new Thread(new TestProtocol(bp, g));
                //System.out.println("Gen "+g);
		    }
			 
            for(int i=0; i<N; i++)
                t[i].start();  
		    
            try {
			    for(int i=0; i<N; i++)
				    t[i].join();
		    } 
		    catch (InterruptedException e) {
                e.printStackTrace();
            }
		} //end for iterations
	}//end main
}

