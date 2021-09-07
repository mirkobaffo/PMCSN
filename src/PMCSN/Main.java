package PMCSN;

/*
 * 1. capire perché le stampe danno tutti 0.00
 * 2. boh continua a testare con gli altri server e con tutti insieme
 */

public class Main {
	
	public static void main(String[] args) {
		Ssq2.ssqArrive();
		while (!Ssq2.hQueue.isEmpty() || !Ssq2.mQueue.isEmpty() || !Ssq2.lQueue.isEmpty()) {
			ServerMaster.master();
			ServerBackend.backend();
			ServerWordpress.wordpress();
			ServerFrontend.frontend();
			ServerBlog.blog();
		}
		/*Thread ssq2 = new Thread(new Ssq2());
		ssq2.start();
		Thread master = new Thread(new ServerMaster());
        master.start();
		Thread backend = new Thread(new ServerBackend());
		backend.start();
	    Thread frontend = new Thread(new ServerFrontend());
        frontend.start();
        Thread wordpress = new Thread(new ServerWordpress());
        wordpress.start();
        Thread blog = new Thread(new ServerBlog());
        blog.start();*/

    }
}
