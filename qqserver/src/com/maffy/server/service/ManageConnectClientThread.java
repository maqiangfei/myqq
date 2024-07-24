package com.maffy.server.service;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class ManageConnectClientThread {
    private static HashMap<String, ConnectClientThread> map = new HashMap<>();

    public static void addThread(String username, ConnectClientThread connectClientThread) {
        map.put(username, connectClientThread);
    }

    public static void addBySocket(String username, Socket socket, MessageService messageService) {
        ConnectClientThread connectClientThread = new ConnectClientThread(socket, username, messageService);
        connectClientThread.start();
        map.put(username, connectClientThread);
    }

    public static void removeThread(String username) {
        map.remove(username);
    }

    public static ConnectClientThread getThread(String username) {
        return map.get(username);
    }

    public static String getOnlineUser() {
        Set<String> usernames = map.keySet();
        StringBuilder sb = new StringBuilder();
        for (String username : usernames) {
            sb.append(username + " ");
        }
        return sb.toString();
    }

    public static HashMap<String, ConnectClientThread> getMap() {
        return map;
    }
}
