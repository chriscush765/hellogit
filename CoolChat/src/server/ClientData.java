package server;

import java.util.*;

public class ClientData {

	private Vector<Vector> data = new Vector<Vector>();
	
	public synchronized void add(String name, Object value){
		if(data[0].indexOf(name) < 0)
			throw new IllegalArgumentException("Name " +name+" is already occupied");
		else
			data[0].add(name);
			data[1].add(value);
	}
	
	public synchronized Object get(String name){
		int index = data[0].indexOf(name);
		if(index < 0)
			return null;
		return data[1].get(index);
	}
}
