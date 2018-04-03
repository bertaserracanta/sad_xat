

import java.io.*;
import java.net.*;

public class MySocket {
    Socket s;
     BufferedReader in;
     PrintWriter out;
    public MySocket(InetAddress ip,int port){
        try{
            s = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        }catch(IOException e){
            System.err.println("Couldn't create the connection");
            System.exit(1);
        }
    }
    
    public void close(){
        try{
            s.close();
        }catch(IOException e){
            System.err.println("Couldn't close the connection");
            System.exit(1);
        }
    }
    
    public void connect(SocketAddress addrSocket){
        try{
            s.connect(addrSocket);
        }catch(IOException e){
            
        }
    }
    
    public InetAddress getInetAddress(){ //returns the address of the remote machine that is connected to the socket
        return s.getInetAddress();
    }
    
    
    public int getLocalPort(){
        return s.getLocalPort();
    }
    
    public int getPort(){ //retorna el port al que esta connectat
        return s.getPort();
    }
    
    public void write(String str){
            out.print(str);
    }
    
    public String read(){
        String llegit = null;
        try{
            llegit = in.readLine();
        }catch(IOException e){
            System.err.println("Couldn't get I/O for the connection");
            System.exit(1);
        }
        return llegit;
    }
    
    public boolean isReady(){
        boolean ready = false;
        try{
            ready = in.ready();
        }catch(IOException e){
            
        }
        return ready;
    }
    
}
