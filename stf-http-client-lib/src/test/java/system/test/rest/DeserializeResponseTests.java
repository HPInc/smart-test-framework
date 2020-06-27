package system.test.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedRequest;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.hpinc.jeangiacomin.stf.enums.http.HttpRequestMethod;
import com.github.hpinc.jeangiacomin.stf.enums.serialization.SerializationType;
import com.github.hpinc.jeangiacomin.stf.framework.io.InputOutputHelper;
import com.github.hpinc.jeangiacomin.stf.framework.serialization.DeserializeHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;
import system.test.pojo.CommonPayloadBody;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class DeserializeResponseTests extends HttpClientTestBase {

    CommonPayloadBody requestBody;
    CommonPayloadBody responseBodyToBeUsedByMock;
    String endpoint;

    @BeforeClass
    public void initializeMocks() throws JsonProcessingException {

        requestBody = new CommonPayloadBody(
                "123",
                "hi this is a msg from request",
                "abc123");

        responseBodyToBeUsedByMock = new CommonPayloadBody(
                "321",
                "hi this is a msg from response",
                "xyz098");

        endpoint = "/deserialize/some/thing";

        configureStub(wireMockServer, responseBodyToBeUsedByMock.extractBytes(), endpoint);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request, then deserialize it into a POJO")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void deserializeUsingPOJO() throws Exception {

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.POST;
        httpDetailedRequest.payloadToBeSent = requestBody.extractBytes();

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(httpDetailedRequest);

        Assert.assertEquals(httpResponse.statusCode, 201);

        CommonPayloadBody deserializedResponse = DeserializeHelper.deserializeStringToObject(CommonPayloadBody.class, SerializationType.JSON, httpResponse.responseBody);

        Assert.assertEquals(deserializedResponse.msg, responseBodyToBeUsedByMock.msg);
        Assert.assertEquals(deserializedResponse.id, responseBodyToBeUsedByMock.id);
        Assert.assertEquals(deserializedResponse.value, responseBodyToBeUsedByMock.value);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request, then deserialize it into a string using JsonPath API")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void deserializeUsingJsonPathParser() throws Exception {

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.POST;
        httpDetailedRequest.payloadToBeSent = requestBody.extractBytes();

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(httpDetailedRequest);

        Assert.assertEquals(httpResponse.statusCode, 201);

        Assert.assertEquals(JsonPath.parse(httpResponse.responseBody).read("$.msg"), responseBodyToBeUsedByMock.msg);
        Assert.assertEquals(JsonPath.parse(httpResponse.responseBody).read("$.id"), responseBodyToBeUsedByMock.id);
        Assert.assertEquals(JsonPath.parse(httpResponse.responseBody).read("$.value"), responseBodyToBeUsedByMock.value);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request reading body content and expected values from files in hard disk")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void readRequestAndResponseFromDisk() throws Exception {

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.POST;
        httpDetailedRequest.payloadToBeSent = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("requestBody.json")).getBytes();

        HttpDetailedResponse httpResponse = httpClient.performHttpRequest(httpDetailedRequest);

        Assert.assertEquals(httpResponse.statusCode, 201);

        String expectedResponseFromDisk = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBody.json"));
        Assert.assertTrue(expectedResponseFromDisk.contains(JsonPath.parse(httpResponse.responseBody).read("$.msg")));
        Assert.assertTrue(expectedResponseFromDisk.contains(JsonPath.parse(httpResponse.responseBody).read("$.id")));
        Assert.assertTrue(expectedResponseFromDisk.contains(JsonPath.parse(httpResponse.responseBody).read("$.value")));
    }

    private void configureStub(WireMockServer wireMockServer, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }

    private String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }

}