package plugins;

import common.*;
import server.*;

public class Name implements Plugin {

	public boolean isValid(Message mail) {
		return mail.value.startsWith("/name ");
	}

	public void process(Message mail, ServerDispatcher serverDispatcher) {
			mail.value = mail.value.substring(0, 6);
		
	}

}
