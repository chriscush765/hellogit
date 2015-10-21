package client;

import java.io.*;
import java.net.*;
 
public class ChatClient
{
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 2002;
    
    public static final String NAME = null;
 
    public static void main(String[] args)
    {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
           // Connect to Nakov Chat Server
           Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
           out = new ObjectOutputStream(socket.getOutputStream());
           in = new ObjectInputStream(socket.getInputStream());
           
           System.out.println("Connected to server " +
              SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException ioe) {
           System.err.println("Can not establish connection to " +
               SERVER_HOSTNAME + ":" + SERVER_PORT);
           ioe.printStackTrace();
           System.exit(-1);
        }
 
        // Create and start Sender thread
        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();
 
        try {
           // Read messages from the server and print them
            Message mail;
           while ((mail = (Message) in.readObject()) != null) {
//        	   if(mail.status == Status.SAY){
        		   String message = mail.name + ": " + mail.value;
        		   System.out.println(message);
//        	   }
        		   
           }
        } catch (IOException | ClassNotFoundException ioe) {
           System.err.println("Connection to server broken.");
           ioe.printStackTrace();
        }
 
    }
}
 