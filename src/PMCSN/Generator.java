package PMCSN;

public class Generator {
		
	public static int getRandomInRange(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
		
	
	public static double exponentialGenerator(double m, Rngs g, Rng r) {
		/* ---------------------------------------------------
		 * generate an Exponential random variate, use m > 0.0
		 * ---------------------------------------------------
		 */

		if (g == null) {
		    return ((-m * Math.log(1.0 - r.random())));
		} else {
			return ((-m * Math.log(1.0 - g.random())));
		}
	}
	
	public static boolean getWeighBoolean(int percentage) { 
		double num = Math.random();
		if (num > percentage) {
			return false;
		}
		return true;
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
	
	public static double getArrival(double sarrival, Rng r, double m) {
		/* ------------------------------
		 * generate the next arrival time
		 * ------------------------------
		 */
//		    static double sarrival = START;

		    sarrival = Generator.exponentialGenerator(m, null, r);
		    return (sarrival);
	}
	
	public static double getService(Rng r, double u) {
		/* ------------------------------
		 * generate the next service time
		 * ------------------------------
		 */
		    return (Generator.exponentialGenerator(u, null, r));
	}

	public static double getMultiService(Rngs r, double u) {

		// generate the next service time, with rate 1/6

		r.selectStream(1);
		return (Generator.exponentialGenerator(u, r, null));
	}

}
