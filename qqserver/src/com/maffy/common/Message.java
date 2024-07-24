package com.maffy.common;

import java.io.Serializable;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;//发送者
    private int[] headshot;
    private String getter;//接收者
    private String content;//内容
    private String sendTime;//发送时间
    private int type;//消息类型[可以在接口定义消息类型]

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(int[] headshot) {
        this.headshot = headshot;
    }
}
