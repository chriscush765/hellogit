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
               processMail(mail);
           }
        } catch (IOException | ClassNotFoundException ioex) {
           // Problem reading from socket (communication is broken)
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
    
    public synchronized void processMail(Message mail){
		if (mail == null)
			return;
		
		if (mail.status == Status.LOGIN) {
			String oldName = mClientInfo.name;
			if(oldName == null)
				oldName = "(New User)";
			mClientInfo.name = mail.value;
			mServerDispatcher.sendMessageToAllClients(new Message("Server", oldName + " changed their name to "+mClientInfo.name, Status.SAY));
		}
		else if(mClientInfo.name == null && !mServerDispatcher.anonMode){
			mClientInfo.mClientSender.sendMessage(new Message("Server","You may not be anonymous. Type \"/name desiredname\"", Status.WARN));
			//mServerDispatcher.kickClient(mClientInfo, "Server", "Your name can not be NULL");
		}
		else if(mail.status == Status.SAY){
			mServerDispatcher.dispatchMessage(mClientInfo, mail);
		}

	}
 
}