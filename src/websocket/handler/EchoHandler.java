package websocket.handler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.MissionDao;
import com.zhanglong.sg.protocol.Request;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.utils.SpringContextUtils;

public class EchoHandler extends TextWebSocketHandler {

    public static ThreadLocal<Handler> connections = new ThreadLocal<Handler>(); 

    private static Map<String, Handler> sessionMap = new ConcurrentHashMap<>();

    // 世界BOSS
    private static Map<Integer, Map<Integer, Handler>> bossPlayers = new ConcurrentHashMap<>();

    public static void bossPut(Handler handler) {
    	Map<Integer, Handler> map = EchoHandler.bossPlayers.get(handler.getServerId());
    	if (map == null) {
    		map = new ConcurrentHashMap<>();
    		EchoHandler.bossPlayers.put(handler.getServerId(), map);
    	}
    	map.put(handler.getRoleId(), handler);
    }

    public static void bossRemove(int serverId, int roleId) {
    	Map<Integer, Handler> map = EchoHandler.bossPlayers.get(serverId);
    	if (map != null) {
    		map.remove(roleId);
    	}
    }

    public static Map<Integer, Handler> getBoss(int serverId) {
    	Map<Integer, Handler> map = EchoHandler.bossPlayers.get(serverId);
    	if (map == null) {
    		map = new ConcurrentHashMap<>();
    		EchoHandler.bossPlayers.put(serverId, map);
    	}
    	return map;
    }

    public static void bossClear(int serverId) {
    	Map<Integer, Handler> map = EchoHandler.bossPlayers.get(serverId);
    	if (map != null) {
    		map.clear();
    	}
    }

    public static void broadcast(int roleId, int serverId, String msg) throws JsonParseException, JsonMappingException, IOException {

        for (Iterator<Map.Entry<String, Handler>> iter = EchoHandler.sessionMap.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Handler> entry = iter.next();
            Handler handler = entry.getValue();
            if (handler.serverId == serverId && roleId != handler.getRoleId()) {

                   try {

                       handler.getSession().sendMessage(new TextMessage(msg));

                   } catch (Exception e) {

                   }
            }
        }
    }

    public static int size() {
        return sessionMap.size();
    }

    // 单发
    public static void pri(int roleId, String msg) {
        for (Iterator<Map.Entry<String, Handler>> iter = EchoHandler.sessionMap.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Handler> entry = iter.next();
            Handler handler = entry.getValue();
            if (handler.roleId == roleId) {
                try {
                    handler.getSession().sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Handler handler = new Handler();
        handler.setSession(session);
        EchoHandler.sessionMap.put(session.getId(), handler);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        EchoHandler.sessionMap.remove(session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws BeansException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

        Handler handler = EchoHandler.sessionMap.get(session.getId());
        EchoHandler.connections.set(handler);

        MissionDao.cache.set(null);

        String content = message.getPayload();

        if (content.equals("p")) {
            session.sendMessage(new TextMessage("p"));
            return ;
        }

        Request request = null;
        try {
            // 解析请求
            request = this.unmarshal(content);

        } catch (Exception e) {
            try {
                session.sendMessage(new TextMessage(Response.marshalError(0, -32600, "Invalid Request")));
            } catch (IOException e1) {
            }
        }

        if (request != null) {
           handler.requestId = request.getId();
           try {
			this.run(session, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    }

    private void run(WebSocketSession session, Request request) throws Exception {
        // 首字母大写
        String name = request.getService();

        if (!name.equals("login") && !name.equals("server") && !name.equals("notice") && !(name.equals("role") && request.getMethod().equals("getPlayer"))) {
            if (EchoHandler.connections.get().roleId == 0) {
                try {
                    session.sendMessage(new TextMessage(Response.marshalError(request.getId(), -1, "Session disconnect")));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    return ;
                }
            }
        }

        if(!Character.isUpperCase(name.charAt(0))) {
            name = (new StringBuilder()).append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).toString();
        }

        String javaName = "com.zhanglong.sg.service." + name + "Service";

        Object service = SpringContextUtils.getBean(Class.forName(javaName).newInstance().getClass());

        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(service.getClass());

        Method method = null;
        for (Method method2 : methods) {
            if (method2.getName().equals(request.getMethod()) && method2.getParameterTypes().length == request.getParams().length) {
                method = method2;
            }
        }

        if (method == null) {
            try {
                session.sendMessage(new TextMessage(Response.marshalError(request.getId(), -32601, "Method not found")));
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

        if (request.getService().equals("notice") && request.getMethod().equals("notice")) {
            session.close();
        }
    }

    public Request unmarshal(String content) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, Request.class);
    }

    public static void close(WebSocketSession session, int roleId, boolean sendMsg) {
        for (Iterator<Map.Entry<String, Handler>> iter = EchoHandler.sessionMap.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Handler> entry = iter.next();
            Handler handler = entry.getValue();

            if (!session.getId().equals(handler.getSession().getId()) && handler.roleId == roleId && handler.getSession().isOpen()) {
                try {

                    if (sendMsg) {
                        String str = Response.marshalError(0, -1, "Session disconnect");
                        handler.getSession().sendMessage(new TextMessage(str));
                    }
                    handler.getSession().close();
                    iter.remove();

                } catch (Exception e) {
                }
            }
        }
    }
}
