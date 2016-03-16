package client;

import java.io.*;

import javax.swing.JTextArea;


public class Receiver extends Thread{

	private BufferedReader mIn = null;
	private JTextArea mTextArea;
	public Receiver(JTextArea aTextArea, BufferedReader aIn) {
		mIn = aIn;
		mTextArea = aTextArea;
	}
	public void run(){
		 try {
	           // Read messages from the server and print them
	            String mail;
	           while ((mail = mIn.readLine()) != null) {
	        	   mTextArea.append(mail.trim()+"\n");
					if(mTextArea.getLineCount()>256) mTextArea.setText(mTextArea.getText().substring(mTextArea.getText().length()/2));
	           }
	        } catch (IOException ioe) {
	           System.err.println("Connection to server broken.");
	           ioe.printStackTrace();
	        }
	}
}
