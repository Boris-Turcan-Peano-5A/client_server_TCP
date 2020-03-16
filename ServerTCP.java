package servertcp;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int portNumber= 1234;
		
		try {
			//server in ascolto
			ServerSocket server= new ServerSocket(portNumber);
			System.out.println("Server chatTCP in esecuzione...");
			
			while(true) {
				SocketWorker w;
				try {
					//in attesa della richiesta di connessione
					Socket socket= server.accept();
					//creo un nuovo worker
					w= new SocketWorker(socket);
					System.out.println("Creo un worker;");
					//thread per ogni worker
					Thread th= new Thread(w);
					th.start();
				}
				catch(Exception ex) {
					System.out.println("Connessione NON riuscita con client");
                    System.exit(-1);
				}
			}
		}
		catch(Exception ex) {
			System.out.println("Error! Porta: " + portNumber + " non disponibile");
            System.exit(-1);
		}
	}

}
