package PMCSN;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerFrontend implements Runnable {
	
	public static ArrayList<Job> fJobs = new ArrayList<>();
	
	public void run() {
		
    	Job job = new Job(0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
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
                TimeUnit.MICROSECONDS.sleep(1000);
                if (fJobs.size() > 0) {
					job = fJobs.get(0);
					//System.out.println("job del frontend: " + job.getTopic() + ":" + fJobs.get(0).getTopic());

					if (job == null) {
						System.out.println("Questo job è null e quindi forse per questo esplode tutto");
						index++;
						continue;
					} else {
						counter++;
						System.out.println("vuoto?" + fJobs.isEmpty() + " " + fJobs.get(0));
						fJobs.remove(0);
					}

                } else {
					//System.out.println("coda del frontend vuota");
					index++;
					continue;
                }
                if (job == null) {
                	System.out.println("Questo job per qualche motivo è null");
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

    			//job.setState(true); // setto lo stato del job a true, cioè servito e da revisionare
    			Utils.prioSplitter(job);
    			index++;    			
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        System.out.println("JOB F VALIDI: " + counter);

        /* STUPIDI QUESTO NON LO STAMPERÀ MAI PERCHÉ ALLA FINE DEL LOOP LA CODA SI SARÀ SVUOTATA
        *  (colpo di scena: e invece una volta l'ha stampato!!!)
        *  */
       /* for (Job elem: fJobs) {
    		System.out.println("è arrivato il job n. " + elem.getSqn() + "del tipo " + elem.getTopic());
    	}*/

        
    }

}
