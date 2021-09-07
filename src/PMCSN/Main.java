package PMCSN;

public class Main {
	
	public static void main(String[] args) {
		Arrival.flow();
		while (!Arrival.hQueue.isEmpty() || !Arrival.mQueue.isEmpty() || !Arrival.lQueue.isEmpty()) {
			ServerMaster.master();
			ServerBackend.backend();
			ServerWordpress.wordpress();
			ServerFrontend.frontend();
			ServerBlog.blog();
		}
	
    }
}
