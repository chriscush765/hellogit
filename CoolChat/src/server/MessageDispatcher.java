package server;

import plugins.Plugin;
import plugins.PluginManager;

public class MessageDispatcher extends Thread{
	private static Message mail;
	private static ServerDispatcher mServerDispatcher;
	private static Plugin plugin;
	
	public MessageDispatcher(Plugin _plugin, Message _mail, ServerDispatcher _mServerDispatcher ){
		super();
		plugin = _plugin;
		mail = _mail;
		mServerDispatcher = _mServerDispatcher;
	}
	
	@Override
	public void run(){
		if(plugin.isValid(mail)){
			System.out.println("sent to a plugin");
			plugin.process(mail, mServerDispatcher);
			PluginManager.case1 = true;
		}
	}
}
