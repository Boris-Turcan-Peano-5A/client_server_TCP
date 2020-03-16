package servertcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import servertcp.MessageManager.InviaMessaggio;
import servertcp.MessageManager.RiceviMessaggio;

public class SocketWorker implements Runnable, InviaMessaggio, RiceviMessaggio{
	
	private static final MessageManager gestoreMessaggi= new MessageManager();
	private Socket client;
	private PrintWriter out;
	
	SocketWorker(Socket client){
		this.client= client;
		gestoreMessaggi.addClient(this);
	}

	public void sendMessaggio(String m) {
		// TODO Auto-generated method stub
		//invia lo stesso messaggio appena ricevuto
		out.println("[Server]>"+m);
	}

	@Override
	public void messaggioRicevuto(String m) {
		// TODO Auto-generated method stub
		//m=this.decofificaMessaggioCesare(m, 3);
		this.gestoreMessaggi.sendNewMessage(m);
	}
	//decodifica messaggio
	public String decofificaMessaggioCesare(String m, int chiave) {
		String s=m;
		char[] codificata=new char[s.length()]; 
		  int[] charValues= new int[s.length()];
		  for(int i=0; i<s.length(); i++) {
			  charValues[i]=(s.charAt(i)-chiave)%127;
			  codificata[i]=(char) charValues[i];
		  }
		  String c=String.copyValueOf(codificata);
		return c;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader in= null;
		//connessione per ricevere e inviare il testo
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out= new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore: in|out fallito");
	        System.exit(-1);
		}
		String line= "";
		int clientPort = client.getPort(); //il "nome" del mittente (client)
		while(line != null) {
			try {
				//mi metto in attesa di ricevere un nuovo messaggio da client
				line= in.readLine();
				//messaggio ricevuto -> inseriamo nella var. "messaggio" della classe eventReceiver -> invio a ogni Worker -> invio a ogni client
				messaggioRicevuto(line);
				//messaggio ricevuto scrivo sul terminale
				System.out.println("["+clientPort+"]>> "+line);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("lettura da socket fallito");
	            System.exit(-1);
			}
			
		}
		try {
			client.close();
			System.out.println("connessione con client " + client + " terminata!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 System.out.println("Errore connessione con client: " + client);
		}
		
	}

}
