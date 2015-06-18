package websocket.handler;

import java.io.IOException;

public class Broadcast implements Runnable {

	private int serverId;
	private String msg;

	public Broadcast() {
	}

	public void send(int serverId, String msg) {
		this.serverId = serverId;
		this.msg = msg;

		Thread thread = new Thread(this);  
		thread.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			EchoHandler.broadcast(this.serverId, this.msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
