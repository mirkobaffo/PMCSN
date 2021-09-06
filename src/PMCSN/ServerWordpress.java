package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/*public class ServerWordpress implements Runnable {
	
	public static ArrayList<Job> wJobs = new ArrayList<>();

	public void run() {
    	Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false);
    	int index = 0;
    	double delay = Ssq2.START; 									// delay in queue
        double arrival = Ssq2.START;
    	double response = Ssq2.START;
    	double service = Ssq2.START;                               // service time
        double wait = Ssq2.START;                                  // delay + service
        double departure = Ssq2.START;
        double totalService = Ssq2.START;
		//service time
		double u = 1.0;

		int counter = 0;

        try {

			while (index < Ssq2.LAST) {
				Job temp;
				TimeUnit.MICROSECONDS.sleep(1000);
				if (wJobs.size() > 0) {
					temp = wJobs.get(0);
					if (temp == null) {
						index++;
						continue;
					} else {
						counter++;
						System.out.println(wJobs.get(0) + " " + wJobs.get(0).getArrival());
						wJobs.remove(0);
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
		System.out.println("Server Wordpress\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		
		System.out.println("JOB W VALIDI: " + counter);

	}


}*/

public class ServerWordpress implements Runnable {

	static double START   = 0.0;            // initial (open the door)
	static double STOP    = 20000.0;        // terminal (close the door) time
	static int    SERVERS = 2;              // number of servers

	static double sarrival = START;
	static double u = 1.0;

	public static ArrayList<Job> wJobs = new ArrayList<>();

	class MsqT { // mantiene lo stato del server
		double current;                   // current time
		double next;                      // next (most imminent) event time
	}

	class MsqSum {                      // accumulated sums of
		double service;                   //   service times
		long   served;                    //   number served
	}

	class MsqEvent{                     // the next-event list
		double t;                         //   next event time
		int    x;                         //   event status, 0 or 1
	}


	public void run(){
		long   number = 0;             // number in the node
		int    e;                      // next event index
		int    s;                      // server index
		long   index  = 0;             // used to count processed jobs
		double area   = 0.0;           // time integrated number in the node
		double service;
		int counter = 0;
		
    	Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false);

		
		Rngs r = new Rngs();
		r.plantSeeds(0);


		MsqEvent [] event = new MsqEvent [Msq.SERVERS + 1];  // coda di eventi (job)
		MsqSum [] sum = new MsqSum [Msq.SERVERS + 1];
		for (s = 0; s < Msq.SERVERS + 1; s++) {
			event[s] = new MsqEvent();
			sum [s]  = new MsqSum();
		}

		PMCSN.MsqT t = new PMCSN.MsqT(); // mantiene lo stato attuale del nodo?

		t.current    = Msq.START;  // time corrente del nodo
		event[0].t   = getArrival(r);  // assegno tempo di arrivo al primo job
		event[0].x   = 1;  // setto lo stato del job a 1
		for (s = 1; s <= Msq.SERVERS; s++) {
			event[s].t     = Msq.START;          // per tutti gli altri job inizializzo il tempo a 0
			event[s].x     = 0;              	// e inzializzo lo stato a 0
			sum[s].service = 0.0;				// inizializzo tutto a 0
			sum[s].served  = 0;
		}

		while ((event[0].x != 0) || (number != 0)) {  // finché lo stato del job è diverso da 0 e il numero di job in esecuzione != 0
			
			Job temp;
			TimeUnit.MICROSECONDS.sleep(1000);
			if (wJobs.size() > 0) {
				temp = wJobs.get(0);
				if (temp == null) {
					index++;
					continue;
				} else {
					counter++;
					System.out.println(wJobs.get(0) + " " + wJobs.get(0).getArrival());
					wJobs.remove(temp);
				}
			} else {
				index++;
				continue;
			}

			e         = nextEvent(event);                // next event index dice qual è il prossimo job attivo che può essere mandato in esecuzione
			t.next    = event[e].t;                        // next event time assegna al tempo del server il tempo di quel job
			area     += (t.next - t.current) * number;     // update integral
			t.current = t.next;                            // advance the clock

			if (e == 0) {                                  // process an arrival
				number++;
				event[0].t        = getArrival(r);
				if (event[0].t > Msq.STOP)
					event[0].x      = 0;
				if (number <= Msq.SERVERS) {
					service         = Arrival.getMultiService(r, u);
					s               = findOne(event);
					sum[s].service += service;
					sum[s].served++;
					event[s].t      = t.current + service;
					event[s].x      = 1;
				}
			}
			else {                                         // process a departure
				index++;                                     // from server s
				number--;
				s                 = e;
				if (number >= Msq.SERVERS) {
					service         = Arrival.getMultiService(r, u);
					sum[s].service += service;
					sum[s].served++;
					event[s].t      = t.current + service;
				}
				else
					event[s].x      = 0;
			}
		}

		DecimalFormat f = new DecimalFormat("###0.00");
		DecimalFormat g = new DecimalFormat("###0.000");

		System.out.println("\nfor " + index + " jobs the service node statistics are:\n");
		System.out.println("  avg interarrivals .. =   " + f.format(event[0].t / index));
		System.out.println("  avg wait ........... =   " + f.format(area / index));
		System.out.println("  avg # in node ...... =   " + f.format(area / t.current));

		for (s = 1; s <= Msq.SERVERS; s++)          // adjust area to calculate
			area -= sum[s].service;              // averages for the queue

		System.out.println("  avg delay .......... =   " + f.format(area / index));
		System.out.println("  avg # in queue ..... =   " + f.format(area / t.current));
		System.out.println("\nthe server statistics are:\n");
		System.out.println("    server     utilization     avg service      share");
		for (s = 1; s <= Msq.SERVERS; s++) {
			System.out.print("       " + s + "          " + g.format(sum[s].service / t.current) + "            ");
			System.out.println(f.format(sum[s].service / sum[s].served) + "         " + g.format(sum[s].served / (double)index));
		}

		System.out.println("");
	}

	int nextEvent(MsqEvent[] event) {

		 // return the index of the next event type

		int e;
		int i = 0;

		while (event[i].x == 0)       // find the index of the first 'active'
			i++;                        // element in the event list
		e = i;
		while (i < Msq.SERVERS) {         // now, check the others to find which
			i++;                        // event type is most imminent
			if ((event[i].x == 1) && (event[i].t < event[e].t))
				e = i;
		}
		return (e);
	}

	int findOne(MsqEvent[] event) {
		//return the index of the available server idle longest

		int s;
		int i = 1;

		while (event[i].x == 1)       // find the index of the first available
			i++;                        // (idle) server
		s = i;
		while (i < Msq.SERVERS) {         // now, check the others to find which
			i++;                        // has been idle longest
			if ((event[i].x == 0) && (event[i].t < event[s].t))
				s = i;
		}
		return (s);
	}



}




