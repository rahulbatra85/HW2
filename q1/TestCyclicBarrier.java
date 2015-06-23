//import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier implements Runnable {

	CyclicBarrier cb;
	int[] a;
	int[] b;
	int id, num;

	TestCyclicBarrier(CyclicBarrier cb, int[] a, int[] b, int id, int num){
		this.cb = cb;
		this.a = a;
		this.b = b;
		//this.id = id; //Thread id??? CB id??? 
		this.num = num;
	}

	public void run() {
		//a[id] = id;
        int a=-1;
		try{
			a = cb.await(); //returns arrived index
		}	
		catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("\tThread arrived index " + a);
        
        
        /* COMMENTING OUT FOR NOW -- NOT CLEAR WHAT WE'RE TESTING FOR
		int sum=0;
		for(int i=0; i<a.length; i++){
			sum += a[i];
		}
		//System.out.println("Id: " + id + " SUM=" + sum);
		if(id == num - 1) {
			b[0] = sum;		
		} 
        else {
			b[id+1] = sum;
		}
        */
        
	}
    
    // not working --- public int get_arrived() { return id; }

	public static void main(String[] args) {
		int N = 100, sum=0;
		int[] a = new int[N];	
		int[] b = new int[N];	
        
		TestCyclicBarrier[] tcb = new TestCyclicBarrier[N];
		CyclicBarrier cb = new CyclicBarrier(N);
		
        
		for(int x=0; x<N; x++) {
            
			tcb[x] = new TestCyclicBarrier(cb,a,b,x,N);
            Thread[] t = new Thread[N];
		
		    for(int i=0; i<N; i++){
                // can be reused after all waiting threads have been released
                t[i] = new Thread(tcb[x]); // t[i] = new Thread(tcb[i]); 
                sum += i;      
		    }
			 
            for(int i=0; i<N; i++)
                t[i].start();  
		    
            try {
			    for(int i=0; i<N; i++){
				    t[i].join(); 
                    //sum -= t[i].get_arrived(); 
			    }
		    } 
		    catch (InterruptedException e) {
                e.printStackTrace();
            }
             
            System.out.println ("Iteration "+x+"\tsum is "+sum);
            sum=0; 
		}
        
		/* SEE ABOVE
         * for(int i=0; i<N; i++){
			if(a[i] != i){
				System.out.println("ERROR: a[" + i + "] didn't match expected value. Observed:" + a[i] + " Expected: " + i); 
			}
			if(b[i] != sum){
				System.out.println("ERROR: b[" + i + "] didn't match expected value. Observed:" + b[i] + " Expected: " + sum); 
			}
		}*/
        
	}
}
