package com.mwm.loyal.utils;

import java.net.InetAddress;

public class PubUtils {
    public static String getLocalHostIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String localName = inetAddress.getHostName();
            String localIp = inetAddress.getHostAddress();
            System.out.println("本机名称是：" + localName);
            System.out.println("本机的ip是 ：" + localIp);
            return localIp;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getLocalHostIpAddress:"+e.getMessage());
            return "";
        }
    }
}