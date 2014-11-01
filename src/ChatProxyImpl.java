// repräsentiert den User

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ChatProxyImpl extends UnicastRemoteObject implements ChatProxy {
  private ChatServerImpl server;
  private String nickname;
  private ClientProxy handle;

  public ChatProxyImpl() throws RemoteException {
  }

  public ChatProxyImpl(ChatServerImpl server, String nickname, ClientProxy handle) throws RemoteException {
    this.server = server;
    this.nickname = nickname;
    this.handle = handle;
  }

  public void sendMessage(String message) throws RemoteException {
    server.postMessage(message, this);
  }

  public ClientProxy getClientHandle() {
    return handle;
  }

  public String getNickname() {
    return nickname;
  }

}