
package de.hsos.vs.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NewsProxyImpl extends UnicastRemoteObject implements NewsProxy {
    ClientProxy clientProxy;
    NewsServerImpl newsServer;
    String username;
    
    NewsProxyImpl( ClientProxy clientProxy, NewsServerImpl newsServer, String username ) throws RemoteException {
        this.clientProxy = clientProxy;
        this.newsServer = newsServer;
        this.username = username;
    }
    
    public ClientProxy get(){
        return this.clientProxy;
    }
    
    @Override
    public void sendNews (String newstext) throws RemoteException {
        this.newsServer.sendNews( this.username, newstext );
    }
}
