/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica8;

import java.io.*;

/**
 *
 * @author Berta
 */
public class Client {
    
    static String nick;
    static PrintWriter socketout;
    static BufferedReader socketin;
    static PrintWriter out = new PrintWriter(System.out);;
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    public static void main(String[] args) {
     
        try {
            MySocket s = new MySocket("localhost", 12345);
            
            // define in out socket streams
            socketout = s.getPrintWriter();
            socketin = s.getBufferedReader();
            
            // read and send nick
            
            
            // output thread
            new Thread() {
                public void run() {
                    String msg;
                    try {
                        while ((msg = socketin.readLine()) != null) {
                            // write msg to console
                            out.println(msg);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }                
            }.start();
            
            // input thread
            new Thread() {
                public void run() {
                    String linia = null;
                    try{
                        linia = in.readLine();
                    }catch(IOException e){
                        
                    }
                    while (linia != null) {
                        // write line to socket
                        s.write(linia);
                    }
                    
                }                
            }.start();
            
            // close socket for output
            s.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
