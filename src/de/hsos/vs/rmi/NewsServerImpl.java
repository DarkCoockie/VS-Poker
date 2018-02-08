/*
 * Implementation of TimeServer.
 */
package de.hsos.vs.rmi;

import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;

public class NewsServerImpl extends UnicastRemoteObject implements NewsServer {
    private List<NewsProxyImpl> newsProxys;

    public static void main(String[] args) {
        try {
            // Man kann entweder eine lokale Registry erzeugen ...
            LocateRegistry.createRegistry (Registry.REGISTRY_PORT);
            // ... oder verwendet die Standard-RMI Registry auf dem lokalen Host
            // unter Port 1099. In diesem Fall müssen aber Policies konfiguriert
            // werden. Siehe timer.policy in diesem Verzeichnis fuerr Hinweise.
            // Registry registry = LocateRegistry.getRegistry();
            Naming.rebind ("NewsServer", new NewsServerImpl());
            System.out.println ("NewsServerImpl registered as 'NewsServer' ...");
        } catch (Exception ex) {
            System.err.println ("Exception during server registration (port = " + Registry.REGISTRY_PORT + ")");
            ex.printStackTrace();
        }
    }
    
    private NewsServerImpl() throws RemoteException {
        // Default ctor overloaded to catch RemoteException
        System.out.println("NewsServerImpl created...");
        newsProxys = new ArrayList<NewsProxyImpl>();
    }
    
    private void log( String log ){
        System.out.println(new Date(System.currentTimeMillis()) + " " + log);
    }
    
    public void sendNews( String username, String newstext ) throws RemoteException {
        this.log( "[sendNews] " + username + ": " + newstext + " to " + newsProxys.size() + " newsProxys" );
        
        List<NewsProxyImpl> tempNewsProxys = new ArrayList<NewsProxyImpl>(this.newsProxys);
        
        for( int i = 0; i < tempNewsProxys.size(); i++ ){
            NewsProxyImpl newsProxy = tempNewsProxys.get(i);
            /*if(newsProxy.username.equals(username)){
                continue;
            }*/
            
            try{
                ClientProxy clientProxy = newsProxy.get();
                clientProxy.receiveNews( username, newstext );
            }catch( Exception e ){
                this.unsubscribeUser(newsProxy.username);
            }
        }
        System.out.println("success");
    }
    
    @Override
    public NewsProxy subscribeUser (String username, ClientProxy handle) throws RemoteException {
        log("[subscribeUser] username: " + username);
        NewsProxyImpl newsProxy = new NewsProxyImpl(handle, this, username);
        newsProxys.add(newsProxy);
        return newsProxy;
    }
    
    @Override
    public boolean unsubscribeUser (String username) throws RemoteException {
        log("[unsubscribeUser] username: " + username);
        for(int i = 0; i < newsProxys.size(); i++){
            if( newsProxys.get(i).username.equals(username)){
                newsProxys.remove(i);
                return true;
            }
        }
        return false;
    }
}
