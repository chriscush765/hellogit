package plugins;

import server.ClientInfo;
import server.Message;
import server.ServerDispatcher;

public class Kick implements Plugin {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Kick";
	}

	@Override
	public boolean isValid(Message mail) {
		// TODO Auto-generated method stub
		return mail.value.startsWith("/kick");
	}

	@Override
	public void process(Message mail, ServerDispatcher serverDispatcher) {
		String[] parts = mail.value.split(" ");
		if (parts.length == 0) {
			serverDispatcher.sendMessageToClient("You did not enter a user to kick", mail.sender);
			return;
		}
		ClientInfo clientToKick = serverDispatcher.searchForClient(parts[1]);
		
		if(clientToKick == null){
			serverDispatcher.sendMessageToClient("This user doesn't exist", mail.sender);
			return;
		}
		String name = parts[1];
		
		serverDispatcher.kickClient(clientToKick, (String) mail.sender.get("name"), "You have been kek'd");

		

	}

}
