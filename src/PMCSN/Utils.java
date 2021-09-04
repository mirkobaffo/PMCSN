package PMCSN;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utils {
	
	public static void prioSplitter(Job job) {
		if (job.getPriority() == Ssq2.hPrio) {
			Ssq2.hQueue.add(job); 
	  } else if (job.getPriority() == Ssq2.mPrio) {
		  Ssq2.mQueue.add(job); 
	  } else {
		  Ssq2.lQueue.add(job); 
	  }
	}
	
	public static void topicSplitter(Job job) {
		
		if (job.getTopic() == 'F') {
			//System.out.println("sto nell'if del frontend: " + job.getTopic());
			ServerFrontend.fJobs.add(job);
			//System.out.println("questa è la size della coda  frontend: " + ServerFrontend.fJobs.size());

		} else if (job.getTopic() == 'B') {
			//System.out.println("sto nell'if del backend: " + job.getTopic());
			ServerBackend.bJobs.add(job);
			//System.out.println("questa è la size della coda dei blog: " + ServerBackend.bJobs.size());

		} else if (job.getTopic() == 'W') {
			//System.out.println("sto nell'if del wordpress: " + job.getTopic());
			ServerWordpress.wJobs.add(job);
			//System.out.println("questa è la size della coda dei blog: " + ServerWordpress.wJobs.size());

		} else if (job.getTopic() == 'R') {
			//System.out.println("sto nell'if del blog: " + job.getTopic());
			ServerBlog.rJobs.add(job);
			//System.out.println("questa è la size della coda dei blog: " + ServerBlog.rJobs.size());
		}
		
	}
	
	/*public static void checkState(Job job) {
		if (Generator.getWeighBoolean(80) == true) {
			DecimalFormat f = new DecimalFormat("###0.00");
			//System.out.println("   type of the job .. =   " + job.getTopic());
			//System.out.println("   interarrival time =   " + f.format(job.getInterarrival()));
			//System.out.println("   wait ............ =   " + f.format(job.getWait()));
			//System.out.println("   delay ........... =   " + f.format(job.getDelay()));
			//System.out.println("   service time .... =   " + f.format(job.getService()));
		} else {
			job.setState(false);
			topicSplitter(job);
		} 
		
	}*/

}
