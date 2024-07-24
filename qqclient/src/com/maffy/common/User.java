package com.maffy.common;

import java.io.Serializable;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private int[] headshot;

    public User() {
    }

    public User(String username, String password, int[] headshot) {
        this.username = username;
        this.password = password;
        setHeadshot(headshot);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(int[] headshot) {
        if (headshot[0] < 31 || headshot[0] > 36) headshot[0] = 31;
        if (headshot[1] < 41 || headshot[1] > 46) headshot[1] = 41;
        this.headshot = headshot;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
