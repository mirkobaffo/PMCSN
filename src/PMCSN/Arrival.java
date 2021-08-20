package PMCSN;
import java.util.ArrayList;

public class Arrival {

	public static ArrayList<ArrayList<Job>> getArrival() {	
	
		int interval = 120; //abbiamo fasce orarie da 2 ore
		int totTime = 0;
		int hPrio = 1;
		int mPrio = 2;
		int lPrio = 3;
		
		ArrayList<ArrayList<Job>> queue = new ArrayList<>();
		ArrayList<Job> hQueue = new ArrayList<>();
		ArrayList<Job> mQueue = new ArrayList<>();
		ArrayList<Job> lQueue = new ArrayList<>();
		queue.add(hQueue);
		queue.add(mQueue);
		queue.add(lQueue);
		
		while(totTime < interval) {
			
			int priority = Generator.getRandomPriority(hPrio, lPrio);
			int interarrival = totTime + Generator.poissonGenerator();
			Job job = new Job(interarrival, priority);
			
			if (job.getPriority() == hPrio) {
				queue.get(hPrio).add(job);
			} else if (job.getPriority() == mPrio) {
				queue.get(mPrio).add(job);
			} else {
				queue.get(lPrio).add(job);
			}
			
			totTime = totTime + interarrival;
		}
		
		return queue;
	}
}
