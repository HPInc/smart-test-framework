package system.test.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedHeaders;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.hpinc.jeangiacomin.stf.enums.http.HttpRequestMethod;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HeadersTests extends HttpClientTestBase {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request using legacy MAP <String , String> headers")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getWithLegacyHeaders() throws Exception {
        //configuring mock
        String endpoint = "/get/some/thing/legacy";
        HttpDetailedHeader httpDetailedResponseHeader1 = new HttpDetailedHeader("superHeaderResponseLegacyKey1", "superHeaderResponseLegacyValue1");
        HttpDetailedHeader httpDetailedResponseHeader2 = new HttpDetailedHeader("superHeaderResponseLegacyKey2", "superHeaderResponseLegacyValue2");
        HttpDetailedHeader httpDetailedResponseHeader3 = new HttpDetailedHeader("superHeaderResponseLegacyKey3", "superHeaderResponseLegacyValue3");
        configureStubForGetWithLegacyHeaders(wireMockServer, endpoint, httpDetailedResponseHeader1, httpDetailedResponseHeader2, httpDetailedResponseHeader3);

        //configuring request headers
        String legacyHeaderKey1 = "superHeaderRequestKey1l";
        String legacyHeaderKey2 = "superHeaderRequestKey2l";
        String legacyHeaderKey3 = "superHeaderRequestKey3l";
        Map<String, String> legacyHeaders = new HashMap<>();
        legacyHeaders.put(legacyHeaderKey1, "superHeaderRequestValue1l");
        legacyHeaders.put(legacyHeaderKey2, "superHeaderRequestValue2l");
        legacyHeaders.put(legacyHeaderKey3, "superHeaderRequestValue3l");

        //executing request
        String url = baseServerUrl + endpoint;
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url, legacyHeaders);

        Assert.assertEquals(httpDetailedResponse.url, url);
        Assert.assertEquals(httpDetailedResponse.statusCode, 200);

        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, legacyHeaderKey1, legacyHeaders.get(legacyHeaderKey1)));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, legacyHeaderKey2, legacyHeaders.get(legacyHeaderKey2)));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, legacyHeaderKey3, legacyHeaders.get(legacyHeaderKey3)));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader1));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader2));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader3));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request using new headers object")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getRequestWithNewHeaders() throws Exception {
        //Configuring mock
        String endpoint = "/get/some/thing/new";
        HttpDetailedHeader httpDetailedResponseHeader1 = new HttpDetailedHeader("superHeaderResponseKey1n", "superHeaderResponseValue1n");
        HttpDetailedHeader httpDetailedResponseHeader2 = new HttpDetailedHeader("superHeaderResponseKey2n", "superHeaderResponseValue2n");
        HttpDetailedHeader httpDetailedResponseHeader3 = new HttpDetailedHeader("superHeaderResponseKey3n", "superHeaderResponseValue3n");
        configureStubForGetRequestWithNewHeaders(wireMockServer, endpoint, httpDetailedResponseHeader1, httpDetailedResponseHeader2, httpDetailedResponseHeader3);

        //configuring http request
        HttpDetailedHeader httpDetailedRequestHeader1 = new HttpDetailedHeader("superHeaderRequestKey1n", "superHeaderRequestValue1n");
        HttpDetailedHeader httpDetailedRequestHeader2 = new HttpDetailedHeader("superHeaderRequestKey2n", "superHeaderRequestValue2n");
        HttpDetailedHeader httpDetailedRequestHeader3 = new HttpDetailedHeader("superHeaderRequestKey3n", "superHeaderRequestValue3n");

        HttpDetailedHeaders httpDetailedHeaders = new HttpDetailedHeaders();
        httpDetailedHeaders.headerList.add(httpDetailedRequestHeader1);
        httpDetailedHeaders.headerList.add(httpDetailedRequestHeader2);
        httpDetailedHeaders.headerList.add(httpDetailedRequestHeader3);

        //executing the request
        String url = baseServerUrl + endpoint;
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url, httpDetailedHeaders);

        Assert.assertEquals(httpDetailedResponse.url, url);
        Assert.assertEquals(httpDetailedResponse.statusCode, 200);
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, httpDetailedRequestHeader1));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, httpDetailedRequestHeader2));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.requestHeadersKeysAndValuesPair, httpDetailedRequestHeader3));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader1));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader2));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader3));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request with no headers specified, check it will be correctly represented on httpDetailedResponse")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getRequestWithoutRequestHeaders() throws Exception {
        String endpoint = "/get/some/thing/noheaders";
        configureStubForGetRequestWithoutRequestHeaders(wireMockServer, endpoint);

        String url = baseServerUrl + endpoint;

        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url);

        Assert.assertEquals(httpDetailedResponse.url, url);
        Assert.assertEquals(httpDetailedResponse.statusCode, 200);
        Assert.assertTrue(httpDetailedResponse.requestHeadersKeysAndValuesPair == null || httpDetailedResponse.requestHeadersKeysAndValuesPair.headerList == null || httpDetailedResponse.requestHeadersKeysAndValuesPair.headerList.size() == 0);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request that will retrieve duplicate headers keys from response. The objective is to make sure new headers object will be capable of handling it properly")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getResponseWithDuplicatedHeaders() throws Exception {
        //Configuring mock
        String endpoint = "/get/some/thing/dupheaders";
        HttpDetailedHeader httpDetailedResponseHeader1 = new HttpDetailedHeader("myresponseheaderKeydup", "abc");
        HttpDetailedHeader httpDetailedResponseHeader2 = new HttpDetailedHeader("myresponseheaderKeydup", "123");
        configureStubForGetResponseWithDuplicatedHeaders(wireMockServer, endpoint, httpDetailedResponseHeader1, httpDetailedResponseHeader2);

        //configuring http request
        String url = baseServerUrl + endpoint;
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url);

        Assert.assertEquals(httpDetailedResponse.url, url);
        Assert.assertEquals(httpDetailedResponse.statusCode, 200);

        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader1));
        Assert.assertTrue(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader2));
        Assert.assertFalse(HttpDetailedHeaders.containsHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, "nonExistentKey", "nonExistentValue"));
        Assert.assertTrue(HttpDetailedHeaders.getFirstMatchingHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader1.headerKey).equals(httpDetailedResponseHeader1.headerValue));
        String headerValue = HttpDetailedHeaders.getFirstMatchingHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader1.headerKey);
        Assert.assertFalse(headerValue.equals(httpDetailedResponseHeader2.headerValue));


        String headerName = HttpDetailedHeaders.discoverHeaderKeyByValue(httpDetailedResponse.responseHeadersKeysAndValuesPair, httpDetailedResponseHeader2.headerValue);
        Assert.assertTrue(headerName.equals(httpDetailedResponseHeader2.headerKey));
        Assert.assertNull(HttpDetailedHeaders.getFirstMatchingHeader(httpDetailedResponse.responseHeadersKeysAndValuesPair, "dontexist"));
        Assert.assertNull(HttpDetailedHeaders.discoverHeaderKeyByValue(httpDetailedResponse.responseHeadersKeysAndValuesPair, "dontexist"));
    }

    private void configureStubForGetWithLegacyHeaders(WireMockServer wireMockServer, String endpoint, HttpDetailedHeader httpDetailedResponseHeader1, HttpDetailedHeader httpDetailedResponseHeader2, HttpDetailedHeader httpDetailedResponseHeader3) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withHeader(httpDetailedResponseHeader1.headerKey, httpDetailedResponseHeader1.headerValue)
                        .withHeader(httpDetailedResponseHeader2.headerKey, httpDetailedResponseHeader2.headerValue)
                        .withHeader(httpDetailedResponseHeader3.headerKey, httpDetailedResponseHeader3.headerValue)
                ));
    }

    private void configureStubForGetRequestWithNewHeaders(WireMockServer wireMockServer, String endpoint, HttpDetailedHeader httpDetailedResponseHeader1, HttpDetailedHeader httpDetailedResponseHeader2, HttpDetailedHeader httpDetailedResponseHeader3) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withHeader(httpDetailedResponseHeader1.headerKey, httpDetailedResponseHeader1.headerValue)
                        .withHeader(httpDetailedResponseHeader2.headerKey, httpDetailedResponseHeader2.headerValue)
                        .withHeader(httpDetailedResponseHeader3.headerKey, httpDetailedResponseHeader3.headerValue)
                ));
    }

    private void configureStubForGetRequestWithoutRequestHeaders(WireMockServer wireMockServer, String endpoint) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()));
    }

    private void configureStubForGetResponseWithDuplicatedHeaders(WireMockServer wireMockServer, String endpoint, HttpDetailedHeader httpDetailedResponseHeader1, HttpDetailedHeader httpDetailedResponseHeader2) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withHeader(httpDetailedResponseHeader1.headerKey, httpDetailedResponseHeader1.headerValue)
                        .withHeader(httpDetailedResponseHeader2.headerKey, httpDetailedResponseHeader2.headerValue)
                ));
    }
}