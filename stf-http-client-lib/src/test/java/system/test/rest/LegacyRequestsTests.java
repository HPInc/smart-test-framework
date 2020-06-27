package system.test.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.hpinc.jeangiacomin.stf.enums.http.HttpRequestMethod;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class LegacyRequestsTests extends HttpClientTestBase {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a get on mocked API")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void executeGetRequest() throws Exception {
        String msg = "Hello STF user! Testing GET!";
        String endpoint = "/get/some/thing";

        configureStubForGetRequest(wireMockServer, endpoint, msg);

        String url = baseServerUrl + endpoint;

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url);
        Assert.assertEquals(httpResponse.statusCode, 200);
        Assert.assertTrue(httpResponse.elapsedTime.elapsed(TimeUnit.MILLISECONDS) <= 1000);
        Assert.assertEquals(httpResponse.responseBody, msg);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a put on a non existent URL")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getRequestWrongUrl() throws Exception {
        String msg = "Hello STF user! Testing GET!";
        String endpoint = "/get/some/thing";

        configureStubForGetRequestWrongUrl(wireMockServer, endpoint, msg);

        String url = baseServerUrl + endpoint + "/wrong";
        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url);
        Assert.assertEquals(httpResponse.statusCode, 404);
        Assert.assertTrue(httpResponse.responseBody.contains("Request was not matched"));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform http request using an non supported method")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getRequestWrongMethod() throws Exception {
        String msg = "Hello STF user! Testing Non supported method!";
        String endpoint = "/get/some/thing";

        configureStubForGetRequestWrongMethod(wireMockServer, endpoint, msg);

        String url = baseServerUrl + endpoint;
        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(HttpRequestMethod.PUT, url);
        Assert.assertEquals(httpResponse.statusCode, 405);
        Assert.assertEquals(httpResponse.responseBody, msg);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a put request")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void putRequest() throws Exception {
        String msg = "Hello STF user! Testing PUT!";
        String endpoint = "/put/some/thing";

        configureStubForPutRequest(wireMockServer, endpoint, msg);

        String url = baseServerUrl + endpoint;
        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(HttpRequestMethod.PUT, url);
        Assert.assertEquals(httpResponse.statusCode, 200);
        Assert.assertTrue(httpResponse.elapsedTime.elapsed(TimeUnit.MILLISECONDS) <= 600);
        Assert.assertEquals(httpResponse.responseBody, msg);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a HTTP request that will timeout, check if STF will be capable of handling the failure properly")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void getRequestWithDelayedResponse() throws Exception {
        String endpoint = "/get/some/thing/delayed";

        configureStubForGetRequestWithDelayedResponse(wireMockServer, endpoint);

        String url = baseServerUrl + endpoint;

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(HttpRequestMethod.GET, url);

        Assert.assertEquals(httpResponse.url, url);
        Assert.assertEquals(httpResponse.statusCode, 0);
        Assert.assertNull(httpResponse.httpResponse);
        Assert.assertTrue(httpResponse.elapsedTime.elapsed(TimeUnit.MILLISECONDS) > 10000);
    }

    private void configureStubForGetRequest(WireMockServer wireMockServer, String endpoint, String msg) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(msg)));
    }

    private void configureStubForGetRequestWrongUrl(WireMockServer wireMockServer, String endpoint, String msg) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(msg)));
    }

    private void configureStubForGetRequestWrongMethod(WireMockServer wireMockServer, String endpoint, String msg) {
        wireMockServer.stubFor(put(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(405)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(msg)));
    }

    private void configureStubForPutRequest(WireMockServer wireMockServer, String endpoint, String msg) {
        wireMockServer.stubFor(put(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(msg)));
    }

    private void configureStubForGetRequestWithDelayedResponse(WireMockServer wireMockServer, String endpoint) {
        wireMockServer.stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withFixedDelay(60000)));
    }
}