package com.maffy.util;

import com.maffy.common.Message;
import com.maffy.common.MessageType;

import java.util.Scanner;

/**
 * @version 1.0
 * @auther 马强飞
 */
public class IOUtil {
    public static String readString(int maxLength) {
        Scanner scanner = new Scanner(System.in);
        String res = scanner.next();
        if (res.length() <= maxLength) return res;
        return res.substring(0, maxLength);
    }

    public static String readLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


//  格式：System.out.println("\33[前景色代号;背景色代号;数字m");
//  %n表示换行
//  \33[0m 变回原样
//  背景颜色代号(41-46)
//  前景色代号(31-36)
//  数字+m：1加粗；3斜体；4下划线
    public static void println(String content) {
        System.out.println(content);
    }

    private static void print(String content, int color, int bgColor) {
        System.out.format("\33[%d;%d;1m%s\33[0m %n", color, bgColor, content);
    }

    private static void print(String content, int color) {
        System.out.format("\33[%d;1m%s\33[0m %n", color, content);
    }

    public static void printMessage(Message message) {
        String senderName = message.getSender();
        int[] headshot = message.getHeadshot();
        print(senderName, headshot[0], headshot[1]);
        if (message.getType() == MessageType.MESSAGE_TO_ALL_MES) {
             System.out.print(" @所有人");
        }
        myPrintln();
        System.out.println("\t" + message.getContent());
        System.out.println();
        System.out.print(">");
    }

    public static void myPrintln(String content) {
        System.out.println(content);
        System.out.print(">");
    }

    public static void myPrintln() {
        System.out.println();
        System.out.print(">");
    }
}
