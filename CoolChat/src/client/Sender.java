package client;

import java.io.*;

class Sender extends Thread {
	private OutputStreamWriter mOut;
	private String mClient;

	public Sender(OutputStreamWriter aOut) {
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
				sendMail(message);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	public synchronized void sendMail(String aMail){
		try {
			mOut.write(aMail);
			mOut.flush();
		} catch (IOException e) {
			System.err.println("could not send message");
			e.printStackTrace();
		}
	}
}