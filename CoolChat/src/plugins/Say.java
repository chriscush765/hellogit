package plugins;

import server.ServerDispatcher;

import common.Message;

public class Say extends Plugin {

	public static boolean isValid(Message mail) {
		return true;
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
		
	}

}
