package client;

import java.io.*;

class Sender extends Thread {
	private PrintWriter mOut;
	private String mClient;

	public Sender(PrintWriter aOut) {
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
				System.out.println("message");
				sendMail(message);
				System.out.println("sent");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	public synchronized void sendMail(String aMail){
		mOut.write(aMail);
		mOut.flush();
	}
}