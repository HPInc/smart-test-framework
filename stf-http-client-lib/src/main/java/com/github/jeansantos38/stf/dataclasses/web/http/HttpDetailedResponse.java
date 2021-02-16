package com.github.jeansantos38.stf.dataclasses.web.http;

import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.httpclient.HttpContentHandler;
import com.github.jeansantos38.stf.framework.httpclient.StatusCodeVerifier;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;
import com.github.jeansantos38.stf.framework.serialization.JsonParserHelper;
import com.google.common.base.Stopwatch;
import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpDetailedResponse {

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE = "\n***** REQUEST DETAILS ****\n    [#URL] %s\n    [#METHOD] %s\n    [#REQUEST_HEADERS] %s\n    [#REQUEST_BODY_SIZE_BYTES] %s\n    [#REQUEST_BODY] %s\n \n***** RESPONSE DETAILS ****\n    [#HTTP_STATUS_CODE] %s\n    [#RESPONSE_HEADERS] %s\n    [#ELAPSED TIME] %s ms\n    [#LOCAL_TIMESTAMP] %s\n    [#LOCAL_DATETIME] %s\n    [#THREAD_ID] %s\n    [#RESPONSE_BODY_SIZE_BYTES] %s\n    [#RESPONSE_BODY] %s\n";

    private static final String LOG_BASIC_INFO_ONLY_FROM_REQUEST_AND_RESPONSE = "\n***** REQUEST DETAILS ****\n    [#URL] %s\n    [#METHOD] %s\n    \n***** RESPONSE DETAILS ****\n    [#HTTP_STATUS_CODE] %s\n    [#ELAPSED TIME] %s ms\n    [#LOCAL_TIMESTAMP] %s\n    [#LOCAL_DATETIME] %s\n    [#THREAD_ID] %s\n";

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_HEADERS = "\n***** REQUEST DETAILS ****\n    [#URL] %s\n    [#METHOD] %s\n    [#REQUEST_BODY_SIZE_BYTES] %s\n    [#REQUEST_BODY] %s\n \n***** ## RESPONSE DETAILS ****\n    [#HTTP_STATUS_CODE] %s\n    [#RESPONSE_BODY_SIZE_BYTES] %s\n    [#ELAPSED TIME] %s ms\n    [#LOCAL_TIMESTAMP] %s\n    [#LOCAL_DATETIME] %s\n    [#THREAD_ID] %s\n    [#RESPONSE_BODY] %s\n";

    private static final String LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE_BUT_PAYLOAD = "\n***** REQUEST DETAILS ****\n    [#URL] %s\n    [#METHOD] %s\n    [#REQUEST_HEADERS] %s\n  \n***** ## RESPONSE DETAILS ****\n    [#HTTP_STATUS_CODE] %s\n    [#RESPONSE_HEADERS] %s\n    [#ELAPSED TIME] %s ms\n    [#LOCAL_TIMESTAMP] %s\n    [#LOCAL_DATETIME] %s\n    [#THREAD_ID] %s\n";


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
     * @throws Exception in case of failure while parsing response data.
     */
    public HttpDetailedResponse(HttpResponse response, HttpRequestMethod method, Object headers, String url, String requestBody, Stopwatch elapsedTime, Long localTimestamp) throws Exception {
        boolean gotValidResponse = response != null;

        this.requestHeadersKeysAndValuesPair = extractRequestHeaders(headers);
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
                this.requestPayloadSizeBytes,
                this.requestBody,
                Integer.toString(this.statusCode),
                responseHeadersRaw,
                this.responsePayloadSizeBytes,
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId,
                responseBody);

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
                this.requestPayloadSizeBytes,
                this.requestBody,
                Integer.toString(this.statusCode),
                this.responsePayloadSizeBytes,
                this.elapsedTime.elapsed(TimeUnit.MILLISECONDS),
                this.localTimestamp,
                this.localDateTime,
                this.threadId,
                responseBody);

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

    public HttpDetailedResponse assertExpectedStatusCode(StatusCodeVerifier expectedStatusCodes) throws Exception {
        expectedStatusCodes.verifyStatusCode(this.statusCode, true);
        return this;
    }

    public HttpDetailedResponse assertExpectedStatusCode(int expectedStatusCode) throws Exception {
        new StatusCodeVerifier(expectedStatusCode).verifyStatusCode(this.statusCode, true);
        return this;
    }

    public HttpDetailedResponse assertResponseBodyContainsText(String expectedText) {
        String msg = String.format("The payload response does not contains: %s", expectedText);
        Assert.assertTrue(this.responseBody.contains(expectedText), msg);
        return this;
    }

    public HttpDetailedResponse assertResponseElapsedTimeInMs(int equalOrLessThanMs) {
        Assert.assertTrue(this.elapsedTime.elapsed(TimeUnit.MILLISECONDS) <= equalOrLessThanMs,
                String.format("The request was completed in %s ms, instead %s ms", this.elapsedTime.elapsed(TimeUnit.MILLISECONDS), equalOrLessThanMs));
        return this;
    }

    public HttpDetailedResponse assertJsonPathFromResponse(Operator operator, String jsonPathExpression, String valueToBeAsserted) {
        String match = JsonParserHelper.tryParseJsonPathToString(jsonPathExpression, this.responseBody);
        switch (operator) {
            case CONTAINS:
                Assert.assertTrue(match.contains(valueToBeAsserted), String.format("The title [%s] was not found in the payload!", valueToBeAsserted));
                break;
            case EQUAL:
                Assert.assertEquals(match, valueToBeAsserted);
                break;
        }
        return this;
    }

    public HttpDetailedResponse assertResponseContainsHeader(String key) {
        return assertResponseContainsHeader(key, "");
    }

    public HttpDetailedResponse assertResponseContainsHeader(String key, String value) {
        boolean hasIt = HttpDetailedHeaders.containsHeader(this.responseHeadersKeysAndValuesPair, new HttpDetailedHeader(key, value));
        String errorMsg = value.isEmpty() ? String.format("The header [%s] was not found in response!", key) :
                String.format("The header {%s:%s} was not found in response!", key, value);
        Assert.assertTrue(hasIt, errorMsg);
        return this;
    }

    public JSONArray readJsonPathFromResponse(String jsonPathExpression) {
        return JsonParserHelper.tryReadJsonPath(jsonPathExpression, this.responseBody);
    }

    public <T> T deserializeJsonPathFromResponse(Class<T> tClass, String jsonPathExpression) {
        return JsonParserHelper.deserializeJsonPath(tClass, jsonPathExpression, this.responseBody);
    }

    public <T> T deserializeResponseBodyToObject(Class<T> tClass, SerializationType serializationType) throws Exception {
        return DeserializeHelper.deserializeStringToObject(tClass, serializationType, this.responseBody);
    }

    public enum Operator {
        EQUAL, CONTAINS
    }

    private HttpDetailedHeaders extractRequestHeaders(Object headers) {
        if (headers != null) {
            if (headers.getClass().equals(HashMap.class)) {
                return HttpDetailedHeaders.convertLegacyHeaders((Map<String, String>) headers);
            } else if (headers.getClass().equals(HttpDetailedHeaders.class)) {
                return (HttpDetailedHeaders) headers;
            } else if (headers.getClass().equals(HttpDetailedHeader.class)) {
                HttpDetailedHeader httpDetailedHeader = (HttpDetailedHeader) headers;
                List<HttpDetailedHeader> httpDetailedHeaders = new ArrayList<>();
                httpDetailedHeaders.add(httpDetailedHeader);
                return new HttpDetailedHeaders(httpDetailedHeaders);
            }
        }
        return null;
    }
};