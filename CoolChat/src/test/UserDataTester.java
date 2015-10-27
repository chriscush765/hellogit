package test;

import server.*;

public class UserDataTester {

	public static void main(String[] args) {
		ClientData data = new ClientData();
		
		data.add("a","1");
		data.add("b","2");
		System.out.println(data.get("b"));
	}

}
