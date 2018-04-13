
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    public static void main(String[] args) {
        try {
            
            ConcurrentHashMap<String, MySocket> m = new ConcurrentHashMap<String, MySocket>(); //clau=String, valor=MySocket
            MyServerSocket ss = new MyServerSocket(Integer.parseInt(args[0])); //port del servidor
            
            while (true) {
                MySocket s = ss.accept(); // retorna socket connectat quan un shi conecta

                (new Thread(){ //Assistant
                    String nick;

                    void connect() {
                        Iterator<MySocket> i = m.values().iterator();
                        try {
                            nick = s.readLine();
                            if(m.containsKey(nick)){
                                //inform user that that nick is already in use
                                s.println("L'usuari: " + nick + "ja existeix, introdueix un altre nick: "); 
                                nick = s.readLine();
                            }
                            // inform users about a new user joining and add user
                            m.putIfAbsent(nick, s); 
                            while(i.hasNext()){
                                i.next().println("L'usuari: " + nick + " s'ha unit");
                            }
                        } catch (Exception ex) {
                        }

                    }
    
                    void disconnect() {
                        Iterator<MySocket> i = m.values().iterator();
                        try {
                            // remove user and inform users about user leaving
                            m.remove(nick);
                            while(i.hasNext())
                            i.next().println("L'usuari: " + nick + " s'ha desconnectat");
                        } catch (Exception ex) {
                        }
                    }

                    @Override
                    public void run() {
                            connect();
                            
                            String msg;
                            while ((msg = s.readLine()) != null) { //sha d'estar connectat mentre el client no envii ctrl D
                                // send nick and msg content to other users
                                Iterator<MySocket> i = m.values().iterator();
                                while(i.hasNext()){
                                    i.next().println(nick + " diu: " + msg);
                                }
                            }

                            disconnect();
                            s.close();       //no se si va aqui, o dins del disconect un metode qeu tanqui avisant l'output thread del client                     
                    }
                }).start();
            }
        } catch (Exception ex) {
        }
    }    
}


/*class Assistant implements Runnable {

    protected MySocket s;
    protected String nick;
    protected ConcurrentHashMap m;
    
    public Assistant(MySocket so, ConcurrentHashMap<String, MySocket> map) {
        s = so;
        m = map;
    }
    
    public void connect() {
        Iterator<MySocket> i = m.values().iterator();
        try {
            nick = s.readLine();
            if(m.containsKey(nick)){
                //inform user that that nick is already in use
                s.println("L'usuari: " + nick + "ja existeix"); //sortida o system? System.out.println("L'usuari: " + nick + "ja existeix");
            }else{
                // inform users about a new user joining and add user
                m.putIfAbsent(nick, s); //no se si el que retorna aquest metode pot utilitzarse per tractar si un usuari s'intenta connectar amb el matix nick
                while(i.hasNext()){
                    i.next().println("L'usuari: " + nick + "s'ha unit");
                }
            }
        } catch (Exception ex) {
        }

    }
    
    public void disconnect() {
        Iterator<MySocket> i = m.values().iterator();
        try {
            // remove user and inform users about user leaving
            m.remove(nick);
            while(i.hasNext())
            i.next().println("L'usuari: " + nick + "s'ha desconnectat");
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
            connect();
            
            Iterator<MySocket> i = m.values().iterator();
            
            String msg;
            while ((msg = s.readLine()) != null) { //sha d'estar connectat mentre el client no envii ctrl D
                // send nick and msg content to other users
                while(i.hasNext()){
                    i.next().println(nick + " diu: " + msg);
                }
            }

            disconnect();
            s.close();       //no se si va aqui, o dins del disconect un metode qeu tanqui avisant l'output thread del client                     
    }
}*/