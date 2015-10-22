package plugins;
import java.util.*;

import server.*;

public class PluginManager {

	protected ServerDispatcher mServerDispatcher;
	
	protected Vector<Plugin> PluginList = new Vector<Plugin>();
	
	public PluginManager(ServerDispatcher aServerDispatcher) {
		mServerDispatcher = aServerDispatcher;
		
		// add new plugins here
		PluginList.add(new Name());
	}
	
	

}
