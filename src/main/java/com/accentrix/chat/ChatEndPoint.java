package com.accentrix.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatEndPoint {

    private static Logger logger = LoggerFactory.getLogger(ChatEndPoint.class);

    private static CopyOnWriteArraySet<ChatRoom> webSocketSet = new CopyOnWriteArraySet<>();
    private ChatRoom cr;

    @OnOpen
    public void onOpen(Session session) {
        this.cr = new ChatRoom(UUID.randomUUID().toString(), session);
        webSocketSet.add(this.cr);
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this.cr);
    }

    @OnMessage
    public void onMessage(String message) {
        logger.debug("Message: " + message);
        logger.debug("Count: " + webSocketSet.size());
        logger.debug("UUID: " + this.cr.getId());
        broadcast(message);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    private void broadcast(String message) {
        webSocketSet.parallelStream().forEach(item -> {
            try {
                item.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
