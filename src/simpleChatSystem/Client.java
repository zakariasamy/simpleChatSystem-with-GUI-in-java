package simpleChatSystem;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import java.util.logging.Level;
import java.util.logging.Logger;
  
public class Client  
{ 
    public Scanner scn = new Scanner(System.in);
    public volatile boolean  isReadyToSend;
    public volatile String msg = "";
    public volatile Form currentFrame = null;
    public volatile boolean  firstReceive= true;
    public static String name;


    private Socket s = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    InetAddress ip;
    Thread sendMessage;
    Thread readMessage;
    public Client(Form f){
        this.currentFrame = f;
            // sendMessage thread 
        sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
                    if(isReadyToSend == true){
                        System.out.println("inside thread");
                    try {
                        // write on the output stream 
                        if(!msg.equals(""))
                            dos.writeUTF(msg); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    }
                    currentFrame.message.setText("");
                    isReadyToSend=false;
                    }
                } 
            } 
        }); 
          
        // readMessage thread 
        readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF();
                        if(!firstReceive){
                          msg = currentFrame.allMessages.getText() + '\n' + msg;
                        }
                        else{
                            firstReceive = false;
                        }
                        currentFrame.allMessages.setText(msg);
                        //f.setAllMessages(msg);
                        System.out.println(msg); 
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
        
        System.out.println("End constructor");
    }
  
    public void establish(){
        try {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");
            // establish the connection
            this.s = new Socket(ip, 1234);

            // obtaining input and out streams
            
            this.dis = new DataInputStream(this.s.getInputStream());
            this.dos = new DataOutputStream(this.s.getOutputStream());

            this.sendMessage.start(); 
            System.out.println("before establish ======");

            this.readMessage.start();
            System.out.println("after establish");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        System.out.println("in main of client");

  
    } 
} 