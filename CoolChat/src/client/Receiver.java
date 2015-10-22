package client;

import java.io.*;
import common.*;

public class Receiver extends Thread{

	private ObjectInputStream mIn = null;
	
	public Receiver(ObjectInputStream aIn) {
		mIn = aIn;
	}
	public void run(){
		 try {
	           // Read messages from the server and print them
	            Message mail;
	           while ((mail = (Message) mIn.readObject()) != null) {
	        	   if(mail.status == Status.SAY){
	        		   String message = mail.name + ": " + mail.value;
	        		   System.out.println(message);
	        	   }
	        	   else if(mail.status == Status.WARN){
	        		   String message = mail.name + ": " + mail.value;
	        		   System.err.println(message);
	        	   }
	        	   else if(mail.status == Status.SERVER){
	        		   System.err.print(mail.name+": ");
	        		   System.out.println(mail.value);
	        	   }
	        	   else
	        		   System.out.println(mail.value);
	        		   
	           }
	        } catch (IOException | ClassNotFoundException ioe) {
	           System.err.println("Connection to server broken.");
	           ioe.printStackTrace();
	        }
	}
}
