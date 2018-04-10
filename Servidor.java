/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica8;
import practica8.Assistant;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {

    public static void main(String[] args) {
        try {
            ConcurrentHashMap<String, PrintWriter> m = new ConcurrentHashMap();
            MyServerSocket ss = new MyServerSocket(9999); //port del servidor
            while (true) {
                MySocket s = ss.accept(); // retorna socket connectat quan un shi conecta
                new Thread(new Assistant(s, m)).start();

            }
        } catch (Exception ex) {
            
        }

    }
    

}
