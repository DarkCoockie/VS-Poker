
package de.hsos.vs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientProxy extends Remote {
    public void receiveNews (String username, String Newstext) throws RemoteException;
}