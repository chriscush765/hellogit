package server;

import java.io.*;
import java.net.*;
import common.*;
 
public class ClientListener extends Thread
{
    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private ObjectInputStream mIn;
 
    public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
    throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mIn = new ObjectInputStream(socket.getInputStream());
    }
 
    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run()
    {
        try {
           while (!isInterrupted()) {
               Message mail = (Message) mIn.readObject();
               mail.sender = mClientInfo;
               mServerDispatcher.processIncomingMessage(mail);
           }
        } catch (IOException | ClassNotFoundException ioex) {
           // Problem reading from socket (communication is broken)
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }

 
}