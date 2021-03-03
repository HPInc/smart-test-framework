package com.github.jeansantos38.stf.enums.ui.http;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public enum HttpRequestLogLevel {
    LOG_NOTHING,
    LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE,
    LOG_BASIC_INFO_ONLY_FROM_REQUEST_AND_RESPONSE,
    LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_HEADERS,
    LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_PAYLOAD
}