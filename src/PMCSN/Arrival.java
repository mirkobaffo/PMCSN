package PMCSN;

import java.lang.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class Arrival  { //dovremo cambiargli nome perché questo è il thread degli arrivi

  static long LAST = 1024;                    /* number of jobs processed for 100 times */
  static double START = 0.0;                  /* initial time             */

  
  static double sarrival = START;  			   /* Why did I do this?       */
    static double lambdaR = 10.0;
  static int hPrio = 1;
  static int mPrio = 2;
  static int lPrio = 3;
  
  static int minPrioValue = 1;
  static int maxPrioValue = 4;
  
  static int minLabel = 1;
  static int maxLabel = 5;
  static char frontend = 'F';
  static char backend = 'B';
  static char wordpress = 'W';
  static char blog = 'R';


  public static ArrayList<Job> queue = new ArrayList<>();
  public static ArrayList<Job> hQueue = new ArrayList<>();
  public static ArrayList<Job> mQueue = new ArrayList<>();
  public static ArrayList<Job> lQueue = new ArrayList<>();
  
  public static Rng r = new Rng();

  public static boolean isFinish = false;
  public static long start = System.currentTimeMillis();

  public static void flow() {

      DecimalFormat f = new DecimalFormat("#.######");

      int   index     = 0;                    /* job index            */
    double interarrival = START;
    double arrival   = START;             /* time of arrival      */
    double departure = START;  					/* time of departure    */
    int priority;
    char label;
    int count = 0;
    long generator = 0;
    Random rand = new Random(System.currentTimeMillis());
    r.putSeed(Math.abs(rand.nextInt()));

    while (index < LAST ) {
            /*try {
                TimeUnit.MICROSECONDS.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } //da scommentare se classi di priorità */
            priority = Generator.getRandomInRange(minPrioValue, maxPrioValue);
            label = Generator.getRandomTopic(minLabel, maxLabel);
            interarrival = Generator.getArrival(sarrival, r, lambdaR);
            arrival += interarrival;
            Job job = new Job(interarrival, arrival, Arrival.START, departure, priority, label, index, Arrival.START, Arrival.START, Arrival.START, false, Arrival.START);
            //Utils.prioSplitter(job); //da scommentare se classi di priorità
            queue.add(job);
            sarrival = arrival;
            count++;
            index++;
        }

    }
    //System.out.println("Valore di count = " + count);





   static double exponential(double m, Rng r) {
/* ---------------------------------------------------
 * generate an Exponential random variate, use m > 0.0
 * ---------------------------------------------------
 */
    return (-m * Math.log(1.0 - r.random()));
  }

   double uniform(double a, double b, Rng r) {
/* ------------------------------------------------
 * generate an Uniform random variate, use a < b
 * ------------------------------------------------
 */
    return (a + (b - a) * r.random());
  }


   static double getArrival(Rng r) {
/* ------------------------------
 * generate the next arrival time
 * ------------------------------
 */
//    static double sarrival = START;

    sarrival += exponential(2.0, r);
    return (sarrival);
  }


   double getService(Rng r) {
/* ------------------------------
 * generate the next service time
 * ------------------------------
 */
    return (uniform(1.0, 2.0, r));
  }
    
}
