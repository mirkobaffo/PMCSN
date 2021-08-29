package PMCSN;

public class ServerBackend implements Runnable {

	public void run() {
        //while(true){
            System.out.println("ciao Backend" + Thread.currentThread().getId());
        
    }
}
