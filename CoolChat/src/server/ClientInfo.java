package server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import plugins.Plugin;

public class ClientInfo
{
	public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
    
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public synchronized void add(String aName, Object aValue){
		data.put(aName, aValue);
	}
	
	public synchronized Object get(String aName){
		return data.get(aName);
	}
	
    
}