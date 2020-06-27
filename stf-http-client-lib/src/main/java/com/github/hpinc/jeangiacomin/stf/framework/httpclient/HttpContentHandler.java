package com.github.hpinc.jeangiacomin.stf.framework.httpclient;

import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedHeaders;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class HttpContentHandler {

    private static final String HEADER_KEY_VALUE_BASE = "%1$s:%2$s%3$s";

    /***
     * Helper to extract header key and value pair.
     * @param allHeaders An array of headers.
     * @return A hash map of headers.
     */
    public static HttpDetailedHeaders extractHeaderKeysAndValues(Header[] allHeaders) {
        HttpDetailedHeaders httpDetailedHeaders = new HttpDetailedHeaders();
        for (Header header : allHeaders) {
            httpDetailedHeaders.headerList.add(new HttpDetailedHeader(header.getName(), header.getValue()));
        }
        return httpDetailedHeaders;
    }

    /***
     * Helper to extract readable information from http response stream.
     * @param response: The http response.
     * @return Its content as string.
     * @throws IOException
     */
    public static String extractContentFromResponseBodyStream(HttpResponse response) throws IOException {
        //This change was required due to long polling features - that might return 204 with no content, crashing the buffer reader.
        if (response.getStatusLine().getStatusCode() != 204) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                //nothing to do here.
            }
        }
        return "";
    }

    /***
     * Extract all headers from a map in a row.
     * @param headers: A list of headers.
     * @return All headers in a ordinary string.
     */
    public static String extractRequestHeadersRaw(Object headers) throws Exception {
        String allHeaders = "";
        if (headers != null) {
            if (headers.getClass().equals(HashMap.class)) {
                HashMap<String, String> headers1 = (HashMap<String, String>) headers;
                //Headers check
                boolean hasHeaders = false;
                {
                    if (headers != null) {
                        if (!headers1.isEmpty()) {
                            hasHeaders = true;
                        }
                    }
                }
                if (hasHeaders) {

                    int counter = 1;
                    for (Map.Entry<String, String> entry : headers1.entrySet()) {
                        allHeaders += String.format(HEADER_KEY_VALUE_BASE, entry.getKey(), entry.getValue(), returnHeaderSeparator(counter, headers1.size()));
                        counter++;
                    }
                    return allHeaders;
                } else {
                    return "";
                }
            } else if (headers.getClass().equals(HttpDetailedHeaders.class)) {

                HttpDetailedHeaders httpDetailedHeaders = (HttpDetailedHeaders) headers;
                int counter = 1;
                for (HttpDetailedHeader httpDetailedHeader : httpDetailedHeaders.headerList) {
                    allHeaders += String.format(HEADER_KEY_VALUE_BASE, httpDetailedHeader.headerKey, httpDetailedHeader.headerValue, returnHeaderSeparator(counter, httpDetailedHeaders.headerList.size()));
                    counter++;
                }
                return allHeaders;
            } else {
                throw new Exception("The only supported headers objects are Hashmasp<String,String> and HttpDetailedHeaders! Please verify what you're sending!");
            }
        }
        return allHeaders;
    }

    /***
     * Extract headers from http response object.
     * @param response: The http response.
     * @return All headers in a row.
     */
    public static String extractResponseHeadersRaw(HttpResponse response) {
        String allHeaders = "";
        int counter = 1;
        Header[] headers = response.getAllHeaders();
        for (Header header : headers) {
            allHeaders += String.format(HEADER_KEY_VALUE_BASE, header.getName(), header.getValue(), returnHeaderSeparator(counter, headers.length));
            counter++;
        }
        return allHeaders;
    }

    /**
     * Simple helper to retrieve the size of an payload in bytes.
     *
     * @param value:   Any string value you want to know its size in bytes.
     * @param charset: The encoding of the value you're trying to measure.
     * @return Its size in bytes.
     * @throws UnsupportedEncodingException
     */
    public static String extractSizeInBytes(String value, String charset) throws UnsupportedEncodingException {
        byte[] byteArray = value.getBytes(charset);
        return String.valueOf(byteArray.length);
    }

    /**
     * Simple helper to provide the correct separator for each header (key-value).
     *
     * @param index
     * @param totalItems
     * @return
     */
    private static String returnHeaderSeparator(int index, int totalItems) {
        if (index < totalItems) {
            return ", ";
        } else {//[index == totalItems] in case we're talking about the last header.
            return "";
        }
    }
}