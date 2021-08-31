package PMCSN;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class ServerMaster implements Runnable {
	
	/*private final ThreadObject threadObject;

    public void workingTask(ThreadObject workingObject) {
        threadObject = workingObject;
    }*/


    public void run() {
    	
    	Job job = new Job();
    	int index = 0;
    	int counter = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0;
        
        try {
        	
        FileWriter myWriter = new FileWriter("/home/crazile/Scrivania/output.txt");

        while(index < Ssq2.LAST){
        	            
        	myWriter.write("Job totali: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size() + "\n");
        	//myWriter.write("ciao Master" + Thread.currentThread().getId());
        
            if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && Ssq2.lQueue.isEmpty()) {
      		  // continua
            	myWriter.write("Tutte vuote" + "\n");
            	
            	/* questo if e counter servono solo per poter vedere le code svuotarsi,
            	 * andranno tolti prima di consegnare
            	 */
            	if (counter == 50) {
            		System.exit(0);
            	}
            	counter++;
            	/**/
            	
            	continue;
            	
	      	} else if (Ssq2.lQueue.get(0).getArrival() >=totalService && Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && !Ssq2.lQueue.isEmpty()) {
	      		  // prendi il primo elemento di lQueue, mandalo al server e rimuovilo
	      		myWriter.write("Job a bassa priorità" + "\n");
	      		job = Ssq2.lQueue.get(0);
	      		Ssq2.lQueue.remove(0);
	      	} else if (Ssq2.mQueue.get(0).getArrival() >=totalService && Ssq2.hQueue.isEmpty() && !Ssq2.mQueue.isEmpty()) {
	      		  // prendi il primo elemento di mQueue, mandalo al server e rimuovilo
	      		myWriter.write("Job a media priorità" + "\n");
        		job = Ssq2.mQueue.get(0);
	      		Ssq2.mQueue.remove(0);
	      	} else if (Ssq2.hQueue.get(0).getArrival() >=totalService  && !Ssq2.hQueue.isEmpty()) {
	      		  // prendi il primo elemento di hQueue, mandalo al server e rimuovilo
	      		myWriter.write("Job ad alta priorità" + "\n");
        		job = Ssq2.hQueue.get(0);
        		//System.out.println("1: " + Ssq2.hQueue.get(0).getSqn());
    			Ssq2.hQueue.remove(0);
	      	}
            if (job == null) {
            	myWriter.write("Questo job per qualche motivo è null" + "\n");
            	continue;
            }
            myWriter.write(Double.toString(job.getArrival()) + "\n");
            myWriter.write(Double.toString(job.getDeparture()) + "\n");
            //System.exit(0);
            
            if (job.getArrival() < job.getDeparture()) {
            	  delay = job.getDeparture() - job.getArrival(); 	// delay in queue 
            } else {
            	  delay = 0.0;      							 // no delay   
            }
			service = Arrival.getService(Ssq2.r);
			wait = delay + service;
			departure = job.getArrival() + wait;            	  // time of departure 
			job.setDelay(job.getDelay() + delay);
			job.setWait(job.getWait() + wait);
			job.setService(job.getService() + service);
			totalService = totalService + service;
			
            index++; 
        }
        }catch (IOException e) {
        	e.printStackTrace();
        }
		job.setInterarrival(job.getArrival() - Ssq2.START);
		
		DecimalFormat f = new DecimalFormat("###0.00");
		
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(job.getService() / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(job.getService() / departure));
    }
      
}  


