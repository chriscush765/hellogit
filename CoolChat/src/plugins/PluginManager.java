package plugins;
import java.util.*;

import common.Message;

import server.*;

public class PluginManager {

	protected ServerDispatcher mServerDispatcher;
	
	protected Vector<Plugin> PluginList = new Vector<Plugin>();
	
	public PluginManager(ServerDispatcher aServerDispatcher) {
		mServerDispatcher = aServerDispatcher;
		
		// add new plugins here
		PluginList.add(new Name());
	}
	
	public void sendMessageToPlugins(Message mail){
		for(Plugin plugin : PluginList){
			if(plugin.isValid(mail))
				plugin.process(mail, mServerDispatcher);
		}	
	}

}
