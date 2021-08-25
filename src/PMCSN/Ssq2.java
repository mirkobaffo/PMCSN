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

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.ArrayList;

class Ssq2 implements Runnable{

  static long LAST = 10000;                    /* number of jobs processed */
  static double START = 0.0;                   /* initial time             */
  
  static double sarrival = START;              /* Why did I do this?       */

  static int hPrio = 1;
  static int mPrio = 2;
  static int lPrio = 3;
  
  static int minTopic = 1;
  static int maxTopic = 2;
  static char frontend = 'F';
  static char backend = 'B';
  static char wordpress = 'W';
  static char blog = 'R';

    //All'interno di questo metodo dobbiamo mettere quello che va fatto eseguire dall'altro thread
    //ovviamente le variabili vanno rese public e globali per la classe altrimenti i due thread non le vedono
    //Secondo me dovremmo mettere quella porzione di codice proprio all'interno della classe server
    public void run() {
        while(true){
            System.out.println("ciao");
        }
    }
	
  public static void main(String[] args) {
    
    long   index     = 0;                         /* job index            */
    double arrival   = START;                     /* time of arrival      */
    double delay;                                 /* delay in queue       */
    double service;                               /* service time         */
    double wait;                                  /* delay + service      */
    double departure = START;  					/* time of departure    */
    int priority;
    char topic;
    
    Job job = new Job();
    job.initParams();
      
    Rng r = new Rng();
    r.putSeed(123456789);
    
    ArrayList<ArrayList<Job>> queue = new ArrayList<>();
	ArrayList<Job> hQueue = new ArrayList<>();
	ArrayList<Job> mQueue = new ArrayList<>();
	ArrayList<Job> lQueue = new ArrayList<>();
	queue.add(hQueue);
	queue.add(mQueue);
	queue.add(lQueue);

    while (index < LAST) {
      priority = Generator.getRandomInRange(hPrio, lPrio);
      topic = Generator.getRandomTopic(minTopic, maxTopic);
	  arrival = Arrival.getArrival(sarrival, r);
	  job.setArrival(arrival);
	  job.setPriority(priority);
	  job.setTopic(topic);

	  if (job.getPriority() == hPrio) {
			hQueue.add(job); //queue.get(hPrio).add(job);
	  } else if (job.getPriority() == mPrio) {
			mQueue.add(job); //queue.get(mPrio).add(job);
	  } else {
			lQueue.add(job); //queue.get(lPrio).add(job);
	  }

	  Server s = new Server();
	  Thread t = new Thread(s);
	  t.start();
	  /* Quello che segue dovrebbe essere eseguito da un altro thread */

	  if (hQueue.isEmpty() && mQueue.isEmpty() && lQueue.isEmpty()) {
		  continue;
	  } else if (hQueue.isEmpty() && mQueue.isEmpty() && !lQueue.isEmpty()) {
		  // prendi il primo elemento di lQueue, mandalo al server e rimuovilo
	  } else if (hQueue.isEmpty() && !mQueue.isEmpty()) {
		  // prendi il primo elemento di mQueue, mandalo al server e rimuovilo
	  } else if (!hQueue.isEmpty()) {
		  // prendi il primo elemento di hQueue, mandalo al server e rimuovilo
	  }

	  if (arrival < departure)
	    delay = departure - arrival;         /* delay in queue    */
	  else
	    delay = 0.0;                         /* no delay          */
	  service = Arrival.getService(r);
	  wait = delay + service;
	  departure = arrival + wait;              /* time of departure */
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
  }


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
