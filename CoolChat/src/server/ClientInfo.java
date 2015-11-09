package server;

import java.net.Socket;
import java.util.Vector;

import plugins.Plugin;

public class ClientInfo
{
	public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
    
	private Vector mName = new Vector();
	private Vector mValue = new Vector();
	private Vector<Plugin> mPlugins = new Vector<Plugin>();
	
	public synchronized void add(String aName, Object aValue){
		if(mName.contains(aName))
			throw new IllegalArgumentException();
		mName.addElement(aName);
		mValue.addElement(aValue);
	}
	
	public synchronized Object get(String name){
		int index = mName.indexOf(name);
		if(index < 0)
			return null;
		return mValue.get(index);
	}
	
	public synchronized void addPlugin(Plugin aPlugin){
		mPlugins.add(aPlugin);
	}
	
	public synchronized void notifyPlugins(){
		for(Plugin plugin : mPlugins)
			plugin.notify();
	}
    
}