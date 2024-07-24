package com.maffy.client.service;

import com.maffy.common.Message;
import com.maffy.common.MessageType;
import com.maffy.common.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class MessageService {

    private User user;

    public MessageService(User user) {
        this.user = user;
    }

    public void sendMessageToOne(String content, String getterName) {
        Message message = new Message();
        message.setType(MessageType.MESSAGE_COMM_MES);
        message.setGetter(getterName);
        setMessageInfo(message, content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageConnectServerThread.getThread(user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToALL(String content) {
        Message message = new Message();
        message.setType(MessageType.MESSAGE_TO_ALL_MES);  //群发消息
        setMessageInfo(message, content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageConnectServerThread.getThread(user.getUsername()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMessageInfo(Message message, String content) {
        message.setSender(user.getUsername());
        message.setHeadshot(user.getHeadshot());
        message.setContent(content);
        message.setSendTime(LocalDateTime.now().toString());
    }
}
