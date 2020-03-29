/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.didiglobal.sds.client.util;

import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


/**
 * IP工具类
 *
 * @author manzhizhen
 * @version $Id: IpUtils.java, v 0.1 2015年9月22日 下午3:45:13 Administrator Exp $
 */
public class IpUtils {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private static String ip = "Unknown";

    private static String hostname = "Unknown";

    static {
        try {

            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses != null && addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress() && inetAddress.
                            getHostAddress() != null && inetAddress.getHostAddress().indexOf(":") == -1) {

                        ip = inetAddress.getHostAddress();
                        hostname = inetAddress.getHostName();
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("IpUtils 获取本地IP地址异常", e);
        }
    }

    public static String getIp() {
        return ip;
    }

    public static String getHostname() {
        return hostname;
    }
}
