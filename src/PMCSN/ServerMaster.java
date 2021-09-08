package PMCSN;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ServerMaster implements Runnable{

	public static long end;
	public static double serviceAtMaxLoad;
	public static double workingDay = 540.00 * 60; // minuti in giornata lavorativa


	public void run() {

		Job job = new Job(Arrival.START, Arrival.START, Arrival.START, Arrival.START, 0, 'A', 0, Arrival.START, Arrival.START, Arrival.START, false, Arrival.START);
		int index = 0;
		int counter = 0;
		double delay;                                 /* delay in queue */
		double service = 0;                               /* service time */
		double arrival = 0;
		double totalDelay = 0;
		double wait = Arrival.START;                                /* delay + service */
		double response = Arrival.START;
		double totalWait = 0;
		double departure = Arrival.START;
		double totalService = Arrival.START;
		double time = Arrival.START; // è l'arrivo nel server successivo
		double u = 11; // 60/5,45 --> tempo medio di servizio
		double wait1 = 0;
		double wait2 = 0;
		double wait3 = 0;
		int departure1 = 0;
		int departure2 = 0;
		int departure3 = 0;

		DecimalFormat f = new DecimalFormat("#.######");

		try {
		while (index < Arrival.LAST ) {
		//while (!Arrival.hQueue.isEmpty() || !Arrival.mQueue.isEmpty() || !Arrival.lQueue.isEmpty()) {

			TimeUnit.MICROSECONDS.sleep(1000); // questa sleep può essere tolta ????
				Job temp = null;	
				if (Arrival.hQueue.isEmpty() && Arrival.mQueue.isEmpty() && Arrival.lQueue.isEmpty()) {
					//index ++;
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
				if(temp.getPriority() == 1){
					wait1 += wait;
					departure1 ++;
				}
				else if (temp.getPriority() == 2){
					wait2 += wait;
					departure2 ++;
				}
				else if (temp.getPriority() == 3){
					wait3 += wait;
					departure3 ++;
				}
				//System.out.println(counter + " - questa è la wait: " + wait + " questo è il service " + service + " questo è l'arrivo " + arrival + " coda di priorità: " + temp.getPriority() + " questa è la delay  " + delay + " questa è la departure " + departure);
				totalWait += wait;
				Utils.topicSplitter(job);
				index++;
				counter++;
				/*if (arrival > 59700) {
					System.out.println("Ora esco dal while");
					break;
				}*/
			}
			//System.out.println("Valore di counter = " + counter);

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
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));
		System.out.println("   average wait prio1 ............ =   " + f.format(wait1 / departure1));
		System.out.println("   average wait prio2 ............ =   " + f.format(wait2 / departure2));
		System.out.println("   average wait prio3 ............ =   " + f.format(wait3 / departure3));
		System.out.println("prio1: " + departure1 +  " prio2: " + departure2 +  " prio3: " + departure3);
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
	}
}



