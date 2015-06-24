//import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier implements Runnable {

	CyclicBarrier cb;
	int[] a;
	int[] b;
<<<<<<< HEAD
	int id, num;

	TestCyclicBarrier(CyclicBarrier cb, int[] a, int[] b, int id, int num){
		this.cb = cb;
		this.a = a;
		this.b = b;
		//this.id = id; //Thread id??? CB id??? 
=======
	int[] c;
	int id,num;

	TestCyclicBarrier(CyclicBarrier cb,int[] a,int[] b,int[] c, int id,int num){
		this.cb = cb;
		this.a = a;
		this.b = b;
		this.c = c;
		this.id = id;
>>>>>>> Working version using two phase
		this.num = num;
	}

	public void run() {
<<<<<<< HEAD
		//a[id] = id;
        int a = -1;
		try{
			a = cb.await(); //returns arrived index
=======
		int idx=0;

		//Write id at location a[id]
		a[id] = id;
		
		//Wait for all threads
		//If any goes through, then the sum below would be wrong
		try{
			idx = cb.await();
>>>>>>> Working version using two phase
		}	
		catch (Exception e) {
            e.printStackTrace();
        }
<<<<<<< HEAD
        
        System.out.println("\tThread done. Arrived index: " + a);
        
        
        /* COMMENTING OUT FOR NOW -- NOT CLEAR WHAT WE'RE TESTING FOR
=======

		//Write the arrival index to array C
		c[id] = idx;

		//Now sum the entries of array A
>>>>>>> Working version using two phase
		int sum=0;
		for(int i=0; i<a.length; i++){
			sum += a[i];
		}
<<<<<<< HEAD
		//System.out.println("Id: " + id + " SUM=" + sum);
		if(id == num - 1) {
=======

		//Write to array B
		if(id == num - 1){
>>>>>>> Working version using two phase
			b[0] = sum;		
		} 
        else {
			b[id+1] = sum;
		}
<<<<<<< HEAD
        */
        
=======

>>>>>>> Working version using two phase
	}
    
    // not working --- public int get_arrived() { return id; }

	public static void main(String[] args) {
		int N = 1000, sum=0;
		int[] a = new int[N];	
		int[] b = new int[N];	
<<<<<<< HEAD
        
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
        
		/* SEE ABOVE
         * for(int i=0; i<N; i++){
=======
		int[] c = new int[N];	
		TestCyclicBarrier[] tcb = new TestCyclicBarrier[N];
		CyclicBarrier cb = new CyclicBarrier(N);
		Thread[] t = new Thread[N];
		for(int i=0; i<N; i++){
			tcb[i] =new TestCyclicBarrier(cb,a,b,c,i,N);
			t[i] = new Thread(tcb[i]);
			sum += i;
		}

		for(int i=0; i<N; i++){
			t[i].start();
		}

		try {
			for(int i=0; i<N; i++){
				t[i].join();
			}
		} 
		catch (InterruptedException e) {
            e.printStackTrace();
        }

		int cSum = 0;	
		for(int i=0; i<N; i++){
>>>>>>> Working version using two phase
			if(a[i] != i){
				System.out.println("ERROR: a[" + i + "] didn't match expected value. Observed:" + a[i] + " Expected: " + i); 
			}
			if(b[i] != sum){
				System.out.println("ERROR: b[" + i + "] didn't match expected value. Observed:" + b[i] + " Expected: " + sum); 
			}
<<<<<<< HEAD
		}*/
        
=======

			cSum += c[i];
		}
		if(cSum != sum){
			System.out.println("ERROR: cSum didn't match expected value. Observed:" + cSum + " Expected: " + sum); 
		}
>>>>>>> Working version using two phase
	}
}
