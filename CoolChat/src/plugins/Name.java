package plugins;

import server.*;

public class Name implements Plugin {

	public boolean isValid(Message mail) {
		return mail.value.startsWith("/name ");
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
		String desiredName = mail.value.substring(6, mail.value.length()); //remove /name
		if(nameIsOk(desiredName)){
			String oldName = (String) mail.sender.get("name");
			if(serverDispatcher.changeClientName(mail.sender, desiredName)){
				Message serverMessage = new Message(oldName+ " changed their name to "+desiredName);
				serverDispatcher.sendMessageToAllClients(serverMessage);
			}
			else
				serverDispatcher.sendMessageToClient("That name is already taken!", mail.sender);

		}
		else{
			Message serverMessage = new Message("You can not use that name");
			serverDispatcher.dispatchMessage(mail.sender, serverMessage);
		}

	}

	private boolean nameIsOk(String name){
		String[] blocked = {
				"server",
				"admin"
		};

		for(String banned : blocked){
			if(name.equalsIgnoreCase(banned))
				return false;
		}
		return true;
	}

	public String getName(){
		return "Set Name";
	}

}
