package websocket.handler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.protocol.Request;
import com.zhanglong.sg.protocol.Response;

public class EchoHandler extends TextWebSocketHandler {

	public static ThreadLocal<Handler> connections = new ThreadLocal<Handler>(); 

    private Map<String, Handler> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

    	Handler handler = new Handler();
    	handler.setSession(session);
    	this.sessionMap.put(session.getId(), handler);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        this.sessionMap.remove(session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

    	Handler handler = this.sessionMap.get(session.getId());
    	EchoHandler.connections.set(handler);

		String content = message.getPayload();

    	int requestId = 0;

	   	try {

	   		// 解析请求
			Request request = this.unmarshal(content);
			requestId = request.getId();

			handler.requestId = requestId;

			// 执行对应的方法
			this.run(session, request);

	   	} catch (Exception e) {
			try {
				session.sendMessage(new TextMessage(Response.marshalError(requestId, -32600, e.getMessage())));
			} catch (IOException e1) {

			}
	   	}
    }

    private void run(WebSocketSession session, Request request) {

		// 首字母大写
		String name = request.getService();
		
		if (!name.equals("login") && !name.equals("server") &&  !name.equals("notice") && !(name.equals("role") && request.getMethod().equals("getPlayer"))) {
			if (EchoHandler.connections.get().roleId == 0) {
				try {
					session.sendMessage(new TextMessage(Response.marshalError(request.getId(), -1, "连接无效")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return ;
				}
			}
		}

		if(!Character.isUpperCase(name.charAt(0))) {
			name = (new StringBuilder()).append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).toString();
		}

		String javaName = "com.zhanglong.sg.service." + name + "Service";

		Object service;
		try {
			service = ContextLoader.getCurrentWebApplicationContext().getBean(Class.forName(javaName).newInstance().getClass());
		} catch (BeansException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			try {
				session.sendMessage(new TextMessage(Response.marshalError(request.getId(), -32601, e.getMessage())));
			} catch (IOException e1) {
			}
			return ;
		}

		Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(service.getClass());

		Method method = null;
		for (Method method2 : methods) {
			if (method2.getName().equals(request.getMethod()) && method2.getParameterTypes().length == request.getParams().length) {
				method = method2;
			}
		}

		if (method == null) {
			try {
				session.sendMessage(new TextMessage(Response.marshalError(request.getId(), -32601, "该方法不存在或无效")));
			} catch (IOException e1) {
			}
			return ;
		}

		Object[] params = request.getParams();
		for (int i = 0 ; i < params.length ; i++) {
			if (params[i] != null && params[i].getClass() == ArrayList.class) {

				@SuppressWarnings("unchecked")
				ArrayList<Integer> arr = (ArrayList<Integer>)params[i];

				int[] object2 = new int[arr.size()];
				for (int j = 0 ; j < arr.size() ; j++) {
					object2[j] = arr.get(j);
				}
				params[i] = object2;
			}

			if (params[i] != null && method.getParameterTypes()[i] == float.class) {
				if (params[i].getClass() == Integer.class) {
					params[i] = (float)((int)params[i]);
				} else if (params[i].getClass() == Double.class) {
					params[i] = (float)((double)params[i]);
				}
			}
		}

		ReflectionUtils.invokeMethod(method, service, params);
    }

    public Request unmarshal(String content) throws Exception {

    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.readValue(content, Request.class);
    }
}