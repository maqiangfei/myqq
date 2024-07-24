package com.maffy.client.service;

import com.maffy.client.util.IOUtil;
import com.maffy.common.Message;
import com.maffy.common.MessageType;
import com.maffy.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class UserService {

    private User user;
    private Socket socket;

    public UserService(User user) {
        this.user = user;
    }

    public boolean checkUser(String username, String password){
        boolean b = false;
        user.setUsername(username);
        user.setPassword(password);
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //发送user给服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            //读取服务端回复的message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getType() == MessageType.MESSAGE_LOGIN_SUCCEED) {
                System.out.print(">");
                IOUtil.printSystemMessage("登入成功，你可以进行如下操作：\n\t @系统 | list\n\t @系统 | logout\n\t @所有人 | content\n\t @用户名 | content");
                ManageConnectServerThread.addBySocket(username, socket);
                String figures[] = message.getContent().split(" ");
                int[] headshot = new int[]{Integer.parseInt(figures[0]), Integer.parseInt(figures[1])};
                user.setHeadshot(headshot);
                b = true;
            } else {
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void onlineFriendList() {
        Message message = new Message();
        message.setSender(user.getUsername());
        message.setType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        //将请求发送给服务器
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageConnectServerThread.getThread(user.getUsername())
                            .getSocket().getOutputStream()); //socket.getOutputStream();
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        Message message = new Message();
        message.setType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUsername());  //指名是那个客服端退出登入
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
