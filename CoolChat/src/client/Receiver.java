package client;

import java.io.*;


public class Receiver extends Thread{

	private BufferedReader mIn = null;
	
	public Receiver(BufferedReader aIn) {
		mIn = aIn;
	}
	public void run(){
		 try {
	           // Read messages from the server and print them
	            String mail;
	           while ((mail = mIn.readLine()) != null) {
	        		   System.out.println(mail);
	           }
	        } catch (IOException ioe) {
	           System.err.println("Connection to server broken.");
	           ioe.printStackTrace();
	        }
	}
}
