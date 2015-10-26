package plugins;

import server.ServerDispatcher;

import common.Message;

public class Say implements Plugin {

	public boolean isValid(Message mail) {
		return true;
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
		
	}

}
