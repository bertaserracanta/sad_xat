
import java.net.*;
import java.io.*;

public class MyServerSocket {
    protected ServerSocket ss;
    
    public MyServerSocket(int port){
        try{
            ss = new ServerSocket(port);
        }catch(IOException e){
            
        }
    }
    
    public MySocket accept(){
        MySocket s = null;
        try{
            s = new MySocket(ss.accept());
        }catch(IOException e){
            System.err.println("Couldn't accept the connection");
            System.exit(1);
        }
        return s;
    }
}