package com.maffy.client.view;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class QQView {
    public static void loginPage() {
        System.out.println("你可以进行如下操作：\n\t @系统 | login 用户名 密码\n\t @系统 | exit");
        System.out.println("\t @系统 | login 用户名 密码\n");
        System.out.println("\t\t @系统 | exit");
    }

    public static void indexPage(String username) {
        System.out.println("登入成功，你可以进行如下操作：\n\t @系统 | list\n\t @系统 | logout\n\t @所有人 | content\n\t @用户名 | content");
        System.out.println("\t\t @系统 | list");
        System.out.println("\t\t @系统 | logout");
        System.out.println("\t\t @所有人 | content");
        System.out.println("\t\t @所有人 | file src");
        System.out.println("\t\t @用户名 | content");
        System.out.println("\t\t @用户名 | file src");
    }
}
