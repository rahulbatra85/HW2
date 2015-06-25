import java.util.Random;
public class TestProtocol implements Runnable {
    
    public enum Gender { MALE, FEMALE }
	BathroomProtocol BP;
	Gender gen;
	Random rand;
	
	TestProtocol(BathroomProtocol BP, Gender gen) {
		this.BP = BP;
		this.gen = gen; 
        this.rand = new Random();
	}

	public void run() {
	    String gen_out = null;
	    //System.out.println(" "+gen);
	    switch (gen) {
	        case MALE:
	            //System.out.println("in male");
	            BP.enterMale();
	            //do something manly
/*				int val = rand.nextInt(10) + 1;
				try {
					Thread.sleep(val);
				} catch (InterruptedException e){}*/
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

        //System.out.println("  "+gen_out+" thread done.");
	}
    

	public static void main(String[] args) {
		int N;
		int protocol = 0;
		if(args.length > 0){
			N = Integer.parseInt(args[0]);
			if(args.length > 1){
				protocol = Integer.parseInt(args[1]);
			}
		} else{
	        N = 1000;
		}
        Gender g;
        Random rand = new Random();

		BathroomProtocol bp;
		if(protocol == 1){ 
			bp = new SyncBathroomProtocol2();
            System.out.println ("Sync Protocol ");
		} else{
			bp = new LockBathroomProtocol();
            System.out.println ("Lock Protocol ");
		}
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

