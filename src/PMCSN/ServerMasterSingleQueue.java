package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerMasterSingleQueue {

        public static long end;
        public static boolean isReady = false;

        public static class Container {
            double averageWait;
            double averageResponseTime;
            double serverNumber;
            double aversageDelay;
            double averageInQueue;
            List<Double> utilizations = new ArrayList<>();
            List<Double> services = new ArrayList<>();
        }

        public static Main.Container master() {

            Job job = new Job(Arrival.START, Arrival.START, Arrival.START, Arrival.START, 0, 'A', 0, Arrival.START, Arrival.START, Arrival.START, false, Arrival.START);
            int index = 0;
            int counter = 0;
            double delay;                                 /* delay in queue */
            double totalDelay = 0;
            double service = 0;                               /* service time */
            double arrival = 0;
            double wait = Arrival.START;                                /* delay + service */
            double response = Arrival.START;
            double totalWait = 0;
            double departure = Arrival.START;
            double totalService = Arrival.START;
            double time = Arrival.START; // è l'arrivo nel server successivo
            double u = 11; // 60/5,45 --> tempo medio di servizio

            DecimalFormat f = new DecimalFormat("#.######");

            try {
                while (index < Arrival.LAST) {
                    isReady = true;
                    TimeUnit.MICROSECONDS.sleep(1000); // questa sleep può essere tolta ????
                    Job temp = null;
                    Arrival.queue.size();
                    if (Arrival.queue.size() > 0) {
                        temp = Arrival.queue.get(0);
                        if (temp == null) {
                            index++;
                            continue;
                        } else {
                            counter++;
                            Arrival.queue.remove(temp);
                        }
                    } else {
                        index++;
                        continue;
                    }
                    if (temp.getArrival() < departure) {
                        delay = departure - temp.getArrival();    // delay in queue
                        totalDelay += delay;
                    } else {
                        delay = Arrival.START;                                 // no delay
                    }
                    wait = delay + service;        // attesa in coda del job successivo, no di questo job
                    arrival = temp.getArrival();
                    if(arrival > departure + service){
                        wait = 0;
                    }
                    service = Generator.getService(Arrival.r, u);  // del job corrente
                    response = wait + service;
                    departure =  temp.getArrival() + wait;    // time of departure del job corrente
                    time = temp.getArrival() + wait + service;
                    job = new Job(temp.getInterarrival(), temp.getArrival(), delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, temp.getState(), time);
                    totalService += service;
                    totalWait += wait;
                    Utils.topicSplitter(job);
                    index++;
                    counter++;
                }
               // System.out.println("Valore di counter = " + counter);

            } catch(InterruptedException e){
                e.printStackTrace();
            }
            /*
            //DecimalFormat f = new DecimalFormat("#.######");
            System.out.println("Server Master\n");
            System.out.println("\nfor " + index + " jobs");
            System.out.println("   average interarrival time =   " + f.format(job.getArrival() / index));
            System.out.println("   average wait ............ =   " + f.format(totalWait / index));
            System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
            System.out.println("   average service time .... =   " + f.format(totalService / index));
            System.out.println("   average # in the node ... =   " + f.format(totalWait / departure));
            System.out.println("   average # in the queue .. =   " + f.format(totalDelay / departure));
            System.out.println("   utilization ............. =   " + f.format(totalService / departure));
             */
            Main.Container c = new Main.Container();
            c.averageInQueue = totalDelay/departure;
            c.serverNumber = 1;
            c.services.add(totalService/index);
            c.utilizations.add(0,totalService/departure);
            c.averageWait = totalWait/index;
            c.responseTime.add(c.averageWait + c.services.get(0));
            c.aversageDelay =  totalDelay/index;
            end = System.currentTimeMillis();
            //System.out.println("Duration: " + (end - Arrival.start) + "milliseconds");
            return c;
        }
    }

