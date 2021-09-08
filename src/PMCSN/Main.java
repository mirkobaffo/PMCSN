package PMCSN;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

	public static int numOfBatch = 64;
	public static int totSum = 0;
	public static double totalWait = 0;
	public static double totalService = 0;
	public static double totalUtilization = 0;

	public static class Container {
		double averageWait;
		double serverNumber;
		double aversageDelay;
		double averageInQueue;
		List<Double> responseTime = new ArrayList<>();
		List<Double> utilizations = new ArrayList<>();
		List<Double> services = new ArrayList<>();
	}

	static List<Container> allJob = new ArrayList<>();
	static List<Container> containerMaster = new ArrayList<>();
	static List<Container> containerFront = new ArrayList<>();
	static List<Container> containerBack = new ArrayList<>();
	static List<Container> containerWord = new ArrayList<>();
	static List<Container> containerBlog = new ArrayList<>();


	public static void main(String[] args) {
		for (int i = 0; i < numOfBatch; i ++) {
			//per inserire classi di priorità, scommentare da riga 31 a 32, e da 36 a 40 della classe Main e da riga 59 a 63 oltre a riga 69 della classe arrival
			//Thread t = new Thread(new ServerMaster());
			//t.start();
			Arrival.flow();
			Arrival.isFinish = true;
			containerMaster.add(ServerMasterSingleQueue.master());
			/*try {
				TimeUnit.MICROSECONDS.sleep(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			containerBack.add(ServerBackend.backend());
			containerWord.add(ServerWordpress.wordpress());
			//containerFront.add(ServerFrontend.frontend());
			containerFront.add(ServerFrontendImproved.frontEnd()); //questo il miglioramento sul frontend
			containerBlog.add(ServerBlog.blog());
		}
		allJob.addAll(containerFront);
		allJob.addAll(containerBack);
		allJob.addAll(containerBlog);
		allJob.addAll(containerWord);
		System.out.println("      MASTER     ");
		System.out.println("                  ");
		getValueFromContainer(containerMaster, numOfBatch);
		System.out.println("                  ");
		System.out.println("      BACKEND     ");
		System.out.println("                  ");
		getValueFromContainer(containerBack, numOfBatch);
		System.out.println("                  ");
		System.out.println("      FRONTEND     ");
		System.out.println("                  ");
		getValueFromContainer(containerFront, numOfBatch);
		System.out.println("                  ");
		System.out.println("      WordPress     ");
		System.out.println("                  ");
		getValueFromContainer(containerWord, numOfBatch);
		System.out.println("                  ");
		System.out.println("      BLOG     ");
		System.out.println("                  ");
		getValueFromContainer(containerBlog, numOfBatch);
		System.out.println("                  ");
		System.out.println("      TUTTI I JOB     ");
		System.out.println("                  ");
		System.out.println("Attesa media: " + totalWait/totSum);
		System.out.println("servizio medio: " + totalService/totSum);
		System.out.println("tempo di risposta medio: " + totalWait/totSum + totalService/totSum);
		System.out.println("utilizzazione media: " + totalUtilization/totSum);







	}

	public static void getValueFromContainer(List<Container> cList, int numOfBatch){
		Container container = new Container();
		double wait = 0;
		double service1 = 0;
		double server = 0;
		double service2 = 0;
		double delay = 0;
		double averageinQueue = 0;
		double response1 = 0;
		double response2 = 0;
		double utilization1 = 0;
		double utilization2 = 0;
		double service3 = 0;
		double utilization3 = 0;
		server = cList.get(0).serverNumber;
		for(int i = 0; i < numOfBatch; i ++){
			wait += cList.get(i).averageWait;
			averageinQueue += cList.get(i).averageInQueue;
			delay += cList.get(i).aversageDelay;
			if(cList.get(i).serverNumber>1){
				service1 += cList.get(i).services.get(0);
				service2 += cList.get(i).services.get(1);
				//service3 += cList.get(i).services.get(2);
				utilization1 += cList.get(i).utilizations.get(0);
				utilization2 += cList.get(i).utilizations.get(1);

				//utilization3 += cList.get(i).utilizations.get(2);
			}
			else{
				service1 += cList.get(i).services.get(0);
				utilization1 += cList.get(i).utilizations.get(0);
			}
		}

		container.serverNumber = server;
		container.averageWait = wait/numOfBatch;
		container.averageInQueue = averageinQueue/numOfBatch;
		container.aversageDelay = delay/numOfBatch;
		container.services.add(service1/numOfBatch);
		container.utilizations.add(utilization1/numOfBatch);
		container.responseTime.add(response1/numOfBatch);
		totalWait += wait/numOfBatch;

		if(server>1) {
			container.services.add(service2/numOfBatch);
			container.utilizations.add(utilization2/numOfBatch);
			container.responseTime.add(response2/numOfBatch);
			totalUtilization += utilization2/numOfBatch + utilization1/numOfBatch;
			totalService += service2/numOfBatch + service1/numOfBatch;
			totSum += 2;

		}
		else{
			totalUtilization += utilization1/numOfBatch;
			totalService += service1/numOfBatch;
			totSum ++;


		}

		printValueFromContainer(container);

	}


	public static void printValueFromContainer(Container c){
		System.out.println("		Average Wait: " + c.averageWait);
		System.out.println("		Average in queue: " + c.averageInQueue);
		System.out.println("		Average Delay: " + c.aversageDelay);
		System.out.println("                  ");
		if(c.serverNumber>1){
			System.out.println("     MULTISERVER");
			System.out.println("                  ");
			System.out.println("    server         utilization         avg service        avg response time");
			System.out.println("	  1        " + c.utilizations.get(0) + "       " + c.services.get(0) + "       " + (c.services.get(0) + c.averageWait));
			System.out.println("      2        " + c.utilizations.get(1) + "       " + c.services.get(1)+ "       " + (c.services.get(1) + c.averageWait));

		}
		else{
			System.out.println( "	Average Service time: " + c.services.get(0));
			System.out.println( "	Average response time: " + (c.services.get(0)+c.averageWait));
			System.out.println( "	Utilization: " + c.utilizations.get(0));
		}
	}
}