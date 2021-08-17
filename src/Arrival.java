import java.util.ArrayList;

public class Arrival {

	public static ArrayList<Patient> getArrival() {	
	
		int interval = 120; //abbiamo fasce orarie da 2 ore
		int totTime = 0;
		double pIllness = 0.3;
		double pPrescription = 0.3;
		
		ArrayList<Patient> sbQueue = new ArrayList<>();
		
		while(totTime < interval) {
			
			boolean illness = Generator.boolGenerator(pIllness);
			boolean prescription = Generator.boolGenerator(pPrescription);
			int interarrival = totTime + Generator.poissonGenerator();
			Patient job = new Patient(interarrival, illness, prescription);
			sbQueue.add(job);
			
			totTime = totTime + interarrival;
		}
		
		return sbQueue;
	}
}
