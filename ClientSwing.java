
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

public class ClientSwing implements ActionListener {

    private JFrame frame;
    private JPanel outp, inp;
    private JTextField entry;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JScrollPane scrollPane;
    private JTextArea messages;
    private JButton send;
    private MySocket sc;
    private String nick;

    public ClientSwing(MySocket socket, String nck) {
        sc = socket;
        nick = nck;
        sc.println(nick); //l'enviem pq el servidor l'afegeixi a la llista
        
        try{
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                //try {
                    createAndShowGUI();
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}
            }
        });
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //output thread
        new Thread(new ClientOutputThreadSwing(this)).start();
    }

    private void createAndShowGUI() {

        //Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        frame = new JFrame("Usuari: " + nick);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0)); //separacio entre panells
        //frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS)); per canviar el tipus de Layout de la frame
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // Create an output JPanel and add a JTextArea(20, 30) inside a JScrollPane
        outp = new JPanel();
        outp.setLayout(new BoxLayout(outp, BoxLayout.X_AXIS));
        messages = new JTextArea(20, 30); //podem treure aixo i fer un setSize de la frame
        scrollPane = new JScrollPane(messages);
        messages.setEditable(false);
        outp.add(scrollPane, BorderLayout.WEST);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        
        JScrollPane listScrollPane = new JScrollPane(list);

        outp.add(listScrollPane, BorderLayout.EAST);

        /*
         definir proporcions entre widgets: gridbagLayout
        
        mirar MigLayout
        DesignGridLayout
         */
        // Create an input JPanel and add a JTextField(25) and a JButton
        inp = new JPanel();
        inp.setLayout(new BoxLayout(inp, BoxLayout.LINE_AXIS));//per ferlo horitzontal
        entry = new JTextField(25); //podem treure aixo i fer un setSize de la frame
        send = new JButton("Send");

        inp.add(entry);
        inp.add(send);

        // Listen to events from the inputField button.
        entry.addActionListener(this);
        send.addActionListener(this);

        //inp.setMaximumSize(new Dimension(inp.getMaximumSize().width, inp.getMinimumSize().height));//limitem l'altura del panell de input a la seva miniima
        // add panels to main frame
        frame.add(outp, BorderLayout.CENTER);
        frame.add(inp, BorderLayout.PAGE_END);

        //Display the window centered.
        frame.pack();//podem treure aixo i fer un setSize de la frame
        //frame.setSize(400,600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    //getters and setters dels diferents atributs de la gui
    public DefaultListModel<String> getList() {
        return listModel;
    }

    public JTextArea getMessages() {
        return messages;
    }

    public MySocket getSc() {
        return sc;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        //Guardem el text de entry, l'esborrem i l'afegim al textAtrea (missatges) i s'envia pel socket.
        String message = entry.getText();
        entry.setText("");
        messages.append(message + "\n");
        sc.println(message);
        
    }

    public static void main(String[] args) {
        new ClientSwing(new MySocket(args[0], Integer.parseInt(args[1])), args[2]);
    }
}

class ClientOutputThreadSwing implements Runnable {

    private ClientSwing gui;

    public ClientOutputThreadSwing(ClientSwing interficie) {
        gui = interficie;
    }

    public void run() {
        String line;
        try {
            while ((line = gui.getSc().readLine()) != null) {
                if(line.contains("usuari")){
                    if(line.contains("unit")){
                        String nick = line.split(" ")[1];
                        gui.getList().addElement(nick);
                    }else if(line.contains("desconnectat")){
                        String nick = line.split(" ")[1];
                        gui.getList().removeElement(nick);
                    }
                }else if (line.contains("Usuaris")){
                    String usuaris[] = line.split(": ")[1].replace('[', ',').replace(']', ',').replaceAll("\\s","").split(",");
                    for(String user : usuaris){ 
                        gui.getList().addElement(user);
                    } 
                }
                gui.getMessages().append(line + "\n");
            }
            gui.getSc().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}