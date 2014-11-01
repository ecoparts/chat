//repräsentiert den Client im Server

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy {
	
	private ChatClient client;
	
	public ClientProxyImpl(ChatClient client) throws RemoteException {
		this.client = client;
	}
	
	public ClientProxyImpl() throws RemoteException {
	}

	@Override
	public void receiveMessage(String username, String message)
			throws RemoteException {
		client.receiveMessage(username,message);
		
	}

}
