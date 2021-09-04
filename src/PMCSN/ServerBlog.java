package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerBlog implements Runnable {

	public static ArrayList<Job> rJobs = new ArrayList<>();

	
	public void run() {
		Job job = new Job(0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
    	int index = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0;
		//service time
		double u = 0.4;

		int counter = 0;

        try {
        	
        	while(index < Ssq2.LAST){
                TimeUnit.MICROSECONDS.sleep(1000);
                if (rJobs.size() > 0) {
					job = rJobs.get(0);

					if (job == null) {
						System.out.println("Questo job è null e quindi forse per questo esplode tutto");
						index++;
						continue;
					} else {
						counter++;
						System.out.println("vuoto?" + rJobs.isEmpty() + " " + rJobs.get(0));
						rJobs.remove(0);
					}

                } else {
					index++;
					continue;
                }
                
                if (job.getArrival() < job.getDeparture()) {
              	  delay = job.getDeparture() - job.getArrival(); 	// delay in queue 
                } else {
              	  delay = 0.0;      								 // no delay   
                }
    			service = Arrival.getService(Ssq2.r, u);
    			wait = delay + service;
    			departure = job.getArrival() + wait;            	  // time of departure 
    			/*job.setDelay(job.getDelay() + delay);
    			job.setWait(job.getWait() + wait);
    			job.setService(job.getService() + service);*/
    			totalService = totalService + service;

    			index++;
    			
    			/* questo server non fa feedback */


    			//job.setState(true); // setto lo stato del job a true, cioè servito e da revisionare
    			//Utils.prioSplitter(job);
				DecimalFormat f = new DecimalFormat("###0.00");

				//System.out.println("   type of the job .. =   " + (job.getTopic()));
				//System.out.println("   interarrival time =   " + f.format(job.getInterarrival()));
				//System.out.println("   wait ............ =   " + f.format(job.getWait()));
				//System.out.println("   delay ........... =   " + f.format(job.getDelay()));
				//System.out.println("   service time .... =   " + f.format(job.getService()));
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }

		System.out.println("JOB R VALIDI: " + counter);

	}
}
