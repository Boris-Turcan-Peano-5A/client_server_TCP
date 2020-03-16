package servertcp;

import java.util.ArrayList;

public class MessageManager {
	private String messaggio;
	private ArrayList<SocketWorker> workers= new ArrayList<>();
	
	void addClient(SocketWorker worker) {
		this.workers.add(worker);
	}
	
	void delClient(SocketWorker worker) {
		this.workers.remove(worker);
	}
	
	synchronized void sendNewMessage(String m) {
		this.messaggio= m;
		for(SocketWorker worker: this.workers) {
			worker.sendMessaggio(messaggio);
		}
	}
	
	interface InviaMessaggio{
		public void sendMessaggio(String m);
	}
	
	interface RiceviMessaggio{
		public void messaggioRicevuto(String m);
	}
}
