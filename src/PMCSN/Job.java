package PMCSN;
/*** La classe Patient rappresenta il singolo job 
 * */

public class Job {
	
	private double interarrival;
	private double arrival;
	private double delay;      /*   delay times  */
	private int priority; // 1=high, 2=medium, 3=low
	private char topic; // 1:'F'=frontend, 2:'B'=backend, 3:'W'=wordpress, 4:'R'=blog
	private int sqn; //sequence number
	private double wait; //tempo di attesa
	private double service; //tempo di servizio
	private double response; //tempo di risposta
	
	public void initParams() {
	    delay = 0.0;
	    wait = 0.0;
	    service = 0.0;
	    interarrival = 0.0;
	}
	
	public Job() { }
	
	public Job(double arrival, int priority, char topic) {
		super();
		this.arrival = arrival;
		this.priority = priority;
		this.topic = topic;
	}
	
	public double getInterarrival() {
		return interarrival;
	}
	public void setInterarrival(double interarrival) {
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
	public double getDelay() {
		return delay;
	}
	public void setDelay(double delay) {
		this.delay = delay;
	}
	public double getArrival() {
		return arrival;
	}
	public void setArrival(double arrival) {
		this.arrival = arrival;
	}
	public char getTopic() {
		return topic;
	}
	public void setTopic(char topic) {
		this.topic = topic;
	}

	
}
