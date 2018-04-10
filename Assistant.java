package practica8;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;


public class Assistant implements Runnable {

    protected MySocket s;
    protected String nick;
    protected ConcurrentHashMap m;
    protected BufferedReader entrada;
    protected PrintWriter sortida;

    
    public Assistant(MySocket so, ConcurrentHashMap<String, MySocket> map) {
        s = so;
        m = map;
                
        try {
            entrada = s.getBufferedReader();
            sortida = s.getPrintWriter();
        } catch (Exception e) {
        }
    }
    
    public void connect() {

        try {
            // read nick
            nick = entrada.readLine();
            
            // inform users about a new user joining and add user
            m.put(nick, s);
            sortida.write("L'usuari: " + nick + "s'ha unit");
            
        } catch (Exception ex) {
        }

    }
    
    public void disconnect() {
        try {
            // remove user and inform users about user leaving
            m.remove(nick);
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        try {
            connect();

            String msg;
            while ((msg = entrada.readLine()) != null) {
                // send nick and msg content to other users
                sortida.write("Usuari: " + nick + " diu: " + msg);
            }

            disconnect();
            s.close();                            
        } catch (Exception ex) {
        }
    }

}
