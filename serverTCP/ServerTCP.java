package servertcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    

    public static void main(String[] args) {

        int portNumber = 1234; //porta a cui il server si mettera' in ascolto
        
        try{
            //metto il server in ascolto alla porta desiderata per clients che
            //richiedono connessione
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server chatTCP in esecuzione...");

            while(true){
                //Socket Worker e' l'oggetto che si occupa di servire il client
                //che si e' connesso, ne verra' generato un Worker per ogni
                //client e verra' eseguito in un suo Thread personale
                SocketWorker w;
                    //mi metto in attesa di una nuova richiesta di connessione
                    //server.accept() restituisce il nuovo socket
                    Socket newSocket = server.accept();
                    //una nuova connessione con un nuovo client e' stata creata
                    //creo un nuovo "servitore"(SocketWorker) che si prendera'
                    //cura del nuovo client
                    w = new SocketWorker(newSocket);

                    //genero il Thread per l'esecuzione del nuovo Worker
                    Thread t = new Thread(w);
                    //Avvio l'esecuzione del nuovo worker nel Thread
                    t.start();
            }
        } catch (IOException e) {
            System.out.println("Error! Porta: " + portNumber + " non disponibile");
            System.exit(-1);
        }

        
    }
}
