package PMCSN;

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
	private final boolean state; // lo stato del job può essere: 
							//true = servito (da revisionare), false = non servito  
	private final double time;
	
	
	
	public Job(double interarrival, double arrival, double delay, double departure, int priority, char label, int sqn, double wait, double service, double response, boolean state, double time) {

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
		this.time = time;
	}
	
	public double getInterarrival() {
		return interarrival;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public int getSqn() {
		return sqn;
	}
	
	public double getWait() {
		return wait;
	}
	
	public double getService() {
		return service;
	}
	
	public double getResponse() {
		return response;
	}
	
	public double getDelay() {
		return delay;
	}
	/*public void setDelay(double delay) {
		this.delay = delay;
	}*/
	public double getArrival() {
		return arrival;
	}
	
	public char getLabel() {
		return label;
	}
	
	public double getDeparture() {
		return departure;
	}
	
	public boolean getState() {
		return state;
	}
	
	public double getTime() {
		return time;
	}
	


	
}
