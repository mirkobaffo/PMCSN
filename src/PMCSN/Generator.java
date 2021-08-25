package PMCSN;

public class Generator {
		
	public static int getRandomInRange(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
		
	
	public static double exponentialGenerator(double m, Rng r) {
		/* ---------------------------------------------------
		 * generate an Exponential random variate, use m > 0.0
		 * ---------------------------------------------------
		 */
		    return (-m * Math.log(1.0 - r.random()));
	}
	
	public static char getRandomTopic(int min, int max) {
		char ret = ' ';
		int topicValue = Generator.getRandomInRange(min, max);
		if (topicValue == 1) {
			ret = 'F';
		} else if (topicValue == 2) {
			ret = 'B';
		} else if (topicValue == 3) {
			ret = 'W';
		} else if (topicValue == 4) {
			ret = 'R';
		} else {
			System.out.println("Topic not found");
		}
		return ret;
	}

	/*
	 * sbagliato perché gli ARRIVI sono poissoniani, 
	 * mentre gli INTERARRIVI sono esponenziali
	 * 
	 * public static int poissonGenerator() {
		int seed = 6; //10 arrivi/ora
		PoissonDistribution distribution = new PoissonDistribution(seed);
		int interarrival = distribution.sample();
		return interarrival;
	}*/
	
}
