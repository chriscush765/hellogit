package server;

import java.net.*;
import java.util.*;
 
public class ServerDispatcher extends Thread
{
    private Vector mMessageQueue = new Vector();
    private Vector mClients = new Vector();
 
    /**
     * Adds given client to the server's client list.
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
        if (clientIndex != -1)
           mClients.removeElementAt(clientIndex);
    }
 
    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void dispatchMessage(ClientInfo aClientInfo, Message aMail)
    {
        Socket socket = aClientInfo.mSocket;
        Message mMail = new Message();
        mMail.sender = socket.getInetAddress().getHostAddress();
        mMail.value = aMail.value;
        mMail.status = Status.SAY;
        mMessageQueue.add(mMail);
        notify();
    }
 
    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized Message getNextMessageFromQueue()
    throws InterruptedException
    {
        while (mMessageQueue.size()==0)
           wait();
        Message mail = (Message) mMessageQueue.get(0);
        mMessageQueue.removeElementAt(0);
        return mail;
    }
 
    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    private synchronized void sendMessageToAllClients(Message aMail)
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
               Message mail = getNextMessageFromQueue();
               sendMessageToAllClients(mail);
           }
        } catch (InterruptedException ie) {
           // Thread interrupted. Stop its execution
        }
    }
 
}