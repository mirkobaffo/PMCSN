/*** La classe Patient rappresenta il singolo job 
 * */

public class Patient {
	
	private int interarrival;
	private boolean illness;
	private boolean prescription;
	private int sqn; //sequence number
	private double wait; //tempo di attesa
	private double service; //tempo di servizio
	private double response; //tempo di risposta
	
	
	
	public Patient(int interarrival, boolean illness, boolean prescription) {
		super();
		this.interarrival = interarrival;
		this.illness = illness;
		this.prescription = prescription;
	}
	
	public int getInterarrival() {
		return interarrival;
	}
	public void setInterarrival(int interarrival) {
		this.interarrival = interarrival;
	}
	public boolean isIllness() {
		return illness;
	}
	public void setIllness(boolean illness) {
		this.illness = illness;
	}
	public boolean isPrescription() {
		return prescription;
	}
	public void setPrescription(boolean prescription) {
		this.prescription = prescription;
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
