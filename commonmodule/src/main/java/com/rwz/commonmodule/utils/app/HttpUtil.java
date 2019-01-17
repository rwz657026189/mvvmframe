package com.rwz.commonmodule.utils.app;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpUtil {

    /**
     * 根据域名获取ip地址 (必须在子线程调用)
     * @param domain e.g : www.baidu.com
     * @return e.g : 180.97.33.108
     */
    public static String getIP(String domain) {
        String IPAddress = "";
        InetAddress ReturnStr1 = null;
        try {
            ReturnStr1 = InetAddress.getByName(domain);
            IPAddress = ReturnStr1.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return IPAddress;
//        return "192.168.3.121";
    }

    public static boolean ping(String domain) {
        boolean result = false;
        InetAddress address;
        try {
            address = InetAddress.getByName(domain);
            System.out.println("Name:" + address.getHostName());
            System.out.println("Addr:" + address.getHostAddress());
            System.out.println("Reach:" + (result = address.isReachable(3000))); //是否能通信 返回true或false
            System.out.println(address.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
