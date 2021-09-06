package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerBlog implements Runnable {

	public static ArrayList<Job> rJobs = new ArrayList<>();

	
	public void run() {
    	Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false, Ssq2.START);
    	int index = 0;
    	double delay = Ssq2.START;                                 /* delay in queue       */
        double arrival = Ssq2.START;
    	double response = Ssq2.START;
    	double service = Ssq2.START;                               /* service time         */
        double wait = Ssq2.START;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = Ssq2.START;
		//service time
		double u = 0.4;

		int counter = 0;

        try {
        	
        	while(index < Ssq2.LAST){
        		Job temp;
                TimeUnit.MICROSECONDS.sleep(1000);
                if (rJobs.size() > 0) {
					temp = rJobs.get(0);
					if (temp == null) {
						index++;
						continue;
					} else {
						counter++;
						System.out.println(rJobs.get(0) + " " + rJobs.get(0).getArrival());
						rJobs.remove(0);
					}
                } else {
					index++;
					continue;
                }
                
                if (temp.getArrival() < departure) {
	            	  delay = departure - temp.getArrival(); 	// delay in queue 
	            } else {
	            	  delay = Ssq2.START;      							 // no delay   
	            }
				service = Arrival.getService(Ssq2.r, u);  // del job corrente
				response = wait + service;
				departure += temp.getArrival() + wait;    // time of departure del job corrente
				arrival = temp.getArrival() + response;
				job = new Job(temp.getInterarrival(), arrival, delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, true, Ssq2.START);
				totalService += service;
				wait = delay + service;		// attesa in coda del job successivo

    			index++;
    			
    			/* questo server non fa feedback */

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

        DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("Server Blog\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		
		System.out.println("JOB R VALIDI: " + counter);

	}
}
