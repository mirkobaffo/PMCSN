package PMCSN;
/*** La classe Patient rappresenta il singolo job 
 * */

public class Job {
	
	private int interarrival;
	private int priority; // 1=high, 2=medium, 3=low
	private int sqn; //sequence number
	private double wait; //tempo di attesa
	private double service; //tempo di servizio
	private double response; //tempo di risposta
	
	
	
	public Job(int interarrival, int priority) {
		super();
		this.interarrival = interarrival;
		this.priority = priority;
	}
	
	public int getInterarrival() {
		return interarrival;
	}
	public void setInterarrival(int interarrival) {
		this.interarrival = interarrival;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getSqn() {
		return sqn;
	}
	public void setSqn(int sqn) {
		this.sqn = sqn;
	}
	public double getWait() {
		return wait;
	}
	public void setWait(double wait) {
		this.wait = wait;
	}
	public double getService() {
		return service;
	}
	public void setService(double service) {
		this.service = service;
	}
	public double getResponse() {
		return response;
	}
	public void setResponse(double response) {
		this.response = response;
	}
	
	
}
