package plugins;

import server.Message;
import server.ServerDispatcher;

public class Say implements Plugin {
	
	public boolean isValid(Message mail) {
		return true;
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
		String message;
		if(serverDispatcher.anonMode)
			message = "[Anon] " + mail.value;
		else
			message = "[" + mail.sender.get("name") + "] " + mail.value;
		serverDispatcher.sendMessageToAllClients(new Message(message));
	}
	
	public String getName(){
		return "Say Plugin";
	}

}
