package com.maffy.client.service;

import com.maffy.client.util.IOUtil;
import com.maffy.common.Message;
import com.maffy.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class ConnectServerThread extends Thread{
    private Socket socket;

    public ConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getType() == MessageType.MESSAGE_RET_ONLINE_FRIEND) {
                    String[] usernames = message.getContent().split(" ");
                    String content = "";
                    for (int i = 0; i < usernames.length - 1; i++) content += usernames[i] + "\n";
                    content += usernames[usernames.length - 1];
                    IOUtil.printSystemMessage(content);
                    System.out.print(">");
                } else if (message.getType() == MessageType.MESSAGE_COMM_MES) {
                    //打印接收到的聊天消息
                    IOUtil.printMessage(message);
                    System.out.print(">");
                } else if (message.getType() == MessageType.MESSAGE_TO_ALL_MES) {
                    //打印群聊消息
                    IOUtil.printMessage(message);
                    System.out.print(">");
                } else if (message.getType() == MessageType.MESSAGE_CLIENT_EXIT) {
                    ManageConnectServerThread.removeThread(message.getGetter());
                    socket.close();
                    return;
                } else {
                    IOUtil.printSystemMessage("收到其他类型的消息，未处理");
                    System.out.print(">");
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
