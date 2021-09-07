package PMCSN;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ServerMaster {

	public static long end;
	public static double serviceAtMaxLoad;
	public static double workingDay = 540.00 * 60; // minuti in giornata lavorativa


	public static void master() {

		Job job = new Job(Ssq2.START, Ssq2.START, Ssq2.START, Ssq2.START, 0, 'A', 0, Ssq2.START, Ssq2.START, Ssq2.START, false, Ssq2.START);
		int index = 0;
		int counter = 0;
		double delay;                                 /* delay in queue */
		double service;                               /* service time */
		double arrival = 0;
		double wait = Ssq2.START;                                /* delay + service */
		double response = Ssq2.START;
		double departure = Ssq2.START;
		double totalService = Ssq2.START;
		double time = Ssq2.START; // è l'arrivo nel server successivo
		double u = 11; // 60/5,45 --> tempo medio di servizio

		try {

			while (index < Ssq2.LAST) {

				TimeUnit.MICROSECONDS.sleep(1000);

				Job temp = null;
				if (job.getState() == true) {        //  gestione dei job in arrivo dal feedback
					Utils.checkState(job);
				} else {
					if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && Ssq2.lQueue.isEmpty()) {
						index++;
						continue;
					} else if (Ssq2.hQueue.isEmpty() && Ssq2.mQueue.isEmpty() && !Ssq2.lQueue.isEmpty()) {
						temp = Ssq2.lQueue.get(0);
						Ssq2.lQueue.remove(temp);
					} else if (Ssq2.hQueue.isEmpty() && !Ssq2.mQueue.isEmpty()) {
						temp = Ssq2.mQueue.get(0);
						Ssq2.mQueue.remove(temp);
					} else if (!Ssq2.hQueue.isEmpty()) {
						temp = Ssq2.hQueue.get(0);
						Ssq2.hQueue.remove(temp);
					}
					//}

					if (temp == null) {
						index++;
						continue;
					}

					if (temp.getArrival() < departure) {
						delay = departure - temp.getArrival();    // delay in queue
					} else {
						delay = Ssq2.START;                                 // no delay
					}
					arrival = job.getArrival();
					service = Arrival.getService(Ssq2.r, u);  // del job corrente

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
		} catch(InterruptedException e){
			e.printStackTrace();
		}

		System.out.println("Size delle code di priorità: " + Ssq2.hQueue.size() + " " + Ssq2.mQueue.size() + " " + Ssq2.lQueue.size());

		DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("Server Master\n");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getArrival() / index));
		System.out.println("   average wait ............ =   " + f.format(job.getWait() / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
		//System.out.println("   la giornata lavorativa finisce dopo .. =   " + f.format(departure+job.getService()) + "minuti");
		System.out.println("   utilization ............. =   " + f.format(totalService / departure));

		end = System.currentTimeMillis();
		System.out.println("Duration: " + (end - Ssq2.start) + "milliseconds");
	}
}



