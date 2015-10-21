package common;

import java.io.Serializable;

public class Message implements Serializable{

	public Status status;
	public String sender;
	public String name;
	public String value;
}
