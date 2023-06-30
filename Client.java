import java.io.*;
import java.net.*;

// import all GUI contents
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame{
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare components 
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Robot",Font.PLAIN,20);

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 8888);
            System.out.println("Connection Done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handelEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
   
    private void handelEvents() {

        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e){

            }
            @Override
            public void keyPressed(KeyEvent e){

            }
            @Override
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode()==10){
                   String conteToSend = messageInput.getText();
                   messageArea.append("Me :"+conteToSend+"\n");
                   out.println(conteToSend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();

                   
                }

            }
        });
    }
     // Here i'am create GUI of ChatApp
    private void createGUI(){
        this.setTitle("Client Massager");
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));
        messageArea.setEditable(false);

        // set of frame layout 
        this.setLayout(new BorderLayout());

        //adding the components to frame
         this.add(heading,BorderLayout.NORTH);
         JScrollPane JScrollPane = new JScrollPane(messageArea);
         this.add(JScrollPane, BorderLayout.CENTER);
         this.add(messageInput,BorderLayout.SOUTH);

         this.setVisible(true);

    }
   
    public void startReading(){
        Runnable r1 =()->{

            System.out.println("Reader started.....");
            try{
            while(true){
              
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat.");
                    JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                messageArea.append("Server : "+msg+"\n");
            }
            }
            catch(Exception e){
                System.out.println("Connection is closed.");
            }
        };
        new Thread(r1).start();

    }

    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("Writer started...");
            try{
            while(!socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }

            }catch(Exception e){
                System.out.println("Connection is Closed.");

            }
            System.out.println("Connection is Closed.");

        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Client...");
        new Client();
    }
}