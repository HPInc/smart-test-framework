package com.github.jeansantos38.stf.framework.httpclient;

import com.github.jeansantos38.stf.dataclasses.web.http.*;
import com.github.jeansantos38.stf.dataclasses.web.proxy.ProxySettings;
import com.github.jeansantos38.stf.enums.http.HttpRequestLogLevel;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.google.common.base.Stopwatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.jeansantos38.stf.framework.httpclient.HttpContentHandler.*;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class HttpClient {

    private static final int DEFAULT_REQUEST_TIMEOUT_SECONDS = 15;
    private static final String SIMPLIFIED_RESPONSE = "\n{***** ##REQUEST DETAILS## ****}\n    [#URL]:%1$s\n    [#METHOD]:%2$s\n    [#REQUEST_HEADERS]:%3$s \n{***** ##RESPONSE DETAILS## ****}\n    [#HTTP_STATUS_CODE]:%4$s\n    [#RESPONSE_HEADERS]:%5$s\n    [#RESPONSE_BODY]:%6$s\n    [#ELAPSED TIME]:%7$s ms\n,   [#LOCAL_TIMESTAMP]:%8$s\n";
    private Stopwatch elapsedTime;
    private long requestLocalTime;
    private ProxySettings proxySettings;
    private TestLog testLog;

    private CredentialsProvider credentialsProvider;
    private RequestConfig requestTimeoutConfig;
    private HttpRequestLogLevel httpRequestLogLevel;

    public HttpRequestLogLevel getHttpRequestLogLevel() {
        return httpRequestLogLevel;
    }

    public void setHttpRequestLogLevel(HttpRequestLogLevel httpRequestLogLevel) {
        this.httpRequestLogLevel = httpRequestLogLevel;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public RequestConfig getRequestTimeoutConfig() {
        return requestTimeoutConfig;
    }

    public void setRequestTimeoutConfig(RequestConfig requestTimeoutConfig) {
        this.requestTimeoutConfig = requestTimeoutConfig;
    }

    /***
     * Class constructor.
     * @param testLog: The logger qa instance for registering helper actions.
     * @param proxySettings: Proxy settings.
     */
    public HttpClient(TestLog testLog, ProxySettings proxySettings) throws Exception {
        this.testLog = testLog;
        if (proxySettings == null) {
            throw new Exception("Proxy settings cannot be null!");
        } else {
            this.proxySettings = proxySettings;
        }
        this.httpRequestLogLevel = HttpRequestLogLevel.LOG_EVERYTHING_FROM_REQUEST_AND_RESPONSE;
    }

    /**
     * Enable capture mode for Fiddler.
     *
     * @param requiredSystemPropertiesForFiddler: Map of key-value pair settings for Fiddler.
     */
    public void enableCaptureModeForSniffer(Map<String, String> requiredSystemPropertiesForFiddler) {
        testLog.logIt("Enabling request capture for Fiddler");
        for (Map.Entry<String, String> entry : requiredSystemPropertiesForFiddler.entrySet()) {
            testLog.logIt(String.format("Setting System Property: [key:]%1$s, [value:]%2$s", entry.getKey(), entry.getValue()));
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Disable fiddler capture.
     *
     * @param requiredSystemPropertiesForFiddler: Map of key-value pair settings to be disabled.
     */
    public void disableCaptureModeForSniffer(Map<String, String> requiredSystemPropertiesForFiddler) {
        testLog.logIt("Disabling fiddler debugger.");
        for (Map.Entry<String, String> entry : requiredSystemPropertiesForFiddler.entrySet()) {
            testLog.logIt(String.format("Clear System Property: [key:]%1$s", entry.getKey()));
            System.clearProperty(entry.getKey());
        }
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url, null, null, null);
    }

    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, byte[] payload) throws Exception {
        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest(method, url, null, payload, null, this.httpRequestLogLevel);
        return performHttpRequest(httpDetailedRequest);
    }

    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers, byte[] payload) throws Exception {
        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest(method, url, headers, payload, null, this.httpRequestLogLevel);
        return performHttpRequest(httpDetailedRequest);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url) throws Exception {
        return performHttpRequest(tClass, method, url, null, null, null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url, null, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(tClass, method, url, null, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url, headers, null, null, null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers) throws Exception {
        return performHttpRequest(tClass, method, url, headers, null, null, null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url,
                headers, null, null, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(tClass, method, url, headers, null, null, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers, HttpEntity requestBody) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url, headers, requestBody, null, null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers, HttpEntity requestBody) throws Exception {
        return performHttpRequest(tClass, method, url, headers, requestBody, null, null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(method, url, headers, requestBody, null, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param credentialsProvider: User credentials settings.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, CredentialsProvider credentialsProvider, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(HttpDetailedResponse.class, method, url, headers, requestBody, credentialsProvider, requestTimeoutConfig);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(tClass, method, url, headers, requestBody, null, requestTimeoutConfig);
    }


    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param credentialsProvider: User credentials settings.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, CredentialsProvider credentialsProvider, RequestConfig requestTimeoutConfig) throws Exception {
        return performHttpRequest(tClass, method, url, headers, requestBody, null, requestTimeoutConfig, getHttpRequestLogLevel(), null);
    }

    /***
     * Helper to perform HTTP Client requests.
     * @param tClass: The response type you need, HttpResponse or HTTPClientResponse.
     * @param method: The request method.
     * @param url: The target url.
     * @param headers: The request headers.
     * @param requestBody: The request body.
     * @param credentialsProvider: User credentials settings.
     * @param <T>: The return class type.
     * @return HttpResponse or HttpClientResponse.
     * @throws Exception
     */
    public <T> T performHttpRequest(Class<T> tClass, HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, CredentialsProvider credentialsProvider, RequestConfig requestTimeoutConfig, HttpRequestLogLevel httpRequestLogLevel, Object statusCodeVerifier) throws Exception {
        HttpContext httpContext = configureHttpContext();
        CloseableHttpClient client = configureHttpClient(credentialsProvider);

        String requestBodyParsed = "";
        if (requestBody != null) {
            requestBodyParsed = extractBodyContent(requestBody);
        }

        if (tClass.equals(HttpDetailedResponse.class)) {
            HttpResponse httpResponse = executeHttpRequest(method, url, headers, requestBody, httpContext, client, requestTimeoutConfig);
            HttpDetailedResponse httpDetailedResponse = new HttpDetailedResponse(httpResponse, method, headers, url, requestBodyParsed, elapsedTime, requestLocalTime);

            logHttpRequest(httpRequestLogLevel, httpDetailedResponse);

            responseValidator(statusCodeVerifier, httpDetailedResponse);

            return (T) httpDetailedResponse;
        } else if (tClass.equals(HttpResponse.class)) {

            HttpResponse httpResponse = executeHttpRequest(method, url, headers, requestBody, httpContext, client, requestTimeoutConfig);

            this.testLog.logIt(String.format(SIMPLIFIED_RESPONSE, url, method.toString(), extractRequestHeadersRaw(headers), Integer.toString(httpResponse.getStatusLine().getStatusCode()), extractResponseHeadersRaw(httpResponse), extractContentFromResponseBodyStream(httpResponse), this.elapsedTime.elapsed(TimeUnit.MILLISECONDS), String.valueOf(this.requestLocalTime)));

            return (T) httpResponse;

        } else {
            throw new Exception(String.format("The type %1$s is not supported!", tClass.toString()));
        }
    }

    /**
     * Perform a HTTP request
     *
     * @param httpDetailedRequest
     * @return
     * @throws Exception
     */
    public HttpDetailedResponse performHttpRequest(HttpDetailedRequest httpDetailedRequest) throws Exception {

        HttpEntity requestBody = null;
        if (httpDetailedRequest.payloadToBeSent != null && httpDetailedRequest.payloadToBeSent.length > 0) {
            requestBody = new ByteArrayEntity(httpDetailedRequest.payloadToBeSent);
        }

        return performHttpRequest(HttpDetailedResponse.class, httpDetailedRequest.method, httpDetailedRequest.url,
                httpDetailedRequest.headers, requestBody, getCredentialsProvider(), getRequestTimeoutConfig(), httpDetailedRequest.logLevel, httpDetailedRequest.statusCodeVerifier);
    }

    /***
     * Helper for downloading files from a given url using FileUtils API.
     * @param url: Content Url.
     * @param destinationFilenamePath: The full filename and its path where the content will be saved.
     * @return The full filename where the content was downloaded.
     * @throws IOException
     */
    public File performDownload(String url, String destinationFilenamePath) throws Exception {
        return performDownload(url, destinationFilenamePath, null, null);
    }

    /***
     * Helper for downloading files from a given url using FileUtils API.
     * @param url: Content Url.
     * @param destinationFilenamePath: The full filename and its path where the content will be saved.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @return The full filename where the content was downloaded.
     * @throws IOException
     */
    public File performDownload(String url, String destinationFilenamePath, RequestConfig requestTimeoutConfig) throws Exception {
        return performDownload(url, destinationFilenamePath, null, requestTimeoutConfig);
    }

    /***
     * Helper for downloading files from a given url using FileUtils API.
     * @param url: Content Url.
     * @param destinationFilenamePath: The full filename and its path where the content will be saved.
     * @param credentialsProvider: Credentials (for proxy).
     * @param requestTimeoutConfig: Request timeout configuration.
     * @return The full filename where the content was downloaded.
     * @throws IOException
     */
    public File performDownload(String url, String destinationFilenamePath, CredentialsProvider credentialsProvider, RequestConfig requestTimeoutConfig) throws Exception {

        HttpContext httpContext = configureHttpContext();
        CloseableHttpClient client = configureHttpClient(credentialsProvider);

        HttpGet httpGet = new HttpGet(url);

        //If there's no configuration, set default value for timeout in order to prevent a test hang.
        if (requestTimeoutConfig == null) {
            requestTimeoutConfig = createRequestConfigTimeout(DEFAULT_REQUEST_TIMEOUT_SECONDS);
        }

        httpGet.setConfig(requestTimeoutConfig);

        File finalFile = null;
        File myFile = new File(destinationFilenamePath);

        try (CloseableHttpResponse response = this.proxySettings.requireProxyAuthentication ? client.execute(httpGet, httpContext) : client.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(myFile)) {
                    entity.writeTo(fileOutputStream);
                    finalFile = myFile;
                }
            }
        }
        return finalFile;
    }

    /**
     * Helper for uploading a file.
     *
     * @param url:                  The destination URL.
     * @param fileToBeUploaded:     The file to be upload.
     * @param filenameForMultipart: The filename to be added in the multipart.
     * @throws Exception
     * @return: Response from upload.
     */
    public HttpResponse performUpload(String url, File fileToBeUploaded, String filenameForMultipart) throws Exception {
        return performUpload(url, fileToBeUploaded, filenameForMultipart, null, null);
    }

    /**
     * Helper for uploading a file.
     *
     * @param url:                  The destination URL.
     * @param fileToBeUploaded:     The file to be upload.
     * @param filenameForMultipart: The filename to be added in the multipart.
     * @param requestTimeoutConfig: Request timeout configuration.
     * @throws Exception
     * @return: Response from upload.
     */
    public HttpResponse performUpload(String url, File fileToBeUploaded, String filenameForMultipart, RequestConfig requestTimeoutConfig) throws Exception {
        return performUpload(url, fileToBeUploaded, filenameForMultipart, null, requestTimeoutConfig);
    }

    /**
     * Helper for uploading a file.
     *
     * @param url:                  The destination URL.
     * @param fileToBeUploaded:     The file to be upload.
     * @param filenameForMultipart: The filename to be added in the multipart.
     * @param credentialsProvider:  Credentials provider (proxy credentials).
     * @param requestTimeoutConfig: Request timeout configuration.
     * @throws Exception
     * @return: Response from upload.
     */
    public HttpResponse performUpload(String url, File fileToBeUploaded, String filenameForMultipart, CredentialsProvider credentialsProvider, RequestConfig requestTimeoutConfig) throws Exception {
        HttpContext httpContext = configureHttpContext();
        CloseableHttpClient client = configureHttpClient(credentialsProvider);

        HttpPost httpPost = new HttpPost(url);

        //If there's no configuration, set default value for timeout in order to prevent a test hang.
        if (requestTimeoutConfig == null) {
            requestTimeoutConfig = createRequestConfigTimeout(DEFAULT_REQUEST_TIMEOUT_SECONDS);
        }
        httpPost.setConfig(requestTimeoutConfig);


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(filenameForMultipart, fileToBeUploaded);
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        HttpResponse response = this.proxySettings.requireProxyAuthentication ? client.execute(httpPost, httpContext) : client.execute(httpPost);
        client.close();
        return response;
    }

    /***
     * Use this method to configure timeouts for Apache HTTP Client
     * @param socketTimeout: Timeout for all current sockets (seconds).
     * @return A request config instance to be used by Apache HTTP Client.
     * @throws Exception
     */
    public RequestConfig createRequestConfigTimeout(Integer socketTimeout) throws Exception {
        return createRequestConfigTimeout(socketTimeout, 0, 0);
    }

    /***
     * Use this method to configure timeouts for Apache HTTP Client
     * @param socketTimeout: Timeout for all current sockets (seconds).
     * @param connectionTimeout: Timeout for waiting a connection (seconds).
     * @param requestTimeoutConfig: Timeout for a http request (seconds).
     * @return A request config instance to be used by Apache HTTP Client.
     * @throws Exception
     */
    public RequestConfig createRequestConfigTimeout(Integer socketTimeout, Integer connectionTimeout, Integer requestTimeoutConfig) throws Exception {

        RequestConfig requestConfig;
        if (connectionTimeout > 0 && requestTimeoutConfig > 0 && socketTimeout > 0) {
            requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout * 1000).setConnectTimeout(connectionTimeout * 1000).setConnectionRequestTimeout(requestTimeoutConfig * 1000).build();

        } else if (socketTimeout > 0) {
            requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout * 1000).build();

        } else {
            throw new Exception("Missing mandatory parameter(s)");
        }
        return requestConfig;
    }

    /***
     * Helper to configure the usage of credentials for proxy - when required.
     * @return HttpContext ready for use.
     */
    private HttpContext configureHttpContext() {
        //If the proxySettings does not allow guest, so configure its credentials.
        HttpContext httpContext = null;
        AuthState authState;

        if (this.proxySettings.requireProxyAuthentication) {
            httpContext = new BasicHttpContext();
            authState = new AuthState();
            authState.update(new BasicScheme(), new UsernamePasswordCredentials(proxySettings.proxyUsername, proxySettings.proxyUserPassword));
            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
        }
        return httpContext;
    }

    /***
     * Helper to configure an HTTP client with all needed information that will be used by other mains helpers.
     * @param credentialsProvider: Credentials.
     * @return And http client ready for use.
     * @throws Exception
     */
    private CloseableHttpClient configureHttpClient(CredentialsProvider credentialsProvider) throws Exception {
        //Http ClientId settings part - Proxy.
        CloseableHttpClient client;
        HttpClientBuilder clientSettings = null;
        if (this.proxySettings.directInternetConnection && !this.proxySettings.requireProxyAuthentication && credentialsProvider == null) {
            client = HttpClients.createDefault();
        } else {
            //Adding proxySettings settings if any.
            if (!this.proxySettings.directInternetConnection) {
                HttpHost mProxy = new HttpHost(proxySettings.proxyAddress, proxySettings.proxyPort);
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(mProxy);
                clientSettings = HttpClients.custom().setRoutePlanner(routePlanner);
            }
            //Adding credentials if any.
            if (credentialsProvider != null) {
                clientSettings = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider);
            }
            client = clientSettings.build();
        }
        return client;
    }

    /***
     * Main helper for performing HTTP requests.
     * @param method: Method to be used.
     * @param url: The target url.
     * @param headers: The headers for the request - if any.


     * @return An HttpClientResponse with detailed information for the request and response.
     * @throws Exception
     */
    private HttpResponse executeHttpRequest(HttpRequestMethod method, String url, Object headers, HttpEntity requestBody, HttpContext httpContext, CloseableHttpClient client, RequestConfig requestTimeoutConfig) throws Exception {
        //This one will be used for measure the time taken to perform the request and retrieve the response.
        Stopwatch requestElapsedTime;
        //This one is for logging the current time when the request was executed.
        Long timestamp;

        //If there's no configuration, set default value for timeout in order to prevent a test hang.
        if (requestTimeoutConfig == null) {
            requestTimeoutConfig = createRequestConfigTimeout(DEFAULT_REQUEST_TIMEOUT_SECONDS);
        }

        boolean hasBodyContent = requestBody != null;
        boolean gotExceptionFromResponse = false;

        //Request assemble and execution part.
        HttpResponse response = null;
        HttpInternalResponse httpInternalResponse;
        switch (method) {
            case GET:
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(requestTimeoutConfig);
                httpGet = (HttpGet) extractHeaders(httpGet, headers);
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpGet, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case POST:
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(requestTimeoutConfig);
                httpPost = (HttpPost) extractHeaders(httpPost, headers);
                if (hasBodyContent) {
                    httpPost.setEntity(requestBody);
                }
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpPost, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case PUT:
                HttpPut httpPut = new HttpPut(url);
                httpPut.setConfig(requestTimeoutConfig);
                httpPut = (HttpPut) extractHeaders(httpPut, headers);
                if (hasBodyContent) {
                    httpPut.setEntity(requestBody);
                }
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpPut, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case DELETE:
                HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(url);
                httpDeleteWithBody.setConfig(requestTimeoutConfig);
                httpDeleteWithBody = (HttpDeleteWithBody) extractHeaders(httpDeleteWithBody, headers);
                if (hasBodyContent) {
                    httpDeleteWithBody.setEntity(requestBody);
                }
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpDeleteWithBody, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case PATCH:
                HttpPatch httpPatch = new HttpPatch(url);
                httpPatch.setConfig(requestTimeoutConfig);
                httpPatch = (HttpPatch) extractHeaders(httpPatch, headers);
                if (hasBodyContent) {
                    httpPatch.setEntity(requestBody);
                }
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpPatch, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case HEAD:
                HttpHead httpHead = new HttpHead(url);
                httpHead.setConfig(requestTimeoutConfig);
                httpHead = (HttpHead) extractHeaders(httpHead, headers);
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpHead, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case OPTIONS:
                HttpOptions httpOptions = new HttpOptions(url);
                httpOptions.setConfig(requestTimeoutConfig);
                httpOptions = (HttpOptions) extractHeaders(httpOptions, headers);
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpOptions, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            case TRACE:
                HttpTrace httpTrace = new HttpTrace(url);
                httpTrace.setConfig(requestTimeoutConfig);
                httpTrace = (HttpTrace) extractHeaders(httpTrace, headers);
                timestamp = System.currentTimeMillis();
                httpInternalResponse = executeRequest(httpTrace, client, httpContext);
                requestElapsedTime = httpInternalResponse.stopwatch;
                response = httpInternalResponse.httpResponse;
                break;

            default:
                throw new Exception(String.format("Method %1$s not implemented!", method.toString()));
        }
        requestLocalTime = timestamp;
        elapsedTime = requestElapsedTime;
        return response;
    }

    private void responseValidator(Object statusCodeVerifier, HttpDetailedResponse httpDetailedResponse) throws Exception {
        if (statusCodeVerifier != null) {
            ((StatusCodeVerifier) statusCodeVerifier).verifyStatusCode(httpDetailedResponse.statusCode);
        }
    }

    private void logHttpRequest(HttpRequestLogLevel httpRequestLogLevel, HttpDetailedResponse httpDetailedResponse) throws Exception {
        RequestsLogger.LogHttpClientRequestInfo(testLog, httpDetailedResponse, httpRequestLogLevel);
    }

    private HttpRequest extractHeaders(HttpRequest httpRequest, Object headers) throws Exception {
        if (headers != null) {
            if (headers.getClass().equals(HashMap.class)) {
                HashMap<String, String> leHeaders = (HashMap<String, String>) headers;
                for (Map.Entry<String, String> entry : leHeaders.entrySet()) {
                    httpRequest.setHeader(entry.getKey(), entry.getValue());
                }
            } else if (headers.getClass().equals(HttpDetailedHeaders.class)) {
                HttpDetailedHeaders httpDetailedHeaders = (HttpDetailedHeaders) headers;
                for (HttpDetailedHeader httpDetailedHeader : httpDetailedHeaders.headerList) {
                    httpRequest.setHeader(httpDetailedHeader.headerKey, httpDetailedHeader.headerValue);
                }
            } else if (headers.getClass().equals(HttpDetailedHeader.class)) {
                HttpDetailedHeader httpDetailedHeader = (HttpDetailedHeader) headers;
                httpRequest.setHeader(httpDetailedHeader.headerKey, httpDetailedHeader.headerValue);
            } else {
                throw new Exception("The only supported headers objects are Hashmasp<String,String> and HttpDetailedHeader(s)! Please verify what you're sending!");
            }
        }

        return httpRequest;
    }

    private HttpInternalResponse executeRequest(HttpRequest httpRequest, CloseableHttpClient client, HttpContext httpContext) {

        Stopwatch requestElapsedTime = Stopwatch.createStarted();
        HttpResponse response = null;
        try {
            response = this.proxySettings.requireProxyAuthentication ? client.execute((HttpUriRequest) httpRequest, httpContext) : client.execute((HttpUriRequest) httpRequest);
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
            testLog.logIt(e.getStackTrace().toString());
        }
        requestElapsedTime.stop();

        return new HttpInternalResponse(requestElapsedTime, response);
    }

    /**
     * Helper to extract body content from requests.
     *
     * @param requestBody
     * @return
     */
    private String extractBodyContent(HttpEntity requestBody) {
        try {
            return InputOutputHelper.convertInputStreamToString(requestBody.getContent());
        } catch (Exception e) {
            this.testLog.logIt(e.getMessage());
            return requestBody.getContentType().getName() + " " + requestBody.getContentType().getValue();
        }
    }
}