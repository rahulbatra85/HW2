//import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier implements Runnable {

	CyclicBarrier cb;
	int[] a;
	int[] b;
	int[] c;
	int id,num;

	TestCyclicBarrier(CyclicBarrier cb,int[] a,int[] b,int[] c, int id,int num){
		this.cb = cb;
		this.a = a;
		this.b = b;
		this.c = c;
		this.id = id;
		this.num = num;
	}

	public void run() {
		int idx=0;

		//Write id at location a[id]
		a[id] = id;
		
		//Wait for all threads
		//If any goes through, then the sum below would be wrong
		try{
			idx = cb.await();
		}	
		catch (Exception e) {
            e.printStackTrace();
        }

		//Write the arrival index to array C
		c[id] = idx; //id and idx may not necessarily match

		//Now sum the entries of array A
		//If any thread didn't wait above, then it's sum will not be correct
		int sum=0;
		for(int i=0; i<a.length; i++){
			sum += a[i];
		}

		//Write to array B
		if(id == num - 1){
			b[0] = sum;		
		} 
        else {
			b[id+1] = sum;
		}
	}
    
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]), sum=0;
		int[] a = new int[N];	
		int[] b = new int[N];	
		int[] c = new int[N];	
		boolean fail = false;
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
			if(a[i] != i){
				System.out.println("ERROR: a[" + i + "] didn't match expected value. Observed:" + a[i] + " Expected: " + i); 
				fail = true;
			}
			if(b[i] != sum){
				System.out.println("ERROR: b[" + i + "] didn't match expected value. Observed:" + b[i] + " Expected: " + sum); 
				fail = true;
			}
			cSum += c[i];
		}
		if(cSum != sum){
			System.out.println("ERROR: cSum didn't match expected value. Observed:" + cSum + " Expected: " + sum); 
			fail = true;
		}
		
		if(!fail){
			System.out.println("PASS: Number of threads:" + N); 
		}
	}
}
