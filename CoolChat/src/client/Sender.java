package client;

import java.io.*;

import server.Message;
import common.Status;

class Sender extends Thread {
	private ObjectOutputStream mOut;
	private String mClient;

	public Sender(ObjectOutputStream aOut) {
		mOut = aOut;
	}

	/**
	 * Until interrupted reads messages from the standard input (keyboard) and
	 * sends them to the chat server through the socket.
	 */
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!isInterrupted()) {
				String message = in.readLine();
				sendMail(new Message(message, Status.CHAT));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	public synchronized void sendMail(Message aMail){
		try {
			mOut.writeObject(aMail);
			mOut.flush();
		} catch (IOException e) {
			System.err.println("could not send message");
			e.printStackTrace();
		}
	}
}