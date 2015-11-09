package plugins;
import java.util.*;

import server.*;

public class PluginManager extends Thread{

	protected ServerDispatcher mServerDispatcher;

	protected Vector<Plugin> PluginList = new Vector<Plugin>();

	private Vector<Message> MailQueue = new Vector<Message>();

	public PluginManager(ServerDispatcher aServerDispatcher) {
		mServerDispatcher = aServerDispatcher;

		// add new plugins here
		PluginList.add(new Name());
	}
	public synchronized void addMessageToQueue(Message aMail){
		MailQueue.add(aMail);
		notify();
	}

	private synchronized void sendMessageToPlugins(Message mail) throws InstantiationException, IllegalAccessException{
		for(Plugin plugin : PluginList){
			if(plugin.isValid(mail) || plugin.catchAll){
				Plugin pluginToAdd = plugin.getClass().newInstance();
				mail.sender.addPlugin(pluginToAdd);
			}
		}	
	}
	private synchronized Message getNextMessageFromQueue() throws InterruptedException{
		if(MailQueue.size() == 0)
			wait();
		Message mail = MailQueue.get(0);
		MailQueue.removeElementAt(0);
		return mail;
	}

	public void run(){
		try {
			while (true) {
				Message mail = getNextMessageFromQueue();
				sendMessageToPlugins(mail);
			}
		} catch (InstantiationException | IllegalAccessException | InterruptedException ie) {
			// Thread interrupted. Stop its execution
		}
	}

}
