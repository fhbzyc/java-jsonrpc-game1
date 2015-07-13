package websocket.handler;

import java.util.Iterator;
import java.util.Map;

import org.springframework.web.socket.TextMessage;

public class BossBroadcast implements Runnable {

	private int serverId;

	private String msg;

	public BossBroadcast() {
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
			Map<Integer, Handler> map = EchoHandler.getBoss(this.serverId);
			for (Iterator<Map.Entry<Integer, Handler>> iter = map.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<Integer, Handler> entry = iter.next();

				Handler handler = entry.getValue();

				if(handler.getSession().isOpen()) {
					try {
						handler.getSession().sendMessage(new TextMessage(msg));
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					iter.remove();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
