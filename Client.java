
import java.net.*;
import java.io.*;
import java.util.concurrent.locks.*;

public class Client {
    protected MySocket s;
    protected Lock lk;
    protected Condition hiHaLinia;
    
    public Client(InetAddress addr, int port){
        s = new MySocket(addr, port);
        lk = new ReentrantLock();
        hiHaLinia = lk.newCondition();
        
        new Thread(new Lectura()).start();
        new Thread(new Escriptura()).start();
        
        
    }
    
    //Thread lector
    private class Lectura implements Runnable {
        @Override
        public void run() {
            while (s.isReady()){
                //escriure línia per pantalla;   
                System.out.print(s.read());
            }
        }
        
    }
    
    //Tread d'escriptura
    private class Escriptura implements Runnable {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String linia = null;
        
        @Override
        public void run() {
            try{
            while ((linia = stdIn.readLine()) != null){}
                //escriure línia per socket;
                s.write(linia);
            }catch(IOException e){
                
            }
        }
        
    }
}
