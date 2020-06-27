package com.github.hpinc.jeangiacomin.stf.dataclasses.web.http;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpDetailedHeader {
    public String headerKey;
    public String headerValue;

    public HttpDetailedHeader() {
    }

    public HttpDetailedHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }
}
