package PMCSN;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ServerMaster {

	public static long end;
	public static double serviceAtMaxLoad;
	public static double workingDay = 540.00 * 60; // minuti in giornata lavorativa


	public static void master() {

		Job job = new Job(Arrival.START, Arrival.START, Arrival.START, Arrival.START, 0, 'A', 0, Arrival.START, Arrival.START, Arrival.START, false, Arrival.START);
		int index = 0;
		int counter = 0;
		double delay;                                 /* delay in queue */
		double service;                               /* service time */
		double arrival = 0;
		double wait = Arrival.START;                                /* delay + service */
		double response = Arrival.START;
		double departure = Arrival.START;
		double totalService = Arrival.START;
		double time = Arrival.START; // è l'arrivo nel server successivo
		double u = 11; // 60/5,45 --> tempo medio di servizio

		try {
			while (index < Arrival.LAST) {
				TimeUnit.MICROSECONDS.sleep(1000); // questa sleep può essere tolta ????
				Job temp = null;	
				if (Arrival.hQueue.isEmpty() && Arrival.mQueue.isEmpty() && Arrival.lQueue.isEmpty()) {
					index++;
					continue;
				} else if (Arrival.hQueue.isEmpty() && Arrival.mQueue.isEmpty() && !Arrival.lQueue.isEmpty()) {
					temp = Arrival.lQueue.get(0);
					Arrival.lQueue.remove(temp);
				} else if (Arrival.hQueue.isEmpty() && !Arrival.mQueue.isEmpty()) {
					temp = Arrival.mQueue.get(0);
					Arrival.mQueue.remove(temp);
				} else if (!Arrival.hQueue.isEmpty()) {
					temp = Arrival.hQueue.get(0);
					Arrival.hQueue.remove(temp);
				}
				if (temp == null) {
					index++;
					continue;
				}
				if (temp.getArrival() < departure) {
					delay = departure - temp.getArrival();    // delay in queue
				} else {
					delay = Arrival.START;                                 // no delay
				}
				arrival = job.getArrival();
				service = Generator.getService(Arrival.r, u);  // del job corrente
				response = wait + service;
				departure = temp.getArrival() + wait;    // time of departure del job corrente
				time = temp.getArrival() + wait + service;
				job = new Job(temp.getInterarrival(), temp.getArrival(), delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, temp.getState(), time);
				totalService += service;
				serviceAtMaxLoad = totalService;
				wait = delay + service;        // attesa in coda del job successivo
				Utils.topicSplitter(job);
				index++;
			}
		} catch(InterruptedException e){
			e.printStackTrace();
		}

		DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("Server Master\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getArrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));

		end = System.currentTimeMillis();
		System.out.println("Duration: " + (end - Arrival.start) + "milliseconds");
	}
}



