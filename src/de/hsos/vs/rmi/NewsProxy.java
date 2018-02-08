
package de.hsos.vs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NewsProxy extends Remote {
    public void sendNews (String Newstext) throws RemoteException;
}