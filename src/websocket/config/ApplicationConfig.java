package websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import websocket.handler.EchoHandler;

@Configuration
@EnableWebSocket
public class ApplicationConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(new EchoHandler(), "/echo");
        // セッションごとにオブジェクトを分ける場合は以下のように書く
        // registry.addHandler(new PerConnectionWebSocketHandler(XxxHandler.class), "/echo");
    }

}
