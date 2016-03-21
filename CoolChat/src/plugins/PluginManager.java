package plugins;
import java.util.*;

import server.*;

public class PluginManager{

	protected ServerDispatcher mServerDispatcher;

	protected Vector<Plugin> pluginList = new Vector<Plugin>();

	public PluginManager(ServerDispatcher aServerDispatcher) {
		mServerDispatcher = aServerDispatcher;

		// add new plugins here
		pluginList.add(new Name());
		
		pluginList.add(new Kick());
		//Say should always be at the bottom as a catchall
		pluginList.add(new Say());
		
	}

	public void sendMessageToPlugins(Message mail) {
		for(Plugin plugin : pluginList){
			if(plugin.isValid(mail)){
				System.out.println("sent to a plugin" + plugin.getName());
				plugin.process(mail, mServerDispatcher);
				break; //only use one plugin so we get priority
			}
		}	
	}



}
