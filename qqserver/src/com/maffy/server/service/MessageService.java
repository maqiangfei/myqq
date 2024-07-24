package com.maffy.server.service;

import com.maffy.common.Message;
import com.maffy.common.MessageType;
import com.maffy.common.User;
import com.maffy.server.controller.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class MessageService {

    public NewsService newsService = null;

    public MessageService(NewsService newsService) {
        this.newsService = newsService;
    }

    public User readUser(Socket socket) {
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void sendFailLoginMessageToOne(String getterName, Socket socket) {
        Message message = new Message();
        message.setSender("系统");
        message.setType(MessageType.MESSAGE_LOGIN_FAIL);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSuccessLoginMessageToOne(String getterName) {
        Message message = new Message();
        message.setType(MessageType.MESSAGE_LOGIN_SUCCEED);
        User validUser = ServerController.getValidUsers().get(getterName);
        int[] headshot = validUser.getHeadshot();
        message.setContent(headshot[0] + " " + headshot[1]);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageConnectClientThread.getThread(getterName).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidUser(String username) {
        return ServerController.getValidUsers().containsKey(username);
    }


    private Boolean sendMessageToOne(Message message, String getterName) {
        if (!isValidUser(getterName)) {
            newsService.sendNewsToOne("该用户不存在", message.getSender());
            return false;
        }
        ConnectClientThread thread = ManageConnectClientThread.getThread(getterName);
        if (thread == null) {
            ArrayList<Message> allMsg = ServerController.getOfflineMsg().get(getterName);
            if (allMsg == null) {
                allMsg = new ArrayList<>();
                ServerController.getOfflineMsg().put(getterName, allMsg);
            }
            allMsg.add(message);
        } else {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Boolean sendMessageToOne(Message message) {
        return sendMessageToOne(message, message.getGetter());
    }

    public void sendMessageToAll(Message message) {
        ConcurrentHashMap<String, User> validUsers = ServerController.getValidUsers();
        for (String username : validUsers.keySet()) {
            if (!username.equals(message.getSender()))
                sendMessageToOne(message, username);
        }
    }

    public void sendAllMessageToOne(String username) {
        ArrayList<Message> allMsg = ServerController.getOfflineMsg().get(username);
        if (allMsg == null) return;
        Socket socket = ManageConnectClientThread.getThread(username).getSocket();
        ObjectOutputStream oos = null;
        for (Message msg : allMsg) {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ServerController.getOfflineMsg().remove(username);
    }

    public void sendRESOnlineMessageToOne(Message message) {
        String onlineUsernames = ManageConnectClientThread.getOnlineUser();
        Message msg = new Message();
        msg.setType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
        msg.setContent(onlineUsernames);
        msg.setGetter(message.getSender());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageConnectClientThread.getThread(message.getSender()).getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendExitMessage(Message message) {
        message.setGetter(message.getSender());
        message.setSender("系统");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageConnectClientThread.getThread(message.getGetter()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
