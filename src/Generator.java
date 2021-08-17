import org.apache.commons.math3.distribution.PoissonDistribution;

public class Generator {
		
	public static boolean boolGenerator(double probabilityTrue){
	    return Math.random() >= 1.0 - probabilityTrue;
	}

		
	public static int poissonGenerator() {
		
		int seed = 6; //10 arrivi/ora
		
		PoissonDistribution distribution = new PoissonDistribution(seed);
		int interarrival = distribution.sample();
		return interarrival;
	}
	
}
