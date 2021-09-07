package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*public class ServerBlog implements Runnable {

	public static ArrayList<Job> rJobs = new ArrayList<>();

	
	public void run() {
    	Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false, Ssq2.START);
    	int index = 0;
    	double delay = Ssq2.START;                                 // delay in queue       
        double arrival = Ssq2.START;
    	double response = Ssq2.START;
    	double service = Ssq2.START;                               // service time         
        double wait = Ssq2.START;                                  // delay + service      
        double departure = Ssq2.START;
        double totalService = Ssq2.START;
		//service time
		double u = 0.4;

		int counter = 0;

        try {
        	
        	while(index < Ssq2.LAST){
        		Job temp;
                TimeUnit.MICROSECONDS.sleep(1000);
                if (rJobs.size() > 0) {
					temp = rJobs.get(0);
					if (temp == null) {
						index++;
						continue;
					} else {
						counter++;
						System.out.println(rJobs.get(0) + " " + rJobs.get(0).getArrival());
						rJobs.remove(0);
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
				job = new Job(temp.getInterarrival(), arrival, delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, true, Ssq2.START);
				totalService += service;
				wait = delay + service;		// attesa in coda del job successivo

    			index++;
    			
    			// questo server non fa feedback 

				DecimalFormat f = new DecimalFormat("###0.00");

				//System.out.println("   type of the job .. =   " + (job.getTopic()));
				//System.out.println("   interarrival time =   " + f.format(job.getInterarrival()));
				//System.out.println("   wait ............ =   " + f.format(job.getWait()));
				//System.out.println("   delay ........... =   " + f.format(job.getDelay()));
				//System.out.println("   service time .... =   " + f.format(job.getService()));
            }
        	
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }

        DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("Server Blog\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getInterarrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		
		System.out.println("JOB R VALIDI: " + counter);

	}
}*/

public class ServerBlog {

    static double START   = 0.0;            // initial (open the door)
    static double STOP    = 1000000.0;        // terminal (close the door) time
    static int    SERVERS = 2;              // number of servers

    static double sarrival = START;
    static double u = 60; //tempo medio di servizio

    public static ArrayList<Job> rJobs = new ArrayList<>();

    static class MsqT {
        double current;                   // current time
        double next;                      // next (most imminent) event time
    }

    static class MsqSum {                      // accumulated sums of
        double service;                   //   service times
        long   served;                    //   number served
    }

    static class MsqEvent {                     // the next-event list
        double t;                         //   next event time
        int    x;                         //   event status, 0 or 1
        double wait;
    }									//   può essere il nostro boolean


    public static void blog() {

        long   number = 0;             // number in the node
        int    e;                      // next event index
        int    s;                      // server index
        int  index  = 0;             // used to count processed jobs
        double area   = 0.0;           // time integrated number in the node
        double service;
        //double wait;
        double totalService = 0;
        Job job;

        Rngs r = new Rngs();
        r.plantSeeds(0);

        MsqEvent[] event = new MsqEvent[SERVERS + 1];
        MsqSum[] sum = new MsqSum[SERVERS + 1];
        for (s = 0; s < SERVERS + 1; s++) {
            event[s] = new MsqEvent();
            sum [s]  = new MsqSum();
        }

        MsqT t = new MsqT();
        t.current    = START;
        if (!rJobs.isEmpty()) {
            event[0].t   = rJobs.get(0).getTime();
            event[0].wait = ((t.next - t.current) * number)+ rJobs.get(0).getWait();
            rJobs.remove(0);
            event[0].x   = 1;
            for (s = 1; s <= SERVERS; s++) {
                event[s].t     = START;          /* this value is arbitrary because */
                event[s].x     = 0;              /* all servers are initially idle  */
                sum[s].service = 0.0;
            }

        }

        while ((event[0].x != 0) || (number != 0)) {
            e = nextEvent(event);                /* next event index */
            t.next = event[e].t;                        /* next event time  */
            area += (t.next - t.current) * number; /* update integral  */
            t.current = t.next;                            /* advance the clock*/
            if (e == 0) {                                  /* process an arrival*/
                number++;
                if (!rJobs.isEmpty()) {
                    event[0].t = rJobs.get(0).getTime();
                    event[0].wait = ((t.next - t.current) * number)+ rJobs.get(0).getWait();
                    /*job = new Job(wJobs.get(0).getInterarrival(), event[0].t, wJobs.get(0).getDelay(), wJobs.get(0).getDeparture(), wJobs.get(0).getPriority(), wJobs.get(0).getLabel(), wJobs.get(0).getSqn(), wait, sum[0].service, wJobs.get(0).getResponse(), true, wJobs.get(0).getTime());
                    Utils.topicSplitter(job);*/
                    rJobs.remove(0);
                } else {
                    continue;
                }
                if (rJobs.isEmpty())
                    //if (event[0].t > STOP)
                    event[0].x = 0;
                if (number <= SERVERS) {
                    service = Arrival.getMultiService(r, u);
                    totalService += service;
                    s = findOne(event);
                    sum[s].service += service;
                    sum[s].served++;
                    event[s].t = t.current + service;
                    event[s].x = 1;
                }
            }
            else {                                         /* process a departure */
                index++;                              /* from server s       */
                number--;
                s = e;

                if (number >= SERVERS) {
                    service = Arrival.getMultiService(r, u);
                    sum[s].service += service;
                    sum[s].served++;
                    event[s].t = t.current + service;
                    job = new Job(0.0, event[s].t, 0.0, 0.0, 0, 'A', index, event[s].wait, sum[s].service, 0.0, true, 0.0);
                    Utils.prioSplitter(job);
                }
                else
                    event[s].x = 0;
            }
        }

        DecimalFormat f = new DecimalFormat("####.##");
        DecimalFormat g = new DecimalFormat("####.###");

        System.out.println("\nfor " + index + " jobs the service node statistics are:\n");
        System.out.println("  avg interarrivals .. =   " + f.format(event[0].t / (index)));
        System.out.println("  avg wait ........... =   " + f.format(area / index));
        System.out.println("  avg # in node ...... =   " + f.format(area / t.current));
        for (s = 1; s <= SERVERS; s++)          /* adjust area to calculate */
            area -= sum[s].service;              /* averages for the queue   */

        System.out.println("  avg delay .......... =   " + f.format(area / index));
        System.out.println("  avg # in queue ..... =   " + f.format(area / (t.current)));
        System.out.println("\nthe server statistics are:\n");
        System.out.println("    server     utilization     avg service      share");
        for (s = 1; s <= SERVERS; s++) {
            System.out.print("       " + s + "          " + g.format(sum[s].service / t.current) + "            ");
            System.out.println(f.format(sum[s].service / sum[s].served) + "         " + g.format(sum[s].served / (double)index));
        }

        System.out.println("");
    }


	static int nextEvent(MsqEvent[] event) {
        /* ---------------------------------------
         * return the index of the next event type
         * ---------------------------------------
         */
        int e;
        int i = 0;

        while (event[i].x == 0)       /* find the index of the first 'active' */
            i++;                        /* element in the event list            */
        e = i;
        while (i < SERVERS) {         /* now, check the others to find which  */
            i++;                        /* event type is most imminent          */
            if ((event[i].x == 1) && (event[i].t < event[e].t))
                e = i;
        }
        return (e);
    }

    static int findOne(MsqEvent[] event) {
        /* -----------------------------------------------------
         * return the index of the available server idle longest
         * -----------------------------------------------------
         */
        int s;
        int i = 1;

        while (event[i].x == 1)       /* find the index of the first available */
            i++;                        /* (idle) server                         */
        s = i;
        while (i < SERVERS) {         /* now, check the others to find which   */
            i++;                        /* has been idle longest                 */
            if ((event[i].x == 0) && (event[i].t < event[s].t))
                s = i;
        }
        return (s);
    }
}
