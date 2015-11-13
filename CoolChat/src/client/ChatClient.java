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
        BufferedReader in = null;
        PrintWriter out = null;
        try {
           // Connect to Nakov Chat Server
           Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
           out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
           in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           
           System.out.println("Connected to server " +
              SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException ioe) {
           System.err.println("Can not establish connection to " +
               SERVER_HOSTNAME + ":" + SERVER_PORT);
           ioe.printStackTrace();
           System.exit(-1);
        }
 
        // Create Sender/Receiver thread
        Sender sender = new Sender(out);
        Receiver receiver = new Receiver(in);
        
        
        
        sender.start();
        receiver.start();
 
       
 
    }
}
 