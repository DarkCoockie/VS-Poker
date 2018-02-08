/*
 * Java interface of Time server.
 */
package de.hsos.vs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NewsServer extends Remote {
    public NewsProxy subscribeUser (String username, ClientProxy handle) throws RemoteException;
    public boolean unsubscribeUser (String username) throws RemoteException;
}

