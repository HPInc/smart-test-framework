package com.github.jeansantos38.stf.dataclasses.web.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpDetailedHeaders {

    public List<HttpDetailedHeader> headerList;

    public HttpDetailedHeaders() {
        this.headerList = new ArrayList<>();
    }

    public HttpDetailedHeaders(List<HttpDetailedHeader> headerList) {
        this.headerList = headerList;
    }

    /**
     * Convert old STF headers into new Version
     *
     * @param headers
     * @return
     */
    public static HttpDetailedHeaders convertLegacyHeaders(Map<String, String> headers) {
        HttpDetailedHeaders httpDetailedHeaders = new HttpDetailedHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpDetailedHeaders.headerList.add(new HttpDetailedHeader(entry.getKey(), entry.getValue()));
        }
        return httpDetailedHeaders;
    }

    /**
     * Helper to check if a detailed header does exist in a list of headers.
     *
     * @param httpDetailedHeaders The headers from Http response
     * @param httpDetailedHeader  The header you're looking for. If you don't provide an expected header value, it will check only if the header key exists.
     * @return
     */
    public static boolean containsHeader(HttpDetailedHeaders httpDetailedHeaders, HttpDetailedHeader httpDetailedHeader) {
        if (httpDetailedHeader.headerValue != null && !httpDetailedHeader.headerValue.isEmpty()) {
            return httpDetailedHeaders.headerList.stream().filter(x -> x.headerKey.equals(httpDetailedHeader.headerKey) && x.headerValue.equals(httpDetailedHeader.headerValue)).findAny().orElse(null) != null;
        } else {
            return httpDetailedHeaders.headerList.stream().filter(x -> x.headerKey.equals(httpDetailedHeader.headerKey)).findAny().orElse(null) != null;
        }
    }

    /**
     * Helper to check if a given header key and value does exist in a list of headers.
     *
     * @param httpDetailedHeaders
     * @param headerKey
     * @param headerValue
     * @return
     */
    public static boolean containsHeader(HttpDetailedHeaders httpDetailedHeaders, String headerKey, String headerValue) {
        HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader(headerKey, headerValue);
        return containsHeader(httpDetailedHeaders, httpDetailedHeader);
    }

    /**
     * It returns the token value for the first matching header key.
     *
     * @param httpDetailedHeaders
     * @param headerKey
     * @return
     * @throws Exception
     */
    public static String getFirstMatchingHeader(HttpDetailedHeaders httpDetailedHeaders, String headerKey) throws Exception {
        return getFirstMatchingHeader(httpDetailedHeaders, true, headerKey);
    }

    /**
     * It returns the header key (name) for the first matching entry with the same provided header value.
     *
     * @param httpDetailedHeaders
     * @param headerValue
     * @return
     * @throws Exception
     */
    public static String discoverHeaderKeyByValue(HttpDetailedHeaders httpDetailedHeaders, String headerValue) throws Exception {
        return getFirstMatchingHeader(httpDetailedHeaders, false, headerValue);
    }

    private static String getFirstMatchingHeader(HttpDetailedHeaders httpDetailedHeaders, boolean isKey, String value) throws Exception {
        HttpDetailedHeader httpDetailedHeader;

        if (isKey) {
            httpDetailedHeader = httpDetailedHeaders.headerList.stream().filter(x -> x.headerKey.equals(value)).findAny().orElse(null);
            return httpDetailedHeader == null ? null : httpDetailedHeader.headerValue;
        } else {
            httpDetailedHeader = httpDetailedHeaders.headerList.stream().filter(x -> x.headerValue.equals(value)).findAny().orElse(null);
            return httpDetailedHeader == null ? null : httpDetailedHeader.headerKey;
        }
    }
}