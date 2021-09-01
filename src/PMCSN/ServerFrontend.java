package PMCSN;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerFrontend implements Runnable {
	
	public static ArrayList<Job> fJobs = new ArrayList<>();
	
	public void run() {
		
    	Job job = new Job();
    	int index = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0;

        try {
        	
        	while(index < Ssq2.LAST){
                TimeUnit.MICROSECONDS.sleep(1000);
                if (fJobs.isEmpty()) {
                	continue;
                } else {
                	job = fJobs.get(0);
                	fJobs.remove(0);
                }
                
                if (job.getArrival() < job.getDeparture()) {
              	  delay = job.getDeparture() - job.getArrival(); 	// delay in queue 
                } else {
              	  delay = 0.0;      								 // no delay   
                }
    			service = Arrival.getService(Ssq2.r);
    			wait = delay + service;
    			departure = job.getArrival() + wait;            	  // time of departure 
    			job.setDelay(job.getDelay() + delay);
    			job.setWait(job.getWait() + wait);
    			job.setService(job.getService() + service);
    			totalService = totalService + service;

    			job.setState(true); // setto lo stato del job a true, cioè servito e da revisionare
    			Utils.prioSplitter(job);
    			
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        

        
    }

}
