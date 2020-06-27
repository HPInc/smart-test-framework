package com.github.hpinc.jeangiacomin.stf.dataclasses.web.proxy;


import com.github.hpinc.jeangiacomin.stf.framework.logger.TestLog;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class ProxySettings {

    public String proxyAddress;
    public int proxyPort;
    public String proxyUsername;
    public String proxyUserPassword;
    public boolean directInternetConnection;
    public boolean requireProxyAuthentication;
    private TestLog testLog;

    /***
     * Class constructor.
     */
    public ProxySettings() {
        this.testLog = new TestLog();
    }

    /***
     * Class constructor.
     */
    public ProxySettings(boolean enableLogs) {
        this.testLog = new TestLog(enableLogs);
        this.directInternetConnection = true;
    }

    /***
     * Helper to configure the ProxySettings.
     * @param proxyAddress: The proxy address.
     * @param proxyPort: Proxy port.
     * @param proxyUsername: Username for proxy authentication.
     * @param proxyUserPassword :Password for proxy authentication.
     */
    public void configureProxySettings(String proxyAddress, String proxyPort, String proxyUsername, String proxyUserPassword) {
        if (proxyAddress != null && !proxyAddress.isEmpty()) {
            this.proxyAddress = proxyAddress;
            this.proxyPort = Integer.parseInt(proxyPort);
        }
        this.proxyUsername = proxyUsername.isEmpty() ? "" : proxyUsername;
        this.proxyUserPassword = proxyUserPassword.isEmpty() ? "" : proxyUserPassword;
        this.requireProxyAuthentication = !this.proxyUsername.isEmpty() || !proxyUserPassword.isEmpty() ? true : false;
        this.directInternetConnection = proxyAddress == null || proxyAddress.isEmpty() ? true : false;

        this.testLog.logIt(this.directInternetConnection ? "No proxy selected - direct internet connection" : String.format("The selected proxy was %1$s:%2$s", this.proxyAddress, Integer.toString(this.proxyPort)));
        if (this.requireProxyAuthentication) {
            this.testLog.logIt(String.format("Proxy user and password are respectively: [%1$s:%2$s]", proxyUsername, proxyUserPassword));
        }
    }
}