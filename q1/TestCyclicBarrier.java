import java.util.concurrent.*;

public class TestCyclicBarrier implements Runnable {

	CyclicBarrier cb;
	int[] a;
	int[] b;
	int id,num;

	TestCyclicBarrier(CyclicBarrier cb,int[] a,int[] b,int id,int num){
		this.cb = cb;
		this.a = a;
		this.b = b;
		this.id = id;
		this.num = num;
	}

	public void run() {
		a[id] = id;
		try{
			cb.await();
		}	
		catch (InterruptedException e) {
            e.printStackTrace();
        }

		int sum=0;
		for(int i=0; i<a.length; i++){
			sum += a[i];
		}
		//System.out.println("Id: " + id + " SUM=" + sum);
		if(id == num - 1){
			b[0] = sum;		
		} else{
			b[id+1] = sum;
		}
	}

	public static void main(String[] args){
		int N = 10000, sum=0;
		int[] a = new int[N];	
		int[] b = new int[N];	
		TestCyclicBarrier[] tcb = new TestCyclicBarrier[N];
		CyclicBarrier cb = new CyclicBarrier(N);
		Thread[] t = new Thread[N];
		for(int i=0; i<N; i++){
			tcb[i] =new TestCyclicBarrier(cb,a,b,i,N);
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

		
		for(int i=0; i<N; i++){
			if(a[i] != i){
				System.out.println("ERROR: a[" + i + "] didn't match expected value. Observed:" + a[i] + " Expected: " + i); 
			}
			if(b[i] != sum){
				System.out.println("ERROR: b[" + i + "] didn't match expected value. Observed:" + b[i] + " Expected: " + sum); 
			}
		}
	}
}
