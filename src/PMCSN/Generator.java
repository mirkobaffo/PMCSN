package PMCSN;
import org.apache.commons.math3.distribution.PoissonDistribution;

public class Generator {
		
	public static int getRandomPriority(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
		
	public static int poissonGenerator() {
		int seed = 6; //10 arrivi/ora
		PoissonDistribution distribution = new PoissonDistribution(seed);
		int interarrival = distribution.sample();
		return interarrival;
	}
	
}
