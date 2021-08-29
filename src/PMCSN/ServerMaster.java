package PMCSN;

import java.text.DecimalFormat;

public class ServerMaster implements Runnable {
	
	/*private final ThreadObject threadObject;

    public void workingTask(ThreadObject workingObject) {
        threadObject = workingObject;
    }*/


    public void run() {
    	
    	Job job;
    	int index = 0;
    	int counter = 0;
    	
        while(index < Ssq2.LAST){
        	System.out.println("Job totali: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size());
            System.out.println("ciao Master" + Thread.currentThread().getId());
        
            if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && Ssq2.lQueue.isEmpty()) {
      		  // continua
            	System.out.println("Tutte vuote");
            	if (counter == 10) {
            		System.exit(0);
            	}
            	counter++;
            	continue;
	      	} else if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && !Ssq2.lQueue.isEmpty()) {
	      		  // prendi il primo elemento di lQueue, mandalo al server e rimuovilo
	      		System.out.println("Job a bassa priorità");
	      		job = Ssq2.lQueue.get(0);
	      		Ssq2.lQueue.remove(0);
	      	} else if (Ssq2.hQueue.isEmpty() && !Ssq2.mQueue.isEmpty()) {
	      		  // prendi il primo elemento di mQueue, mandalo al server e rimuovilo
	        		System.out.println("Job a media priorità");
	        		job = Ssq2.mQueue.get(0);
		      		Ssq2.mQueue.remove(0);
	      	} else if (!Ssq2.hQueue.isEmpty()) {
	      		  // prendi il primo elemento di hQueue, mandalo al server e rimuovilo
	        		System.out.println("Job ad alta priorità");
	        		job = Ssq2.hQueue.get(0);
	        		//System.out.println("1: " + Ssq2.hQueue.get(0).getSqn());
        			Ssq2.hQueue.remove(0);
	      	}
            index++;
	          
        }

      	 /* if (arrival < departure)
      	    delay = departure - arrival;         // delay in queue    
      	  else
      	    delay = 0.0;                         // no delay          
      	  service = Arrival.getService(r);
      	  wait = delay + service;
      	  departure = arrival + wait;              // time of departure 
      	  job.setDelay(job.getDelay() + delay);
      	  job.setWait(job.getWait() + wait);
      	  job.setService(job.getService() + service);
      	  job.setPriority(priority);

      	  index++;
          }
          job.setInterarrival(arrival - START);

          DecimalFormat f = new DecimalFormat("###0.00");

          System.out.println("\nfor " + index + " jobs");
          System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
          System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
          System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
          System.out.println("   average service time .... =   " + f.format(job.getService() / index));
          System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
          System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
          System.out.println("   utilization ............. =   " + f.format(job.getService() / departure));
     */   }


        
}  


