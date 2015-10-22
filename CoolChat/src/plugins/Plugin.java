package plugins;

import common.Message;
import server.*;

public interface Plugin {

	
	public boolean isValid(Message mail);
	
	public void process(Message mail, ServerDispatcher serverDispatcher);
}
