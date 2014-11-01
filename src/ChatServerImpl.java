/*
 * Implementation of TimeServer.
 */

import java.net.InetAddress;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	private List users = new ArrayList<>();

	public ChatServerImpl() throws RemoteException {
		// Default ctor overloaded to catch RemoteException
		System.out.println("ChatServer started...");
	}

	public long getTime() throws RemoteException {

		return System.currentTimeMillis();
	}

	@Override
	public ChatProxy subscribeUser(String nickname, ClientProxy handle)
			throws RemoteException {

		System.out.println(nickname + " hat sich angemeldet!");
		ChatProxy s = new ChatProxyImpl(this, nickname, handle);
		users.add(s);
		return s;
	}

	public boolean unsubscribeUser(ChatProxy handle) throws RemoteException {
		users.remove(handle);
		return true;
	}
	

	public void postMessage(String message, ChatProxyImpl s) throws RemoteException {
		ChatProxyImpl tmp;
		for (int i = 0; i < users.size(); i++) {
			tmp = (ChatProxyImpl) users.get(i);
			try {
				tmp.getClientHandle().receiveMessage(s.getNickname(), message);
			} catch (RemoteException ex) {
				System.out.println("Connection lost: "
						+ s.getNickname());
				 unsubscribeUser(tmp);
				 System.out.println(s.getNickname() + "hat den Chat verlassen.");
				i--; // Da nun alle Clients in Liste einen Platz nach unten
						// rutschen ...
			}
		}
	}

	public static void main(String[] args) {
		// Man kann entweder eine lokale Registry erzeugen ...
		// LocateRegistry.createRegistry (Registry.REGISTRY_PORT);
		// ... oder verwendet die Standard-RMI Registry auf dem lokalen Host
		// unter Port 1099. In diesem Fall müssen aber Policies
		// konfiguriert
		// werden. Siehe timer.policy in diesem Verzeichnis fuerr Hinweise.
		try {
			Registry reg = LocateRegistry.getRegistry ();
			Naming.rebind("ChatServer", new ChatServerImpl());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
