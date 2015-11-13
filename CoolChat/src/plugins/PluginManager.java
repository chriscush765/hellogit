package plugins;
import java.util.*;

import server.*;

public class PluginManager{

	protected ServerDispatcher mServerDispatcher;

	protected Vector<Plugin> PluginList = new Vector<Plugin>();

	public PluginManager(ServerDispatcher aServerDispatcher) {
		mServerDispatcher = aServerDispatcher;

		// add new plugins here
		PluginList.add(new Name());
		
		//Say should always be at the bottom as a catchall
		PluginList.add(new Say());
		
	}

	public void sendMessageToPlugins(Message mail) {
		for(Plugin plugin : PluginList){
			if(plugin.isValid(mail)){
				plugin.process(mail, mServerDispatcher);
				break; //only use one plugin so we get priority
			}
		}	
	}



}
