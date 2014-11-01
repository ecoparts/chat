/*
 * Java interface of Time server.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
	public long getTime() throws RemoteException;

	public ChatProxy subscribeUser(String nickname,ClientProxy handle) throws RemoteException;

	public boolean unsubscribeUser(ClientProxy handle) throws RemoteException;
}
