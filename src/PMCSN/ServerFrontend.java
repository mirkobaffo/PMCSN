package PMCSN;

public class ServerFrontend implements Runnable {
	
	public void run() {
		int i = 0;
        while(i < 50){
        	i++;
            System.out.println("ciao Frontend" + Thread.currentThread().getId());
        }
        
    }

}
