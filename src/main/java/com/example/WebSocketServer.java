package com.example;

import com.example.entity.MessageObject;
import com.google.gson.Gson;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import jakarta.inject.Inject;

import java.util.concurrent.ConcurrentHashMap;

@ServerWebSocket("/server1/{username}")
public class WebSocketServer {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final WebSocketBroadcaster broadcaster;
    private final Gson gson;

    @Inject
    public WebSocketServer(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
        this.gson = new Gson();
    }

    @OnOpen
    public void onOpen(String username, WebSocketSession session) {
        sessions.put(username, session);
        broadcaster.broadcastSync(username + " joined the chat");
    }

    @OnMessage
    public void onMessage(String message, String username) {
        MessageObject receivedMessage = gson.fromJson(message, MessageObject.class);
        handleMessage(receivedMessage, username);
    }

    private void handleMessage(MessageObject messageObject, String username) {
        if (messageObject.getCommand().equals("cmd_connect")) {
            sendReply(username);
        } else {
            broadcaster.broadcastSync("Input valid content");
        }
    }

    private void sendReply(String username) {
        MessageObject reply = new MessageObject(username, "Welcome to the chat!");
//        sessions.get(username).send(gson.toJson(reply));
        broadcaster.broadcastSync(gson.toJson(reply));
    }

    @OnClose
    public void onClose(String username, WebSocketSession session) {
        sessions.remove(username);
        broadcaster.broadcastSync(username + " left the chat");
    }
}