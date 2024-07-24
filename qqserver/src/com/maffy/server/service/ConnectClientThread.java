package com.maffy.server.service;

import com.maffy.common.Message;
import com.maffy.common.MessageType;
import com.maffy.util.IOUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @version 1.0
 * @auther 马强飞
 * 服务器端和一个客户端保持通信
 */
public class ConnectClientThread extends Thread{
    private Socket socket;
    private String username;

    private MessageService messageService = null;

    public ConnectClientThread(Socket socket, String username, MessageService messageService) {
        this.socket = socket;
        this.username = username;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message =(Message) ois.readObject();
                //根据message类型,做相应的业务处理
                if (message.getType() == MessageType.MESSAGE_GET_ONLINE_FRIEND) {
                    messageService.sendRESOnlineMessageToOne(message);
                    IOUtil.myPrintln(message.getSender() + " 请求在线列表");
                } else if (message.getType() == MessageType.MESSAGE_COMM_MES) {
                    if (messageService.sendMessageToOne(message))
                        IOUtil.myPrintln(message.getSender() + " @" + message.getGetter() + "：" + message.getContent());
                } else if (message.getType() == MessageType.MESSAGE_TO_ALL_MES) {
                    //遍历所有的通信线程，转发消息
                    messageService.sendMessageToAll(message);
                    IOUtil.myPrintln(message.getSender() +  " @所有人：" + message.getContent());
                } else if (message.getType() == MessageType.MESSAGE_CLIENT_EXIT){
                    IOUtil.myPrintln(message.getSender() + " 退出");
                    messageService.sendExitMessage(message);
                    ManageConnectClientThread.removeThread(message.getSender());
                    socket.close();
                    return;
                } else {
                    System.out.println("其他类型message, 暂时不处理");
                }
            } catch (Exception e) {
                try {
                    ManageConnectClientThread.removeThread(username);
                    IOUtil.myPrintln(username + " 退出");
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
