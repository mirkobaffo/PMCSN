package PMCSN;
/*** La classe Patient rappresenta il singolo job 
 * */

public final class Job {
	
	private final double interarrival;
	private final double arrival;
	private final double delay;      /*   delay times  */
	private final double departure;
	private final int priority; // 1=high, 2=medium, 3=low
	private final char label; // 1:'F'=frontend, 2:'B'=backend, 3:'W'=wordpress, 4:'R'=blog
	private final int sqn; //sequence number
	private final double wait; //tempo di attesa
	private final double service; //tempo di servizio
	private final double response; //tempo di risposta
	private final boolean state; /* lo stato del job può essere: 
							true = servito (da revisionare), false = non servito  */
	
	
	
	/*public void initParams() {
	    delay = 0.0;
	    wait = 0.0;
	    service = 0.0;
	    interarrival = 0.0;
	}*/
	
	
	public Job(double interarrival, double arrival, double delay, double departure, int priority, char label, int sqn, double wait, double service, double response, boolean state) {

		this.interarrival = interarrival;
		this.arrival = arrival;
		this.delay = delay;
		this.departure = departure;
		this.priority = priority;
		this.label = label;
		this.sqn = sqn;
		this.wait = wait;
		this.service = service;
		this.response = response;
		this.state = state;
	}
	
	public double getInterarrival() {
		return interarrival;
	}
	/*public void setInterarrival(double interarrival) {
		this.inter*/
	public int getPriority() {
		return priority;
	}
	/*public void setPriority(int priority) {
		this.priority = priority;
	}*/
	public int getSqn() {
		return sqn;
	}
	/*public void setSqn(int sqn) {
		this.sqn = sqn;
	}*/
	public double getWait() {
		return wait;
	}
	/*public void setWait(double wait) {
		this.wait = wait;
	}*/
	public double getService() {
		return service;
	}
	/*public void setService(double service) {
		this.service = service;
	}*/
	public double getResponse() {
		return response;
	}
	/*public void setResponse(double response) {
		this.response = response;
	}*/
	public double getDelay() {
		return delay;
	}
	/*public void setDelay(double delay) {
		this.delay = delay;
	}*/
	public double getArrival() {
		return arrival;
	}
	/*public void setArrival(double arrival) {
		this.arrival = arrival;
	}*/
	public char getLabel() {
		return label;
	}
	/*public void setTopic(char topic) {
		this.topic = topic;
	}*/
	public double getDeparture() {
		return departure;
	}
	/*public void setDeparture(double departure) {
		this.departure = departure;
	}*/
	public boolean getState() {
		return state;
	}
	/*public void setState(boolean state) {
		this.state = state;
	}*/
	
}
