package plugins;

import server.Message;
import server.ServerDispatcher;

public class Say extends Thread implements Plugin {
	
	public boolean isValid(Message mail) {
		return true;
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
		serverDispatcher.sendMessageToAllClients(mail);
	}

}
