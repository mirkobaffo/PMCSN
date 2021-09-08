package PMCSN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerFrontend {
	
	public static ArrayList<Job> fJobs = new ArrayList<>();


	public static class Container {
		double averageWait;
		double averageResponseTime;
		double serverNumber;
		double aversageDelay;
		double averageInQueue;
		List<Double> utilizations = new ArrayList<>();
		List<Double> services = new ArrayList<>();
	}


	public static Main.Container frontend() {

		
    	Job job = new Job(Arrival.START, Arrival.START, Arrival.START, Arrival.START, 0, 'A', 0, Arrival.START, Arrival.START, Arrival.START, false, Arrival.START);
    	int index = 0;
    	double delay = Arrival.START;   								/* delay in queue       */
        double arrival = Arrival.START;
    	double response = Arrival.START;
    	double service = Arrival.START;                               /* service time         */
        double wait = Arrival.START;                                  /* delay + service      */
		double totalWait = 0;
		double totalDelay = 0;
        double departure = Arrival.START;
        double totalService = Arrival.START;
        double u = 75; //60/0,8 tempo di servizio medio

        int counter = 0;

		//Qui manca il calcolo degli interarrivi

		while(!fJobs.isEmpty()) {
			Job temp;
			if (fJobs.size() > 0) {
				temp = fJobs.get(0);
				if (temp == null) {
					index++;
					continue;
				} else {
					counter++;
					fJobs.remove(temp);
				}
			} else {
				index++;
				continue;
			}

			if (temp.getArrival() < departure) {
				  delay = departure - temp.getArrival(); 	// delay in queue
				  totalDelay += delay;
			} else {
				  delay = Arrival.START;      							 // no delay
			}
			service = Generator.getService(Arrival.r, u);  // del job corrente
			response = wait + service;
			departure = temp.getArrival() + wait;    // time of departure del job corrente
			//arrival = temp.getArrival() + response; //controllare con Ilenia perchè ha fatto questo
			job = new Job(temp.getInterarrival(), temp.getArrival(), delay, departure, temp.getPriority(), temp.getLabel(), temp.getSqn(), wait, service, response, true, Arrival.START);
			totalService += service;
			wait = delay + service;		// attesa in coda del job successivo
			totalWait += wait;
			index++;
		}
		/*
		DecimalFormat f = new DecimalFormat("#.######");
		System.out.println("                               ");
		System.out.println("          SERVER FRONTEND          ");
		System.out.println("                               ");
		System.out.println("\nfor " + index + " jobs");
		System.out.println("   average interarrival time =   " + f.format(job.getArrival() / index));
		System.out.println("   average wait ............ =   " + f.format(totalWait / index));
		System.out.println("   average delay ........... =   " + f.format(job.getDelay() / index));
		System.out.println("   average service time .... =   " + f.format(totalService / index));
		System.out.println("   average # in the node ... =   " + f.format(job.getWait() / departure));
		System.out.println("   average # in the queue .. =   " + f.format(job.getDelay() / departure));
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
		return c;
    }

}
