package PMCSN;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ServerMaster implements Runnable {

	public static long end;

    public void run() {
    	
    	Job job = new Job(0.0,0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
    	int index = 0;
    	int counter = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0.0;
        double u = 5.45;
		
        try {

	        while(index < Ssq2.LAST){
	        
	        	TimeUnit.MICROSECONDS.sleep(1000);
	        	
	        	Job temp = null;
	        	/*if (job.getState() == true) { 	    //  gestione dei job in arrivo dal feedback 
	        		Utils.checkState(job);
	        	} else {*/
					if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && Ssq2.lQueue.isEmpty()) {
						index++;
						continue;
					} else if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && !Ssq2.lQueue.isEmpty() ) {
						temp = Ssq2.lQueue.get(0);
						Ssq2.lQueue.remove(temp);
					} else if (Ssq2.hQueue.isEmpty() && !Ssq2.mQueue.isEmpty()) {
						temp = Ssq2.mQueue.get(0);
						Ssq2.mQueue.remove(temp);
					} else if (!Ssq2.hQueue.isEmpty()) {
						temp = Ssq2.hQueue.get(0);
						Ssq2.hQueue.remove(temp);
					}
	        	//}
	            
	            if (temp == null) {
	            	index++;
	            	continue;
	            }
	            
	            if (temp.getArrival() < temp.getDeparture()) {
	            	  delay = temp.getDeparture() - temp.getArrival(); 	// delay in queue 
	            } else {
	            	  delay = 0.0;      							 // no delay   
	            }
				service = Arrival.getService(Ssq2.r, u);  // del job precedente
				wait = delay + service;		// del job successivo
				departure += temp.getArrival() + wait;    // time of departure del job precedente
				job = new Job(temp.getInterarrival(), temp.getArrival(), delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, temp.getResponse(), temp.getState());
				totalService += service;
				Utils.topicSplitter(job);
	            index++; 
	        }

	       /* System.out.println("coda frontend: \n");
			for (Job elem: ServerFrontend.fJobs) {
				System.out.println("job: " + elem.getSqn() + ": " + elem.getArrival());
				System.out.println(elem.getTopic() + "\n");
			}
			System.out.println("coda backend: \n");
			for (Job elem: ServerBackend.bJobs) {
				System.out.println("job: " + elem.getSqn() + ": " + elem.getArrival());
				System.out.println(elem.getTopic() + "\n");
			}
			System.out.println("coda wordpress: \n");
			for (Job elem: ServerWordpress.wJobs) {
				System.out.println("job: " + elem.getSqn() + ": " + elem.getArrival());
				System.out.println(elem.getTopic() + "\n");
			}
			System.out.println("coda blog: \n");
			for (Job elem: ServerBlog.rJobs) {
				System.out.println("job: " + elem.getSqn() + ": " + elem.getArrival());
				System.out.println(elem.getTopic() + "\n");
			}*/
	        //writer.close();
        
        /*} catch (IOException e) {
        	e.printStackTrace();*/
        } catch (InterruptedException e) {
    		e.printStackTrace();
    	}

		DecimalFormat f = new DecimalFormat("###0.00");
		System.out.println("Valore della wait dell'ultimo job: " + job.getWait());
		System.out.println("Valore del service dell'ultimo job: " + job.getService());
		System.out.println("Valore del delay dell'ultimo job: " + job.getDelay());
		System.out.println("Valore di index: " + index);

		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		
		end = System.currentTimeMillis();
		System.out.println("Duration: " + (end-Ssq2.start) + "milliseconds");
    }
      
}  


