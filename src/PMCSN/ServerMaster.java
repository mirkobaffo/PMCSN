package PMCSN;

import java.io.FileWriter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerMaster implements Runnable {

	public static long end;

    public void run() {
    	
    	Job job = new Job(0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
    	int index = 0;
    	int counter = 0;
    	double delay;                                 /* delay in queue       */
        double service;                               /* service time         */
        double wait;                                  /* delay + service      */
        double departure = Ssq2.START;
        double totalService = 0.0;
        double u = 5.45;
		
        try {

			//FileWriter writer = new FileWriter("/Users/mirko/Desktop/output");
			//FileWriter writer = new FileWriter("/home/crazile/Scrivania/output.txt");
        		
	        while(index < Ssq2.LAST){
	        
	        	TimeUnit.MICROSECONDS.sleep(1000);
	        	
	        	Job temp = null;
	         	//myWriter.write("ciao Master" + Thread.currentThread().getId());
	        	
	        	/*if (job.getState() == true) { 	    //  gestione dei job in arrivo dal feedback 
	        		Utils.checkState(job);
	        	} else {*/
	        		
	        		if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && Ssq2.lQueue.isEmpty()) {
			      		  // continua
			            	//myWriter.write("Tutte vuote" + "\n");
			            	
			            	/* questo if e counter servono solo per poter vedere le code svuotarsi,
			            	 * andranno tolti prima di consegnare
			            	 
			            	if (counter == 50) {
			            		System.exit(0);
			            	}
			            	counter++;
			            	/**/
			            	index++;
			            	continue;
			            	
				      	} else if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && !Ssq2.lQueue.isEmpty() ) {
				        	//writer.write("Job totali: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size() + "\n");
				      		  // prendi il primo elemento di lQueue, mandalo al server e rimuovilo
				      		//writer.write("Job a bassa priorità" + "\n");
				      		temp = Ssq2.lQueue.get(0);
				      		Ssq2.lQueue.remove(0);
				      	} else if (Ssq2.hQueue.isEmpty() && !Ssq2.mQueue.isEmpty()) {
				        	//writer.write("Job totali: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size() + "\n");
				      		  // prendi il primo elemento di mQueue, mandalo al server e rimuovilo
				      		//writer.write("Job a media priorità" + "\n");
			        		temp = Ssq2.mQueue.get(0);
				      		Ssq2.mQueue.remove(0);
				      	} else if (!Ssq2.hQueue.isEmpty()) {
				        	//writer.write("Job totali: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size() + "\n");
				      		  // prendi il primo elemento di hQueue, mandalo al server e rimuovilo
				      		//writer.write("Job ad alta priorità" + "\n");
			        		temp = Ssq2.hQueue.get(0);
			    			Ssq2.hQueue.remove(0);
				      	}
	        	//}
	            
	        
	            
	            if (temp == null) {
	            	//writer.write("Questo job per qualche motivo è null" + "\n");
	            	index++;
	            	continue;
	            }
	            //writer.write(Double.toString(job.getArrival()) + "\n");
	            //writer.write(Double.toString(job.getDeparture()) + "\n");
	            //System.exit(0);
	            
	            if (temp.getArrival() < temp.getDeparture()) {
	            	  delay = temp.getDeparture() - temp.getArrival(); 	// delay in queue 
	            } else {
	            	  delay = 0.0;      							 // no delay   
	            }
				service = Arrival.getService(Ssq2.r, u);
				wait = delay + service;
				departure = temp.getArrival() + wait;    // time of departure
				job = new Job(temp.getArrival(), temp.getDelay() + delay, temp.getDeparture() + departure, temp.getPriority(), temp.getTopic(), temp.getSqn(), temp.getWait() + wait, service, temp.getResponse(), temp.getState());
				/*temp.setDelay(temp.getDelay() + delay);
				temp.setWait(temp.getWait() + wait);
				temp.setService(temp.getService() + service);*/
				totalService += service;
				
				/*if (!fQueue.isEmpty()) {
					System.out.println("primo elemento di frontend all'iterazione " + index + ": " + fQueue.get(0).getTopic() + "\n");
				}*/
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
		//job.setInterarrival(job.getArrival() - Ssq2.START);
		DecimalFormat f = new DecimalFormat("###0.00");
		System.out.println("Valore della wait dell'ultimo job: " + job.getWait());
		System.out.println("Valore del service dell'ultimo job: " + job.getService());
		System.out.println("Valore del delay dell'ultimo job: " + job.getDelay());
		System.out.println("Valore di index: " + index);

		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(job.getService() / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(job.getService() / departure));
		
		end = System.currentTimeMillis();
		System.out.println("Duration: " + (end-Ssq2.start) + "milliseconds");
    }
      
}  


