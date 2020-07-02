package com.github.jeansantos38.stf.framework.httpclient;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    /***
     * Return the method name.
     * @return
     */
    public String getMethod() {
        return METHOD_NAME;
    }

    /***
     * Helper to perform a delete operation when the request does contain a payload.
     * @param uri: The uri to be accessed.
     */
    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    /***
     * Helper to perform a delete operation when the request does contain a payload.
     * @param uri: The uri to be accessed.
     */
    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    /***
     * Helper to perform a delete operation when the request does contain a payload.
     */
    public HttpDeleteWithBody() {
        super();
    }
}