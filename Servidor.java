
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    public static void main(String[] args) {
        try {
            
            ConcurrentHashMap<String, MySocket> m = new ConcurrentHashMap<String, MySocket>(); //clau=String, valor=MySocket
            MyServerSocket ss = new MyServerSocket(Integer.parseInt(args[0])); //port del servidor
            
            while (true) {
                MySocket s = ss.accept(); // retorna socket connectat quan un s'hi conecta

                (new Thread(){ //Assistant
                    String nick;
                    
                    void connect() {
                        do{
                                nick = s.readLine();
                            if(!m.containsKey(nick)){
                                break;
                            }else{
                                //inform user that that nick is already in use
                                s.println("L'usuari " + nick + " ja existeix, introdueix un altre nick: "); 
                                //nick = s.readLine();
                            }
                        }while(true);
                        
                        //inform user about the other users connected
                        String users =  m.keySet().toString();
                        s.println("Usuaris connectats: " + users);
                        
                        m.putIfAbsent(nick, s); 
                        
                        // inform users about a new user joining and add user
                        for(MySocket s : m.values()){
                            s.println("L'usuari " + nick + " s'ha unit");
                        }

                    }
    
                    void disconnect() {
                        // remove user and inform users about user leaving
                        m.remove(nick);
                        for(MySocket s :  m.values()){
                            s.println("L'usuari: " + nick + " s'ha desconnectat");
                        }
                    }
                    
                    void broadcast(MySocket s, String str){
                        for(MySocket socket : m.values()){
                            if(s == socket) continue;
                            socket.println(nick + "> " + str);
                        }
                    }

                    @Override
                    public void run() {
                            connect();
                            String msg;
                            while ((msg = s.readLine()) != null) { 
                                broadcast(s, msg);
                            }
                            disconnect();
                            s.close();       
                    }
                }).start();
            }
        } catch (Exception ex) {
        }
    }    
}