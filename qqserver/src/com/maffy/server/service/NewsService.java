package com.maffy.server.service;

import com.maffy.common.Message;
import com.maffy.common.MessageType;
import com.maffy.server.controller.ServerController;
import com.maffy.util.IOUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class NewsService implements Runnable{

    public NewsService() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.print(">");
        while (true) {
            String params[] = IOUtil.readLine().split(" ");
            if (!params[0].startsWith("@") || params.length == 1) {
                IOUtil.myPrintln("错误命令");
            } else if ("@所有人".equals(params[0])) {
                sendNewsToAll(params[1]);
            } else {
                if (!sendNewsToOne(params[1], params[0].substring(1))){
                    IOUtil.myPrintln("没有该用户");
                }
                IOUtil.myPrintln();
            }
        }
    }

    private boolean isValidUser(String username) {
        return ServerController.getValidUsers().containsKey(username);
    }

    private void sendNewsToOne(Message message, String getterName) {
        message.setSender("系统");
        message.setHeadshot(new int[]{33, 41});
        message.setSendTime(LocalDateTime.now().toString());
        ConnectClientThread thread = ManageConnectClientThread.getThread(getterName);
        if (thread == null) {
            ArrayList<Message> allMsg = ServerController.getOfflineMsg().get(getterName);
            if (allMsg == null) {
                allMsg = new ArrayList<>();
                ServerController.getOfflineMsg().put(getterName, allMsg);
            }
            allMsg.add(message);
            return;
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean sendNewsToOne(String content, String getterName) {
        Message message = new Message();
        message.setContent(content);
        message.setType(MessageType.MESSAGE_COMM_MES);
        if (!isValidUser(getterName)) {
            return false;
        }
        sendNewsToOne(message, getterName);
        return true;
    }

    public void sendNewsToAll(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setType(MessageType.MESSAGE_TO_ALL_MES);
        for (String getterName : ServerController.getValidUsers().keySet()) {
            sendNewsToOne(message, getterName);
        }
    }
}
