package com.maffy.client.controller;

import com.maffy.client.service.MessageService;
import com.maffy.client.service.UserService;
import com.maffy.client.util.IOUtil;
import com.maffy.common.User;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class QQController {
    private boolean loop = true;
    private User user = new User();

    private UserService userService = new UserService(user);  //
    private MessageService messageService = new MessageService(user);

    public void startQQ() {
        System.out.print(">");
        IOUtil.printSystemMessage("你可以进行如下操作：\n\t @系统 | login 用户名 密码\n\t @系统 | exit");
        String param;
        String params[];
        while (loop) {
            System.out.print(">");
            param = IOUtil.readString(50);
            if (!param.startsWith("@系统")) {
                System.out.print(">");
                IOUtil.printSystemMessage("请使用\"@系统\"");
                continue;
            }
            System.out.print("\t");
            params = IOUtil.readLine().split(" ");
            if ("".equals(params[0])) {
                System.out.print(">");
                IOUtil.printSystemMessage("发送内容不能为空");
                continue;
            }
            if ("login".equals(params[0])) {
                if (params.length < 3) {
                    System.out.print(">");
                    IOUtil.printSystemMessage("需要指定用户名和密码");
                    continue;
                }
                String username = params[1];
                String password = params[2];
                if (userService.checkUser(username, password)) {
                    //QQView.indexPage(username);
                    while (loop) {
                        System.out.print(">");
                        param = IOUtil.readString(50);
                        if (!param.startsWith("@")) {
                            System.out.print(">");
                            IOUtil.printSystemMessage("请使用\"@\"");
                            continue;
                        }
                        System.out.print("\t");
                        params = IOUtil.readLine().split(" ");
                        if ("".equals(params[0])) {
                            System.out.print(">");
                            IOUtil.printSystemMessage("发送内容不能为空");
                        } else if ("@系统".equals(param)) {
                            if ("list".equals(params[0]))
                                userService.onlineFriendList();
                            else if ("logout".equals(params[0])) {
                                userService.logout();
                                loop = false;
                                System.out.print(">");
                                IOUtil.printSystemMessage("你可以进行如下操作：\n\t @系统 | login 用户名 密码\n\t @系统 | exit");
                            } else {
                                System.out.print(">");
                                IOUtil.printSystemMessage("没有该系统指令");
                            }
                        } else if ("@所有人".equals(param)) {
                            if ("file".equals(params[0]) && params.length >= 2) {
                                System.out.print(">");
                                IOUtil.printSystemMessage("暂不支持发送文件");
                            } else if (params.length >= 2){
                                System.out.print(">");
                                IOUtil.printSystemMessage("发送内容不能有空格");
                            } else {
                                messageService.sendMessageToALL(params[0]);
                            }
                        } else if (params.length == 1) {
                            messageService.sendMessageToOne(params[0], param.substring(1));
                        } else if ("file".equals(params[0]) && params.length == 2){
                            System.out.print(">");
                            IOUtil.printSystemMessage("暂不支持发送文件");
                        }
                    }
                    loop = true;
                } else {
                    System.out.print(">");
                    IOUtil.printSystemMessage("用户名或密码错误");
                }
            } else if ("exit".equals(params[0])) {
                loop = false;
            } else {
                System.out.print(">");
                IOUtil.printSystemMessage("没有该系统指令");
            }
        }
    }
}
