package PMCSN;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerFrontend implements Runnable {
	
	public static ArrayList<Job> fJobs = new ArrayList<>();
	
	public void run() {
		
    	Job job; //= new Job(0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
    	int index = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0;
        double u = 0.8;

        int counter = 0;

        try {
        	
        	while(index < Ssq2.LAST) {
        		Job temp;
                TimeUnit.MICROSECONDS.sleep(1000);
                if (fJobs.size() > 0) {
					temp = fJobs.get(0);
					if (temp == null) {
						index++;
						continue;
					} else {
						counter++;
						System.out.println(temp + " " + temp.getArrival());
						fJobs.remove(temp);
					}
                } else {
					index++;
					continue;
                }

                if (temp.getArrival() < temp.getDeparture()) {
              	  delay = temp.getDeparture() - temp.getArrival(); 	// delay in queue
                } else {
              	  delay = 0.0;      								 // no delay   
                }
    			service = Arrival.getService(Ssq2.r, u);
    			wait = delay + service;
    			departure = temp.getArrival() + wait;            	  // time of departure
				totalService = totalService + service;

				/* setto lo stato a true */
				job = new Job(temp.getArrival(), temp.getDelay() + delay, departure, temp.getPriority(), temp.getTopic(), temp.getSqn(), temp.getWait() + wait, service, temp.getResponse(), true);
    			Utils.prioSplitter(job);
    			index++;    			
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        System.out.println("JOB F VALIDI: " + counter);
    }

}
