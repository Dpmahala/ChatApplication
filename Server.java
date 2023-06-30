import java.io.*;
import java.net.*;

// import all GUI contents
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

     // Declare components 
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Robot",Font.PLAIN,20);

    public Server()
    {
        try{
        server = new ServerSocket(8888);
        System.out.println("Server is ready to accept connection");
        System.out.println("Waiting....");
        socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handelEvents();
            startReading();
           // startWriting();
        } catch(Exception e){
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
        this.setTitle("Server Massager");
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
         this.add(JScrollPane,BorderLayout.CENTER);
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
                    System.out.println("Client terminated the chat.");
                    socket.close();
                    break;
                }
                messageArea.append("Client : "+msg+"\n");
            }
            }catch(Exception e){
                System.out.println("Connection is Closed.");
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

        };

        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is Server...going to start server");
        new Server();
    }
}