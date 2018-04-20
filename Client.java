import java.io.*;

public class Client {
    public static void main(String[] args) {
     
        try {
            String nick = args[2];
            MySocket s = new MySocket(args[0], Integer.parseInt(args[1]));
            s.println(nick);
            
            // output thread
            new Thread() {
                @Override
                public void run() {
                    String msg;
                        while ((msg = s.readLine()) != null) {
                            // write msg to console
                            System.out.println(msg);
                        }
                        s.close();
                }                
            }.start();
            
            // input thread
            new Thread() {
                @Override
                public void run() {
                    String linia = null;       
                    BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
                    try{
                        while ((linia = kbd.readLine()) != null) {
                            // write line to socket
                            s.println(linia);
                        }
                        // close socket for output (CTRL + D) el tancament el controla el thread input
                        s.shutDownInput();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }                
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}