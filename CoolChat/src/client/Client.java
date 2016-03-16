package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Client {
	private final JFrame frame;
	private final String host;
	private final int port;
	private final JTextArea textArea;
	private final JTextField textField;

	private Receiver in;
	private PrintWriter out;

	public Client(final int port) {
		this("localhost", port);
	}
	public Client(final String host, final int port) {
		frame=new JFrame("Fern Chat");
		this.host=host;
		this.port=port;
		textArea=new JTextArea();
		textField=new JTextField();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		final GridBagLayout layout=new GridBagLayout();
		layout.columnWidths=new int[] {445/16*15, 445/16};
		layout.rowHeights=new int[] {720/12*11, 720/12};
		final JPanel panel=new JPanel(layout);
		final GridBagConstraints gbc=new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

		textArea.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Color.BLACK));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		((DefaultCaret)(textArea.getCaret())).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setWrapStyleWord(true);

		textField.addActionListener((ae)->{
			out.write(textField.getText() + "\n");
			out.flush();
			textField.setText("");
		});
		textField.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Color.BLACK));

		final JScrollPane scroll=new JScrollPane(textArea);
		scroll.setBorder(null);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		final JButton smileyButton=new JButton(":)");
		smileyButton.addActionListener((ae)->System.out.println("Smilies!"));
		smileyButton.setBorder(null);

		panel.add(scroll, gbc);
		gbc.gridwidth=1;
		gbc.gridy=1;
		panel.add(textField, gbc);
		gbc.gridx=1;
		panel.add(smileyButton, gbc);

		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void start() {
		System.out.println("Client started @ "+host+":"+port+"...");
		try {
			final Socket sock=new Socket(host, port);
			in = new Receiver(textArea, new BufferedReader(new InputStreamReader(sock.getInputStream())));
			out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		in.start();

		//		new Thread(()->{
		//			try(final Socket sock=new Socket(host, port)) {
		//				String str;
		//				while((str=in.readLine())!=null) {
		//					if(str.equals(Status.REQUEST_NAME.toString()))
		//						out.println(JOptionPane.showInputDialog(frame, "Choose a handle:", "Name Selection", JOptionPane.PLAIN_MESSAGE));
		//					else if(str.equals(Status.NAME_ACCEPTED.toString()))
		//						textField.setEditable(true);
		//					else {
		//						textArea.append(str.trim()+"\n");
		//						if(textArea.getLineCount()>256) textArea.setText(textArea.getText().substring(textArea.getText().length()/2));
		//					}
		//				}
		//			} catch(IOException ioe) {
		//				ioe.printStackTrace();
		//			}
		//		}).start();
	}

	public static void main(String[] args) {
		new ServerQueryScreen().show();
	}

	private static final class ServerQueryScreen {
		private final BufferedImage background;
		private final JFrame frame;
		private final JButton goButton;
		private final JTextField hostField, portField;
		private final JPanel panel;

		public ServerQueryScreen() {
			BufferedImage temp=null;
			try {
				temp=ImageIO.read(new File("src/client/fernBar.png"));
			} catch(IOException ioe) {
				System.err.println("|ERROR| Could not find background image...");
			}
			background=temp;

			hostField=new JTextField("host");
			hostField.setBorder(null);

			frame=new JFrame("Fern Chat");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			goButton=new JButton("Connect");
			goButton.setBorder(null);

			final GridBagLayout layout=new GridBagLayout();
			layout.rowHeights=new int[] {40, 40};
			layout.columnWidths=new int[] {160, 160};
			panel=new JPanel(layout);			

			portField=new JTextField("2002");
			portField.setBorder(null);
			portField.setInputVerifier(new InputVerifier() {
				@Override
				public boolean verify(JComponent input) {
					final JTextField tf=(JTextField)input;
					return tf.getText().matches("[0-9]{4,5}");
				}
			});

			final GridBagConstraints gbc=new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 4, 8, 4), 0, 0);
			panel.add(hostField, gbc);
			gbc.gridy=1;
			panel.add(portField, gbc);
			gbc.gridx=1;
			goButton.addActionListener((ae)->{
				final String str=hostField.getText();
				final int num=Integer.parseUnsignedInt(portField.getText());
				kill();				
				new Client(str, num).start();
			});
			panel.add(goButton, gbc);
			gbc.gridy=0;
			panel.add(new ImagePanel(background), gbc);

			frame.setContentPane(panel);
			frame.pack();
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
		}

		public void kill() {
			frame.setVisible(false);
			frame.dispose();
		}
		public void show() {
			frame.setVisible(true);
		}

		@SuppressWarnings("serial")
		private final class ImagePanel extends JPanel {
			private final BufferedImage img;

			public ImagePanel(BufferedImage img) {
				this.img=img;
				setBorder(null);
				setOpaque(true);
				repaint();
			}

			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, null);
			}
		}
	}
}
