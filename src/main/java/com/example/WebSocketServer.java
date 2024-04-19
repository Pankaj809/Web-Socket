package com.example;

import io.micronaut.http.annotation.PathVariable;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@ServerWebSocket("/server1/{username}")
public class WebSocketServer {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final WebSocketBroadcaster broadcaster;

    public WebSocketServer(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(String username, WebSocketSession session) {
        sessions.put(username, session);
        broadcaster.broadcastSync(username + " joined the chat");
    }

    @OnMessage
    public void onMessage(String message, WebSocketSession session, @PathVariable String username) {
            sessions.put(message,session);
            broadcaster.broadcastSync(username + ": " + message);

    }

//    private void createUser(String message, String username) {
//        sessions.put(username, null);
//        broadcaster.broadcastSync("User '" + username + "' created with message: " + message);
//    }

    @OnClose
    public void onClose(String username) {
        sessions.remove(username);
        broadcaster.broadcastSync(username + " left the chat");
    }
}
