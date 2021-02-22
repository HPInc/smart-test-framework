package com.github.jeansantos38.stf.framework.network;

import com.github.jeansantos38.stf.framework.regex.RegexHelper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class NetworkHelper {


    public static String retrieveLocalIPv4Address(String networkInterface) throws SocketException {
        return retrieveLocalIpAddress(IpVersion.IPV4, networkInterface);
    }

    public static String retrieveLocalIPv6Address(String networkInterface) throws SocketException {
        return retrieveLocalIpAddress(IpVersion.IPV6, networkInterface);
    }

    /***
     * Helper to retrieve the ip address from a given network interface.
     * Works in a best effort basis, should return the first non-loopback IP address it can find.
     * @param networkInterface: Network interface name.
     * @return Its IP address.
     * @throws SocketException
     */
    private static String retrieveLocalIpAddress(IpVersion ipVersion, String networkInterface) throws SocketException {
        NetworkInterface ni = NetworkInterface.getByName(networkInterface);
        Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
        String myIP = "";
        while (inetAddresses.hasMoreElements()) {
            InetAddress ia = inetAddresses.nextElement();
            if (!ia.isLinkLocalAddress()) {
                switch (ipVersion) {
                    case IPV4:
                        if (isValidIpv4(ia.getHostAddress())) {
                            return ia.getHostAddress();
                        }
                        break;
                    case IPV6:
                        if (isValidIpv6(ia.getHostAddress())) {
                            return ia.getHostAddress();
                        }
                        break;
                }
            }
        }
        return myIP;
    }

    /**
     * Helper that lists all available network interfaces.
     *
     * @return
     * @throws SocketException
     */
    public static String listAllNetworkInterfacesAvailable() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        String finalString = "";

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            finalString += networkInterface.getName() + " # ";
        }
        return finalString;
    }

    public static boolean isValidIpv6(String ipAddress) {
        return RegexHelper.isMatch("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))",
                ipAddress);
    }

    public static boolean isValidIpv4(String ipAddress) {
        return RegexHelper.isMatch("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",
                ipAddress);
    }

    public enum IpVersion {IPV4, IPV6}
}