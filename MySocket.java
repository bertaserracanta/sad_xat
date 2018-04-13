
import java.io.*;
import java.net.*;

public class MySocket {
    Socket s;
    BufferedReader in;
    PrintWriter out;
    
    public MySocket(Socket socket){
        try{
            s = socket;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        }catch(IOException e){
            System.err.println("Couldn't create the connection");
            System.exit(1);
        }
    }
    
    public MySocket(String str, int port){
        try{
            s = new Socket(str, port);
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
    
    public void shutDownInput(){
        try{
            s.shutdownInput();
        }catch(IOException e){  
        }
    }
    
    public void println(String str){
            out.println(str);
    }
    
    public String readLine(){ 
        String llegit = null;
        try{
            llegit = in.readLine();
        }catch(IOException e){
            System.err.println("Couldn't get I/O for the connection");
            System.exit(1);
        }
        return llegit;
    }
}