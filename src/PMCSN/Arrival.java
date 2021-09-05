package PMCSN;
import java.util.ArrayList;

public class Arrival {
	
	public static double getArrival(double sarrival, Rng r, double m) {
		/* ------------------------------
		 * generate the next arrival time
		 * ------------------------------
		 */
//		    static double sarrival = START;

		    sarrival = Generator.exponentialGenerator(m, null, r);
		    return (sarrival);
	}
	
	/*public static double getMultiArrival(double sarrival, Rngs r, double m) {

		 // generate the next arrival time, with rate 1/2

		r.selectStream(0);
		sarrival = Generator.exponentialGenerator(m, r, null);
		return (sarrival);
	}*/

	
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
