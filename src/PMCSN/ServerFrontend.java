package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerFrontend implements Runnable {
	
	public static ArrayList<Job> fJobs = new ArrayList<>();
	
	public void run() {
		
    	Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false);
    	int index = 0;
    	double delay = Ssq2.START;   								/* delay in queue       */
        double arrival = Ssq2.START;
    	double response = Ssq2.START;
    	double service = Ssq2.START;                               /* service time         */
        double wait = Ssq2.START;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = Ssq2.START;
        double u = 0.8;

        int counter = 0;

        try {

        	//Qui manca il calcolo degli interarrivi
        	
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

                if (temp.getArrival() < departure) {
	            	  delay = departure - temp.getArrival(); 	// delay in queue 
	            } else {
	            	  delay = Ssq2.START;      							 // no delay   
	            }
				service = Arrival.getService(Ssq2.r, u);  // del job corrente
				response = wait + service;
				departure += temp.getArrival() + wait;    // time of departure del job corrente
				arrival = temp.getArrival() + response;
				job = new Job(temp.getInterarrival(), arrival, delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, true);
				totalService += service;
				wait = delay + service;		// attesa in coda del job successivo
    			Utils.prioSplitter(job);
    			index++;    			
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
        DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("Server Frontend\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		
        System.out.println("JOB F VALIDI: " + counter);
    }

}
