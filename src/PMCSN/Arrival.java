package PMCSN;
import java.util.ArrayList;

public class Arrival {
	
	public static double getArrival(double sarrival, Rng r, double m) {
		/* ------------------------------
		 * generate the next arrival time
		 * ------------------------------
		 */
//		    static double sarrival = START;

		    sarrival += Generator.exponentialGenerator(m, r);
		    return (sarrival);
		  }
	
	public static double getService(Rng r, double u) {
		/* ------------------------------
		 * generate the next service time
		 * ------------------------------
		 */
		    return (Generator.exponentialGenerator(u, r));
		  }


	/*public static ArrayList<ArrayList<Job>> getArrival() {	
	
		long LAST = 10000;                    
		double START = 0.0;                 
		double sarrival = START;    
		//int interval = 120; //abbiamo fasce orarie da 2 ore
		double totTime = 0;
		int hPrio = 1;
		int mPrio = 2;
		int lPrio = 3;
		
		Rng r = new Rng();
	    r.putSeed(123456789);
	    
		ArrayList<ArrayList<Job>> queue = new ArrayList<>();
		ArrayList<Job> hQueue = new ArrayList<>();
		ArrayList<Job> mQueue = new ArrayList<>();
		ArrayList<Job> lQueue = new ArrayList<>();
		queue.add(hQueue);
		queue.add(mQueue);
		queue.add(lQueue);
		
		while(totTime < LAST) {
			
			int priority = Generator.getRandomPriority(hPrio, lPrio);
			
			double interarrival = totTime + Generator.exponentialGenerator(2.0, r);
			Job job = new Job(interarrival, priority);
			
			if (job.getPriority() == hPrio) {
				queue.get(hPrio).add(job);
			} else if (job.getPriority() == mPrio) {
				queue.get(mPrio).add(job);
			} else {
				queue.get(lPrio).add(job);
			}
			totTime++;
			//totTime = totTime + interarrival;
		}
		
		return queue;
	}*/
}
