package PMCSN;


//Questa classe è solo esplicativa per vedere come funziona un multiThread
public class Server implements Runnable{

    public void run() {
        while(true){
            System.out.println("ciao");
        }
    }

    //da come ho capito il metodo start può essere chiamato solo in una classe che implementa l'interfaccia Runnable
    public static void main(String[] args) {
        Server s = new Server();
        Thread t = new Thread(s);
        t.start();
    }


}
