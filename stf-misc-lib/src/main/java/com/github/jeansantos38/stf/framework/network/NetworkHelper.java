package com.github.jeansantos38.stf.framework.network;

import com.github.jeansantos38.stf.framework.logger.TestLog;

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

    public String localIpAddress;
    public String networkInterfaceName;
    private TestLog testLog;

    /***
     * Class Constructor.
     * @param networkInterface: Network interface name.
     * @throws SocketException
     */
    public NetworkHelper(String networkInterface) throws SocketException {
        this.testLog = new TestLog();
        try {
            this.localIpAddress = retrieveLocalIpAddress(networkInterface);
        } catch (Exception e) {
            this.testLog.logIt(e.getMessage());
            this.testLog.logIt(String.format("The interface '%1$s' does not exist. Setting loopback ip address.", networkInterface));
            this.localIpAddress = "127.0.0.1";
            this.testLog.logIt(String.format("Instead found:%1$s.", listAllNetworkInterfacesAvailable()));
        }
        this.networkInterfaceName = networkInterface;
    }

    /***
     * Helper to retrieve the ip address from a given network interface.
     * @param networkInterface: Network interface name.
     * @return Its IP address.
     * @throws SocketException
     */
    public String retrieveLocalIpAddress(String networkInterface) throws SocketException {
        NetworkInterface ni = NetworkInterface.getByName(networkInterface);

        Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
        String myIP = "";

        while (inetAddresses.hasMoreElements()) {
            InetAddress ia = inetAddresses.nextElement();
            if (!ia.isLinkLocalAddress()) {
                myIP = ia.getHostAddress();
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
    public String listAllNetworkInterfacesAvailable() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        String finalString = "";

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            finalString += networkInterface.getName() + " # ";
        }
        return finalString;
    }
}