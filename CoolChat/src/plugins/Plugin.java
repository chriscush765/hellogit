package plugins;

import common.Message;
import server.*;

public abstract class Plugin extends Thread{

	
	public static boolean isValid(Message mail){
		return false;
	}
	
	public void process(Message mail, ServerDispatcher serverDispatcher){
		return;
	}
}
