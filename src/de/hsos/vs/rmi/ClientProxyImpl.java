
package de.hsos.vs.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy {
    NewsClient newsClient;
    
    ClientProxyImpl( NewsClient newsClient ) throws RemoteException {
        this.newsClient = newsClient;
    }
    
    @Override
    public void receiveNews (String username, String newstext) throws RemoteException {
        this.newsClient.receiveNews( username, newstext );
    }
}
