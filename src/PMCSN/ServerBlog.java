package PMCSN;

public class ServerBlog implements Runnable {

	public void run() {
        //while(true){
            System.out.println("ciao Blog" + Thread.currentThread().getId());
        
    }
}
