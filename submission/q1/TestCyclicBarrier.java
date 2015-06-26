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
		this.num = num;
	}

	public void run() {
        int a = -1;
		try{
			a = cb.await(); //returns arrived index
		}	
		catch (Exception e) {
            e.printStackTrace();
        }        
        //System.out.println("\tThread done. Arrived index: " + a);       
	}
    
	public static void main(String[] args) {
		int N = 100, sum=0;
		int[] a = new int[N];	
		int[] b = new int[N];	
        
		TestCyclicBarrier[] tcb = new TestCyclicBarrier[N];
		CyclicBarrier cb = new CyclicBarrier(N);
        Thread[] t = new Thread[N];
		
        
		for(int x=0; x<N; x++) {
            System.out.println ("Iteration "+x);
			tcb[x] = new TestCyclicBarrier(cb,a,b,x,N);            
		
		    for(int i=0; i<N; i++) {
                // cb can be reused after all waiting threads have been released
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
             
            System.out.println ("Sum is "+sum);
            sum=0; 
		}
	}
}
