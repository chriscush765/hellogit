package server;

import java.io.IOException;
import java.net.*;
import java.util.*;

import common.*;
import plugins.*;
 
public class ServerDispatcher extends Thread
{
    private Vector mOutgoingQueue = new Vector();
    private PluginManager mPluginManager = new PluginManager(this);
    protected Vector<ClientInfo> mClients = new Vector<ClientInfo>();
    public boolean anonMode = false;
    protected int key;
    
    public ServerDispatcher() {
		Random rand = new Random();
		key = rand.nextInt((9999 - 1111) + 1) + 1111;
		System.out.println("Admin key is "+key);
	}
 
    /**
     * Adds given client to the server's5 client list.
     */
    public synchronized void addClient(ClientInfo aClientInfo)
    {
        mClients.add(aClientInfo);
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
    public synchronized void kickClient(ClientInfo aClientInfo, String admin ,String reason)
    {
    	Message mail = new Message();
    	mail.status = Status.KICK;
    	mail.sender = "Server";
    	mail.value = "You have been kicked by "+admin+": "+reason;
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
        Socket socket = aClientInfo.mSocket;
        mail.sender = socket.getInetAddress().getHostAddress();
        mail.name = aClientInfo.name;
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
    	
    }
    
 
    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    protected synchronized void sendMessageToAllClients(Message aMail)
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

	public boolean nameIsUnique(String value) {
		int occurances = 0;
		for(ClientInfo clientInfo : mClients)
			if(clientInfo.name != null && clientInfo.name.equals(value))
				occurances++;
		return occurances == 0;
	}
 
}