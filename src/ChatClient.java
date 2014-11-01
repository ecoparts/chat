/*
 * Client for TimeServer.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame {

	private JTextArea output;
	private JTextField input;
	private ClientProxy handle;
	private ChatProxy cp;
	private String nickname;

	public ChatClient(String nickname) throws Exception {
		ChatServer server = (ChatServer) Naming.lookup("ChatServer");
		handle = new ClientProxyImpl(this);
		cp = server.subscribeUser(nickname, handle);
		
		
		//GUI
		setTitle(nickname);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		output = new JTextArea();
		output.setEditable(false);
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getViewport().setView(output);
		getContentPane().add(scroller, BorderLayout.CENTER);
		input = new JTextField();
		getContentPane().add(input, BorderLayout.NORTH);
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendMessage(input.getText());
					input.setText("");
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});

		JButton close = new JButton("Schließen");
		getContentPane().add(close, BorderLayout.SOUTH);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		setSize(400, 300);
	}

	public void receiveMessage(String nickname, String message) {
		output.append(nickname + ": " + message + "\n");
		output.setCaretPosition(output.getText().length() - 1);
	}

	public void sendMessage(String message) throws RemoteException {
		cp.sendMessage(message);
	}

	protected void close() {
		System.exit(0);
	}

	public static void main(String[] args) {
		try {
			String name = JOptionPane.showInputDialog(null,
					"Eingabe des Nickname");
			if (name != null && name.trim().length() > 0) {
				ChatClient client = new ChatClient(name);
				client.setVisible(true);
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Bitte geben Sie einen Nickname mit mehr als 1 Zeichen ein.");
				System.exit(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
