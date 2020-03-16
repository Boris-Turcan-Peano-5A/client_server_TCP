package clienttcp;

import java.net.InetAddress;
import java.io.IOException;
import java.io.*;
import java.net.*;

public class ClientTesto {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hostName= "127.0.0.1";
		int  portNumber= 1234;
		
		try {
			//traduzione della stringa in corrispondente IP
			InetAddress address= InetAddress.getByName(hostName);
			//creazione socket
			Socket clientSocket= new Socket(address, portNumber);
			//istruzioni
			System.out.println("Client-Testo: usa Ctrl-C per terminare, ENTER per spedire la linea di testo.\n");
			//connessione concorrente al socket per ricevere i dati da Server
			Listener l;
			try {
				l= new Listener(clientSocket);
				Thread t= new Thread(l);
				t.start();
			}
			catch(Exception e) {
				System.out.println("Connessione non riuscita con server");
			}
			//connesiione al socket (in uscita client --> server)
			PrintWriter out= new PrintWriter(clientSocket.getOutputStream());
			//connessione allo stdIn per inserire il testo dalla linea di comando
			BufferedReader stdIn= new BufferedReader(new InputStreamReader(System.in));
			String userInput;
			//leggi di linea i comando il testo da spedire al server
			System.out.print(">");
			while((userInput= stdIn.readLine())!=null) {
				//codifica messaggio
				//String s=codificaMessaggioCesare(userInput, 3);
				//scrittura messaggio da spedire
				out.println(userInput);
				System.out.println("Spedito: "+userInput);
				System.out.print(">");
			}
			//chiusura socket
			clientSocket.close();
			System.out.println("connessione terminata");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String codificaMessaggioCesare(String m, int chiave) {
		String s=m;
		char[] codificata=new char[s.length()]; 
		  int[] charValues= new int[s.length()];
		  for(int i=0; i<s.length(); i++) {
			  charValues[i]=(s.charAt(i)+chiave)%127;
			  codificata[i]=(char) charValues[i];
		  }
		  String c=String.copyValueOf(codificata);
		return c;
	}

}
