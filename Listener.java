package clienttcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listener implements Runnable{
	private Socket client;

	public Listener(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.client= clientSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//connetti al socket per poter leggere i dati che arrivano al client
		BufferedReader in= null;
		try {
			in= new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IO Error!!!");
	        System.exit(-1);
		}
		//output del testo ricevuto
		String input="";
		try {
			while((input=in.readLine())!=null) {
				System.out.println(input);
				//Bye == termina il Client
				if(input.contains("Bye")) {
					client.close();
					System.exit(0);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connessione terminata dal Server");
			try {
				client.close();
				System.exit(-1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error nella chiusura del Socket");
				System.exit(-1);
			}
			finally {
				System.exit(0);
			}
			
		}
		
	}

}
