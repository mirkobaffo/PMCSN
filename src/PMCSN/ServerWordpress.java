package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerWordpress implements Runnable {
	
	public static ArrayList<Job> wJobs = new ArrayList<>();

	public void run() {
		Job job = new Job(0.0, 0.0, 0.0, 0, 'A', 0, 0.0, 0.0, 0.0, false);
    	int index = 0;
    	double delay;                                 // delay in queue
        double service;                               // service time
        double wait;                                  // delay + service
        double departure = Ssq2.START;
        double totalService = 0;
		//service time
		double u = 1.0;

		int counter = 0;

        try {

			while (index < Ssq2.LAST) {
				TimeUnit.MICROSECONDS.sleep(1000);
				if (wJobs.size() > 0) {
					job = wJobs.get(0);

					if (job == null) {
						//System.out.println("Questo job è null e quindi forse per questo esplode tutto");
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


			if (job.getArrival() < job.getDeparture()) {
				delay = job.getDeparture() - job.getArrival();    // delay in queue
			} else {
				delay = 0.0;                                     // no delay
			}
			service = Arrival.getService(Ssq2.r, u);
			wait = delay + service;
			departure = job.getArrival() + wait;                  // time of departure

    			totalService = totalService + service;

    			//job.setState(true);
    			Utils.prioSplitter(job);

			index++;
		}
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }

		System.out.println("JOB W VALIDI: " + counter);

	}


}

/*public class ServerWordpress implements Runnable {

	static double START   = 0.0;            // initial (open the door)
	static double STOP    = 20000.0;        // terminal (close the door) time
	static int    SERVERS = 4;              // number of servers

	static double sarrival = START;

	class MsqT {
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

		//da rimediare rngs
		Rngs r = new Rngs();
		r.plantSeeds(0);


		MsqEvent [] event = new MsqEvent [Msq.SERVERS + 1];
		MsqSum [] sum = new MsqSum [Msq.SERVERS + 1];
		for (s = 0; s < Msq.SERVERS + 1; s++) {
			event[s] = new MsqEvent();
			sum [s]  = new MsqSum();
		}

		PMCSN.MsqT t = new PMCSN.MsqT();

		t.current    = Msq.START;
		event[0].t   = getArrival(r);
		event[0].x   = 1;
		for (s = 1; s <= Msq.SERVERS; s++) {
			event[s].t     = Msq.START;          // this value is arbitrary because
			event[s].x     = 0;              // all servers are initially idle
			sum[s].service = 0.0;
			sum[s].served  = 0;
		}

		while ((event[0].x != 0) || (number != 0)) {
			e         = nextEvent(event);                // next event index
			t.next    = event[e].t;                        // next event time
			area     += (t.next - t.current) * number;     // update integral
			t.current = t.next;                            // advance the clock

			if (e == 0) {                                  // process an arrival
				number++;
				event[0].t        = getArrival(r);
				if (event[0].t > Msq.STOP)
					event[0].x      = 0;
				if (number <= Msq.SERVERS) {
					service         = getService(r);
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
					service         = getService(r);
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

	double exponential(double m, Rngs r) {

		 // generate an Exponential random variate, use m > 0.0

		return (-m * Math.log(1.0 - r.random()));
	}

	double uniform(double a, double b, Rngs r) {

		 // generate a Uniform random variate, use a < b

		return (a + (b - a) * r.random());
	}

	double getArrival(Rngs r) {

		 // generate the next arrival time, with rate 1/2

		r.selectStream(0);
		Ssq2.sarrival += exponential(2.0, r);
		return (Ssq2.sarrival);
	}


	double getService(Rngs r) {

		// generate the next service time, with rate 1/6

		r.selectStream(1);
		return (uniform(2.0, 10.0, r));
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



}*/




