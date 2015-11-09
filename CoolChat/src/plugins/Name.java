package plugins;

import server.*;

public class Name extends Thread implements Plugin {
	
	public boolean isValid(Message mail) {
		return mail.value.startsWith("/name ");
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
			mail.value = mail.value.substring(0, 6);
	}
	

}
