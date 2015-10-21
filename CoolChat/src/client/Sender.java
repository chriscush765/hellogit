package client;

import java.io.*;
import common.*;

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
				Message mail = new Message();
				mail.value = message;
				mOut.writeObject(mail);
				mOut.flush();
	            System.out.println("sent message " + mail.value);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}