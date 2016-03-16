package server;

import java.util.*;
import plugins.*;
 
public class ServerDispatcher extends Thread
{
    private Vector<Message> mOutgoingQueue = new Vector<Message>();
    private PluginManager mPluginManager = new PluginManager(this);;
    public Vector<ClientInfo> mClients = new Vector<ClientInfo>();
    public boolean anonMode = false;
    protected int key;
    
    public ServerDispatcher() {
		Random rand = new Random();
		key = rand.nextInt((9999 - 1111) + 1) + 1111;
		System.out.println("Admin key is "+key);
	}
 
    /**
     * Adds given client to the server's client list.
     */
    public synchronized void addClient(ClientInfo aClientInfo)
    {
        mClients.add(aClientInfo);
    }
    
    /**
     * Sends a welcome message to a client
     */
    public synchronized void sendWelcomeMessage(ClientInfo aClientInfo) {
    	sendMessageToClient("Welcome to the server!", aClientInfo);
    	sendMessageToClient("Remember to set your name with /name", aClientInfo);
    }
    
    /**
     * Sends a message to a given client
     */
    public synchronized void sendMessageToClient(String aMessage, ClientInfo aClientInfo) {
    	aClientInfo.mClientSender.sendMessage(new Message("(Server)" + aMessage));
    }
 
    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void deleteClient(ClientInfo aClientInfo)
    {
    	
        int clientIndex = mClients.indexOf(aClientInfo);
        if (clientIndex != -1){
        	aClientInfo.mClientListener.interrupt();
        	aClientInfo.mClientSender.interrupt();
        	mClients.removeElementAt(clientIndex);
        }
           
    }
    
    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void kickClient(ClientInfo aClientInfo, String by ,String reason)
    {
    	Message mail = new Message("Server: You have been kicked by "+by+": "+reason);
    	aClientInfo.mClientSender.sendMessage(mail);
        deleteClient(aClientInfo);
    }
 
    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void dispatchMessage(ClientInfo aClientInfo, Message mail)
    {
        mail.value = (String) aClientInfo.get("name") + mail.value;
        mOutgoingQueue.add(mail);
        notify();
    }
 
    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized Message getNextMessageFromOutgoingQueue()
    throws InterruptedException
    {
        while (mOutgoingQueue.size()==0)
           wait();
        Message mail = (Message) mOutgoingQueue.get(0);
        mOutgoingQueue.removeElementAt(0);
        return mail;
    }
    
    protected synchronized void processIncomingMessage(Message aMail){
    	mPluginManager.sendMessageToPlugins(aMail);
    }
    
 
    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    public synchronized void sendMessageToAllClients(Message aMail)
    {
        for (int i=0; i<mClients.size(); i++) {
           ClientInfo clientInfo = (ClientInfo) mClients.get(i);
           clientInfo.mClientSender.sendMessage(aMail);
        }
    }
 
    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */
    public void run()
    {
        try {
           while (true) {
               Message mail = getNextMessageFromOutgoingQueue();
               sendMessageToAllClients(mail);
           }
        } catch (InterruptedException ie) {
           // Thread interrupted. Stop its execution
        }
    }
}