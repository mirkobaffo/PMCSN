package PMCSN;

/* 1. rimettere le code globali ora che il problema è risolto e non era quello - bug oggetti Job mutable fixato)
 * 2. capire perché le stampe danno tutti 0.00
 * 3. boh continua a testare con gli altri server e con tutti insieme
 */

public class Main {
	
	public static void main(String[] args) {
		Thread ssq2 = new Thread(new Ssq2());
		ssq2.start();
        Thread master = new Thread(new ServerMaster());
        master.start();
        for (int i = 0; i < 3; i++) {
	        Thread backend = new Thread(new ServerBackend());
	        backend.start();
		}
	    Thread frontend = new Thread(new ServerFrontend());
        frontend.start();
	    for (int j = 0; j < 2; j++) {
	        Thread wordpress = new Thread(new ServerWordpress());	        
	        wordpress.start();  
	    }
	    for (int k = 0; k < 2; k++) {
	        Thread blog = new Thread(new ServerBlog());
	        blog.start();
	    }
    }
}
