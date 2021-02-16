package com.github.jeansantos38.stf.dataclasses.web.http;

import com.github.jeansantos38.stf.enums.http.HttpRequestLogLevel;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.httpclient.StatusCodeVerifier;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpDetailedRequest {

    public HttpRequestMethod method;
    public String url;
    public Object headers;
    public byte[] payloadToBeSent;
    public StatusCodeVerifier statusCodeVerifier;
    public HttpRequestLogLevel logLevel;

    public HttpDetailedRequest() {
        this(null, null, null, null, null, HttpRequestLogLevel.LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE);
    }

    public HttpDetailedRequest(HttpRequestMethod method, String url, Object headers, byte[] payloadToBeSent, StatusCodeVerifier statusVerifier) {
        this(method, url, headers, payloadToBeSent, statusVerifier, HttpRequestLogLevel.LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE);
    }

    public HttpDetailedRequest(HttpRequestMethod method, String url, Object headers, byte[] payloadToBeSent, StatusCodeVerifier statusVerifier, HttpRequestLogLevel logLevel) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.payloadToBeSent = payloadToBeSent;
        this.statusCodeVerifier = statusVerifier;
        this.logLevel = logLevel;
    }
}