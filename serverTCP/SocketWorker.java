package servertcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import servertcpgui.ServerTCPgui;

public class SocketWorker implements Runnable, InviaMessaggio, RiceviMessaggio  {
    //creo il gestore di Messaggi che permette ai vari workers di comunicare tra loro
    private  static final  MessageManager gestoreMessaggi = new MessageManager();
    private Socket client;
    private PrintWriter out = null;

    //Constructor: inizializza le variabili
    public SocketWorker(Socket client) {
        this.client = client;
        gestoreMessaggi.addClient(this);
        System.out.println("Connesso con: " + client);
    }
    
    @Override
    public void messaggioReceived(String m) {
        this.gestoreMessaggi.sendNewMessaggio(m);
    }
    
    //Questo metodo e' invocato dal metodo setNewMessaggio nella 
    //classe Evento ogni volta che viene generato un evento(messaggioReceived)
    //e rappresenta la richiesta di inviare il messaggio che e' stato appena 
    //ricevuto da uno dei client connessi
    @Override
    public void sendMessaggio(String messaggio) {
        
        //Invia lo stesso messaggio appena ricevuto 
        out.println("Server->> " + messaggio);
        
    }

    // Questa e' la funzione che viene lanciata quando il nuovo "Thread" 
    // viene generato
    public void run(){
        
        BufferedReader in = null;
        try{
          // connessione con il socket per ricevere (in) e inviare(out) il testo
          // da/al client connesso
          in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
          System.out.println("Errore: in|out fallito");
          System.exit(-1);
        }

        String line = "";
        int clientPort = client.getPort(); //il "nome" del mittente (client)
        while(line != null){
          try{
            //mi metto in attesa di ricevere un nuovo messaggio da client
            line = in.readLine();
            //il nuovo messaggio e' stato ricevuto e lo andiamo ad inserire
            //nella variabile "messaggio" della classe EventoReceiver
            //il quale aggiornera' la variabile e richiedera' l'invio a ogni
            //Worker, ognuno al proprio client
            messaggioReceived(line);
            //scrivi messaggio ricevuto su terminale
            System.out.println(clientPort + ">> " + line);
           } catch (IOException e) {
            System.out.println("lettura da socket fallito");
            System.exit(-1);
           }
        }
        try {
            client.close();
            System.out.println("connessione con client: " + client + " terminata!");
        } catch (IOException e) {
            System.out.println("Errore connessione con client: " + client);
        }
    }
}