package PMCSN;

import java.io.FileWriter;
import java.io.IOException;

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
			ServerFrontend.fJobs.add(job);
		} else if (job.getTopic() == 'B') {
			ServerBackend.bJobs.add(job);
		} else if (job.getTopic() == 'W') {
			ServerWordpress.wJobs.add(job);
		} else if (job.getTopic() == 'R') {
			ServerBlog.rJobs.add(job);
		}
	}
	
	public static void checkState(Job job) {
		if (Generator.getWeighBoolean(80) == true) {
			System.out.println("Qui scriveremo tutto quello che vogliamo sapere del job che esce");
		} else {
			job.setState(false);
			topicSplitter(job);
		} 
		
	}

}
