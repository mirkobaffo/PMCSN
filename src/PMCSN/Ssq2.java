package PMCSN;

/* -------------------------------------------------------------------------

 * This program - an extension of program ssq1.c - simulates a single-server
 * FIFO service node using Exponentially distributed interarrival times and
 * Uniformly distributed service times (i.e. a M/U/1 queue).
 *
 * Name              : Ssq2.java  (Single Server Queue, version 2)
 * Authors           : Steve Park & Dave Geyer
 * Translated by     : Jun Wang & Richard Dutton
 * Language          : Java
 * Latest Revision   : 6-16-06
 * ------------------------------------------------------------------------- 
 */

import java.lang.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class Ssq2  { //dovremo cambiargli nome perché questo è il thread degli arrivi

  static long LAST = 10000;                    /* number of jobs processed */
  static double START = 0.0;                   /* initial time             */
  
  static double sarrival = START;  			   /* Why did I do this?       */
    static double lambda = 6.0;
  static int hPrio = 1;
  static int mPrio = 2;
  static int lPrio = 3;
  
  static int minPrioValue = 1;
  static int maxPrioValue = 4;
  
  static int minLabel = 1;
  static int maxLabel = 5;
  static char frontend = 'F';
  static char backend = 'B';
  static char wordpress = 'W';
  static char blog = 'R';


  public static ArrayList<ArrayList<Job>> queue = new ArrayList<>();
  public static ArrayList<Job> hQueue = new ArrayList<>();
  public static ArrayList<Job> mQueue = new ArrayList<>();
  public static ArrayList<Job> lQueue = new ArrayList<>();
  
  public static Rng r = new Rng();


    public static long start = System.currentTimeMillis();

  public static void ssqArrive() {
    
    int   index     = 0;                    /* job index            */
    double interarrival = START;
    double arrival   = START;             /* time of arrival      */
    double departure = START;  					/* time of departure    */
    int priority;
    char label;


    r.putSeed(123456789);
    
	queue.add(hQueue);
	queue.add(mQueue);
	queue.add(lQueue);
    
    while (index < LAST) {
    	try {
	  	    TimeUnit.MICROSECONDS.sleep(1000);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	
      priority = Generator.getRandomInRange(minPrioValue, maxPrioValue);
      label = Generator.getRandomTopic(minLabel, maxLabel);
	  interarrival = Arrival.getArrival(sarrival, r, lambda);
      arrival += interarrival;
	  Job job = new Job(interarrival, arrival, Ssq2.START, departure, priority, label, index, Ssq2.START, Ssq2.START, Ssq2.START, false, Ssq2.START);
	  Utils.prioSplitter(job);
	  sarrival = arrival;

	  index++;
    }

  }

	  /*ServerMaster s = new ServerMaster();
	  Thread t = new Thread(s);
	  t.start();*/
	  /* Quello che segue dovrebbe essere eseguito da un altro thread */

	  /*if (hQueue.isEmpty() && mQueue.isEmpty() && lQueue.isEmpty()) {
		  continue;
	  } else if (hQueue.isEmpty() && mQueue.isEmpty() && !lQueue.isEmpty()) {
		  // prendi il primo elemento di lQueue, mandalo al server e rimuovilo
	  } else if (hQueue.isEmpty() && !mQueue.isEmpty()) {
		  // prendi il primo elemento di mQueue, mandalo al server e rimuovilo
	  } else if (!hQueue.isEmpty()) {
		  // prendi il primo elemento di hQueue, mandalo al server e rimuovilo
	  }

	  if (arrival < departure)
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
  }*/
 

   static double exponential(double m, Rng r) {
/* ---------------------------------------------------
 * generate an Exponential random variate, use m > 0.0
 * ---------------------------------------------------
 */
    return (-m * Math.log(1.0 - r.random()));
  }

   double uniform(double a, double b, Rng r) {
/* ------------------------------------------------
 * generate an Uniform random variate, use a < b
 * ------------------------------------------------
 */
    return (a + (b - a) * r.random());
  }


   static double getArrival(Rng r) {
/* ------------------------------
 * generate the next arrival time
 * ------------------------------
 */
//    static double sarrival = START;

    sarrival += exponential(2.0, r);
    return (sarrival);
  }


   double getService(Rng r) {
/* ------------------------------
 * generate the next service time
 * ------------------------------
 */
    return (uniform(1.0, 2.0, r));
  }
    
}
