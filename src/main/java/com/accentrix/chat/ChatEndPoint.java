package com.accentrix.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatEndPoint {

    private static Logger logger = LoggerFactory.getLogger(ChatEndPoint.class);

    private static CopyOnWriteArraySet<ChatEndPoint> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        webSocketSet.add(this);
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {
        logger.debug("Message: " + message);
        broadcast(message);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void broadcast(String message) {
        webSocketSet.parallelStream().forEach(item -> {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
