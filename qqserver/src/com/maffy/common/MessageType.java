package com.maffy.common;

/**
 * @version 1.0
 * @auther 马强飞
 */
public interface MessageType {
    int MESSAGE_LOGIN_SUCCEED = 1;
    int MESSAGE_LOGIN_FAIL = 2;
    int MESSAGE_COMM_MES = 3;  //普通信息包
    int MESSAGE_GET_ONLINE_FRIEND = 4; //要求返回在线用户列表
    int MESSAGE_RET_ONLINE_FRIEND = 5; //返回在线用户列表
    int MESSAGE_CLIENT_EXIT = 6; //客户端请求退出
    int MESSAGE_TO_ALL_MES = 7; //群发消息
}
