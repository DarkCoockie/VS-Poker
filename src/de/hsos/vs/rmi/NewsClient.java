/*
 * Client for TimeServer.
 */
package de.hsos.vs.rmi;

import java.io.Console;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.registry.LocateRegistry;

public class NewsClient {
    String username = "";
    ClientProxy clientProxy;
    NewsProxy newsProxy;
    NewsServer server;
    
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            NewsServer server = (NewsServer) registry.lookup("NewsServer");
            System.out.println("Connected to NewsServer!");
            
            NewsClient newsClient = new NewsClient( server );
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    
    public NewsClient( NewsServer server ){
        this.server = server;
        try{
            this.clientProxy = new ClientProxyImpl( this );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.start();
    }
    
    private void start(){
        System.out.println("Input username:");
        if(System.console()==null){
            System.out.println("console is null");
        }
        this.username = System.console().readLine();
        boolean stop;
        do {
            stop = !this.update();
        } 
        while( !stop );
    }
    
    private boolean update(){
        System.out.println("-S-ubscribe to News, -U-nsubscribe from News, -C-reate News:");
        String input = System.console().readLine();
        
        if(input.length() <= 0){
            System.out.println("Invalid input!");
            return true;
        }
        
        char action = input.charAt(0);
        switch(action){
            case 'S':
                return this.subscribe();
            case 'U':
                return this.unsubscribe();
            case 'C':
                return this.create();
        }
        
        return true;
    }
    
    private boolean subscribe(){
        try{
            this.newsProxy = this.server.subscribeUser( this.username, this.clientProxy );
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private boolean unsubscribe(){
       boolean result; 
        try{
            result = this.server.unsubscribeUser( this.username );
        } catch (RemoteException e) {
            e.printStackTrace();
            result = false;
        }
        this.newsProxy = null;
        return result;
    }
    
    private boolean create(){
        if( this.newsProxy == null ){
            System.out.println("You have to subscribe to the news first!");
            return true;
        }
        
        System.out.println("Input text:");
        String input = System.console().readLine();
        
        try{
            this.newsProxy.sendNews(input);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void receiveNews( String username, String newstext ){
        System.out.println( "receiving news from " + username + ":" );
        System.out.println( newstext );
    }
}
