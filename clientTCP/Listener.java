package clienttcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import clienttcpgui.ClientTCPgui;

public class Listener implements Runnable {
	  private Socket client;

	    //Constructor
	    public Listener(Socket client) {
	        this.client = client;
	        System.out.println("In ascolto con: " + client);
	    }

	    public void run(){
	        
	        // connetti al socket per poter leggere i dati che arrivano al Client
	        // (client <-- server)
	        BufferedReader in = null;
	        try{
	          in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        } catch (IOException e) {
	          System.out.println("IO Error!!!");
	          System.exit(-1);
	        }

	        //scrivi su terminale il testo ricevuto
	        String testoDaServer = "";
	        try {
	            while ((testoDaServer = in.readLine()) != null) {
	                System.out.println(testoDaServer);
	                //nel caso il testo ricevuto dal Server contiene "Bye." termina il Client
	                if (testoDaServer.contains("Bye.")) {
	                    client.close();
	                    System.exit(0);
	                    break;
	                }
	            }
	        } catch (IOException e) { 
	            try {
	                System.out.println("Connessione terminata dal Server");
	                client.close();
	                System.exit(-1);
	            } catch (IOException ex) {
	                System.out.println("Error nella chiusura del Socket");
	                System.exit(-1);
	            }
	        }
	    }
	}