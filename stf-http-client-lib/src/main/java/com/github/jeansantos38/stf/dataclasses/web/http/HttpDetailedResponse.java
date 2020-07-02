package com.github.jeansantos38.stf.dataclasses.web.http;

import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.httpclient.HttpContentHandler;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.google.common.base.Stopwatch;
import org.apache.http.HttpResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpDetailedResponse {

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE = "\n{***** ##REQUEST DETAILS## ****}\n    [#URL:]%1$s\n    [#METHOD:]%2$s\n    [#REQUEST_HEADERS:]%3$s\n    [#REQUEST_BODY:]%4$s\n    [#REQUEST_BODY_SIZE_BYTES:]%5$s\n \n{***** ##RESPONSE DETAILS## ****}\n    [#HTTP_STATUS_CODE:]%6$s\n    [#RESPONSE_HEADERS:]%7$s\n    [#RESPONSE_BODY:]%8$s\n    [#RESPONSE_BODY_SIZE_BYTES:]%9$s\n    [#ELAPSED TIME:]%10$s ms\n    [#LOCAL_TIMESTAMP:]%11$s\n    [#LOCAL_DATETIME:]%12$s\n    [#THREAD_ID:]%13$s\n";

    private static final String LOG_BASIC_INFO_ONLY_FROM_REQUEST_AND_RESPONSE = "\n{***** ##REQUEST DETAILS## ****}\n    [#URL:]%s\n    [#METHOD:]%s\n    \n{***** ##RESPONSE DETAILS## ****}\n    [#HTTP_STATUS_CODE:]%s\n    [#ELAPSED TIME:]%s ms\n    [#LOCAL_TIMESTAMP:]%s\n    [#LOCAL_DATETIME:]%s\n    [#THREAD_ID:]%s\n";

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_HEADERS = "\n{***** ##REQUEST DETAILS## ****}\n    [#URL:]%s\n    [#METHOD:]%s\n    [#REQUEST_BODY:]%s\n    [#REQUEST_BODY_SIZE_BYTES:]%s\n \n{***** ##RESPONSE DETAILS## ****}\n    [#HTTP_STATUS_CODE:]%s\n    [#RESPONSE_BODY:]%s\n    [#RESPONSE_BODY_SIZE_BYTES:]%s\n    [#ELAPSED TIME:]%s ms\n    [#LOCAL_TIMESTAMP:]%s\n    [#LOCAL_DATETIME:]%s\n    [#THREAD_ID:]%s\n";

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_PAYLOAD = "\n{***** ##REQUEST DETAILS## ****}\n    [#URL:]%s\n    [#METHOD:]%s\n    [#REQUEST_HEADERS:]%s\n  \n{***** ##RESPONSE DETAILS## ****}\n    [#HTTP_STATUS_CODE:]%s\n    [#RESPONSE_HEADERS:]%s\n    [#ELAPSED TIME:]%s ms\n    [#LOCAL_TIMESTAMP:]%s\n    [#LOCAL_DATETIME:]%s\n    [#THREAD_ID:]%s\n";


    public HttpResponse httpResponse;
    public String requestHeadersRaw;
    public String responseHeadersRaw;
    public String url;
    public String responseBody;
    public String requestBody;
    public HttpRequestMethod method;
    public Stopwatch elapsedTime;
    public HttpDetailedHeaders responseHeadersKeysAndValuesPair;
    public HttpDetailedHeaders requestHeadersKeysAndValuesPair;
    public String localTimestamp;
    public String localDateTime;
    public String requestPayloadSizeBytes;
    public String responsePayloadSizeBytes;
    public String threadId;
    public int statusCode;

    //The ones used for logging
    public String fullTransactionContentRaw;
    String logBasicInfoOnlyFromRequestAndResponse;
    String logEverythingButHeaders;
    String logEverythingButPayload;


    public String getLogEverythingButPayload() {
        return this.logEverythingButPayload;
    }

    public String getLogBasicInfoOnlyFromRequestAndResponse() {
        return this.logBasicInfoOnlyFromRequestAndResponse;
    }

    public String getLogEverythingButHeaders() {
        return this.logEverythingButHeaders;
    }

    /***
     *  Main constructor, responsible for compiling all relevant data from a previous HTTP request.
     * @param response: The response for the previous http request.
     * @param method:: The method used.
     * @param headers The request headers.
     * @param url: The target url.
     * @param requestBody: The request body content.
     * @param elapsedTime: The elapsed time for performing the request and retrieving the response.
     * @param localTimestamp The local timestamp when the request was executed.
     * @throws Exception
     */
    public HttpDetailedResponse(HttpResponse response, HttpRequestMethod method, Object headers, String url, String requestBody, Stopwatch elapsedTime, Long localTimestamp) throws Exception {
        boolean gotValidResponse = response != null;

        this.requestHeadersKeysAndValuesPair = headers != null ? headers.getClass().equals(HttpDetailedHeaders.class) ? (HttpDetailedHeaders) headers : HttpDetailedHeaders.convertLegacyHeaders((Map<String, String>) headers) : null;
        this.httpResponse = response;
        this.url = url;
        this.requestHeadersRaw = HttpContentHandler.extractRequestHeadersRaw(headers);


        this.threadId = String.valueOf(Thread.currentThread().getId());
        this.requestBody = requestBody;

        if (this.requestBody != null) {
            this.requestPayloadSizeBytes = HttpContentHandler.extractSizeInBytes(this.requestBody, "UTF-8");
        }
        this.method = method;
        this.elapsedTime = elapsedTime;
        this.localTimestamp = String.valueOf(localTimestamp);
        this.localDateTime = CalendarHelper.convertTimestampToDate(localTimestamp);

        if (gotValidResponse) {
            this.responseHeadersKeysAndValuesPair = HttpContentHandler.extractHeaderKeysAndValues(response.getAllHeaders());
            this.statusCode = response.getStatusLine().getStatusCode();
            this.responseHeadersRaw = HttpContentHandler.extractResponseHeadersRaw(response);
            this.responseBody = HttpContentHandler.extractContentFromResponseBodyStream(response);
            this.responsePayloadSizeBytes = HttpContentHandler.extractSizeInBytes(this.responseBody, "UTF-8");
        }

        this.fullTransactionContentRaw = String.format(
                LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE,
                url,
                this.method.toString(),
                requestHeadersRaw,
                this.requestBody,
                this.requestPayloadSizeBytes,
                Integer.toString(this.statusCode),
                responseHeadersRaw,
                responseBody,
                this.responsePayloadSizeBytes,
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId);

        this.logBasicInfoOnlyFromRequestAndResponse = String.format(
                LOG_BASIC_INFO_ONLY_FROM_REQUEST_AND_RESPONSE,
                url,
                this.method.toString(),
                Integer.toString(this.statusCode),
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId);

        this.logEverythingButHeaders = String.format(
                LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_HEADERS,
                url,
                this.method.toString(),
                this.requestBody,
                this.requestPayloadSizeBytes,
                Integer.toString(this.statusCode),
                responseBody,
                this.responsePayloadSizeBytes,
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId);

        this.logEverythingButPayload = String.format(
                LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_PAYLOAD,
                url,
                this.method.toString(),
                requestHeadersRaw,
                Integer.toString(this.statusCode),
                responseHeadersRaw,
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId);
    }
}