package com.maffy.client.service;

import java.net.Socket;
import java.util.HashMap;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class ManageConnectServerThread {
    private static HashMap<String, ConnectServerThread> map = new HashMap<>();

    public static void addThread(String username, ConnectServerThread connectServerThread) {
        map.put(username, connectServerThread);
    }

    public static void addBySocket(String username, Socket socket) {
        ConnectServerThread thread = new ConnectServerThread(socket);
        thread.start();
        map.put(username, thread);
    }

    public static ConnectServerThread getThread(String username){
        return map.get(username);
    }

    public static void removeThread(String username) {
        map.remove(username);
    }
}
