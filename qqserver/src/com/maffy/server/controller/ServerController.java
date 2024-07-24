package com.maffy.server.controller;

import com.maffy.common.Message;
import com.maffy.common.User;
import com.maffy.server.service.ManageConnectClientThread;
import com.maffy.server.service.MessageService;
import com.maffy.server.service.NewsService;
import com.maffy.util.IOUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @auther 马强飞
 * 服务器，监听9999，等待客户的连接，并保持通信
 */
public class ServerController {

    private ServerSocket ss;
    //ConcurrentHashMap,可以处理并发，没有线程安全问题
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>> offlineMsg = new ConcurrentHashMap<>();

    private NewsService newsService = new NewsService();
    private MessageService messageService = new MessageService(newsService);

    static {
        validUsers.put("至尊宝", new User("至尊宝", "666", new int[]{33, 44}));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "666", new int[]{33, 45}));
        validUsers.put("菩提老祖", new User("菩提老祖", "666", new int[]{33, 42}));
    }

    private boolean checkUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        User validUser = validUsers.get(username);
        if (validUser == null) return false;
        if (!validUser.getPassword().equals(password)) return false;
        return true;
    }

    public void startQQ() {
        System.out.println("服务器在9999端口监听...");
        System.out.print(">");
        try {
            ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                User user = messageService.readUser(socket);
                if (checkUser(user)) {  //验证成功
                    ManageConnectClientThread.addBySocket(user.getUsername(), socket, messageService);
                    messageService.sendSuccessLoginMessageToOne(user.getUsername());
                    IOUtil.myPrintln(user.getUsername() + " 登入");
                    messageService.sendAllMessageToOne(user.getUsername());
                } else {  //验证失败
                    messageService.sendFailLoginMessageToOne(user.getUsername(), socket);
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ConcurrentHashMap<String, ArrayList<Message>> getOfflineMsg() {
        return offlineMsg;
    }

    public static ConcurrentHashMap<String, User> getValidUsers() {
        return validUsers;
    }
}
