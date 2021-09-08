package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ServerFrontendImproved {

        static double START   = 0.0;            // initial (open the door)
        static double STOP    = 1000000.0;        // terminal (close the door) time
        static int    SERVERS = 2;              // number of servers

        static double sarrival = START;
        static double u = 60; //tempo medio di servizio
        static double u2 = 45; //tempo medio del server aggiunto



        public static ArrayList<Job> fJobs = new ArrayList<>();


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


        public static Main.Container frontEnd() {
            long   number = 0;             // number in the node
            int    e;                      // next event index
            int    s;                      // server index
            int  index  = 0;             // used to count processed jobs
            double area   = 0.0;           // time integrated number in the node
            double service;
            double totalService = 0;

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
            if (!fJobs.isEmpty()) {
                event[0].t   = fJobs.get(0).getTime();
                event[0].wait = ((t.next - t.current) * number)+ fJobs.get(0).getWait();
                fJobs.remove(0);
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
                    if (!fJobs.isEmpty()) {
                        event[0].t = fJobs.get(0).getTime();
                        event[0].wait = ((t.next - t.current) * number)+ fJobs.get(0).getWait();
                        fJobs.remove(0);
                    } else {
                        continue;
                    }
                    if (fJobs.isEmpty())
                        event[0].x = 0;
                    if (number <= SERVERS) {
                        service = Generator.getMultiService(r, u);
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
                        if (s == 1) {
                            service = Generator.getMultiService(r, u);
                        }
                        else
                        {
                            service = Generator.getMultiService(r, u2);
                        }
                        sum[s].service += service;
                        sum[s].served++;
                        event[s].t = t.current + service;
                    }
                    else
                        event[s].x = 0;
                }
            }
            Main.Container c = new Main.Container();
            c.averageWait = area/index;
            c.serverNumber = SERVERS;
            /*
            DecimalFormat f = new DecimalFormat("####.##");
            DecimalFormat g = new DecimalFormat("####.###");
            System.out.println("                               ");
            System.out.println("          SERVER FRONTEND MIGLIORATO          ");
            System.out.println("\nfor " + index + " jobs the service node statistics are:\n");
            System.out.println("  avg interarrivals .. =   " + f.format(event[0].t / (index)));
            System.out.println("  avg wait ........... =   " + f.format(area / index));
            System.out.println("  avg # in node ...... =   " + f.format(area / t.current));
             */
            for (s = 1; s <= SERVERS; s++)          /* adjust area to calculate */
                area -= sum[s].service;              /* averages for the queue   */
            c.averageInQueue = area/t.current;
            c.aversageDelay =  area/index;
            /*
            System.out.println("  avg delay .......... =   " + f.format(area / index));
            System.out.println("  avg # in queue ..... =   " + f.format(area / (t.current)));
            System.out.println("\nthe server statistics are:\n");
            System.out.println("    server     utilization     avg service      share");
             */
            for (s = 1; s <= SERVERS; s++) {
               // System.out.print("       " + s + "          " + g.format(sum[s].service / t.current) + "            ");
               // System.out.println(f.format(sum[s].service / sum[s].served) + "         " + g.format(sum[s].served / (double)index));
                c.utilizations.add(sum[s].service/sum[s].served);
                c.services.add(sum[s].service/index);
                c.responseTime.add(sum[s].service/index + c.averageWait);
            }

            return c;

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





