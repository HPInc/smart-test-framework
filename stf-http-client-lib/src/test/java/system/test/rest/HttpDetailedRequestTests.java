package system.test.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedRequest;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.hpinc.jeangiacomin.stf.enums.http.HttpRequestMethod;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;
import system.test.pojo.CommonPayloadBody;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HttpDetailedRequestTests extends HttpClientTestBase {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a HTTP request using HTTP Detailed request object and a string as payload body")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void detailedRequestTest() throws Exception {
        String responseBody = "Hello STF user! Testing PUT with payload!";
        String endpoint = "/put/some/thing";
        String requestBody = "{ \"id\":\"12345\" }";
        configureStubForPutWithBody(wireMockServer, requestBody.getBytes(), responseBody.getBytes(), endpoint);

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.PUT;
        httpDetailedRequest.payloadToBeSent = requestBody.getBytes();


        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(httpDetailedRequest);

        Assert.assertEquals(httpResponse.statusCode, 200);
        Assert.assertEquals(httpResponse.responseBody, responseBody);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a HTTP request using HTTP Detailed request object and a POJO as payload body")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void putRequestWithPayloadUsingObject() throws Exception {

        CommonPayloadBody requestBody = new CommonPayloadBody(
                "123",
                "hi this is a msg from request",
                "abc123");

        CommonPayloadBody responseBody = new CommonPayloadBody(
                "321",
                "hi this is a msg from response",
                "xyz098");

        String endpoint = "/get/some/thing";
        configureStubForPutWithBody(wireMockServer, requestBody.extractBytes(), responseBody.extractBytes(), endpoint);

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.PUT;
        httpDetailedRequest.payloadToBeSent = requestBody.extractBytes();

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(httpDetailedRequest);

        Assert.assertEquals(httpResponse.statusCode, 200);
        Assert.assertTrue(httpResponse.responseBody.contains(responseBody.msg));
    }

    private void configureStubForPutWithBody(WireMockServer wireMockServer, byte[] requestBody, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(put(urlEqualTo(endpoint)).withRequestBody(binaryEqualTo(requestBody))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }
}