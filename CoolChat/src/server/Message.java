package server;

import java.io.Serializable;

public class Message implements Serializable{
	
	public Status status;
	public Object sender;
	public String name;
	public String value;
	
	public Message(String name, String value, Status status){
		this.name = name;
		this.value = value;
		this.status = status;
	}
	
	public Message(String value, Status status){
		this.value = value;
		this.status = status;
	}

	public Message() {
		// TODO Auto-generated constructor stub
	}

	
}
