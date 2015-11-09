package plugins;

import server.Message;
import server.ServerDispatcher;

public interface Plugin {

	boolean catchAll = false;

	public boolean isValid(Message mail);

	public void process(Message mail, ServerDispatcher serverDispatcher);
}
