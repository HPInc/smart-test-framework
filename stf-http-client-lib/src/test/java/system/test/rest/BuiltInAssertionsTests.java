package system.test.rest;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.jeansantos38.stf.enums.http.HttpRequestLogLevel;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;
import system.test.pojo.CommonPayloadBody;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BuiltInAssertionsTests extends HttpClientTestBase {

    CommonPayloadBody requestBody;
    byte[] responseBodyToBeUsedByMock;
    String endpoint;
    String url;
    HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader("stf", "qa");

    @BeforeClass
    public void initializeMocks() throws IOException {

        requestBody = new CommonPayloadBody(
                "123",
                "hi this is a msg from request",
                "abc123");

        responseBodyToBeUsedByMock = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBooksStore.json")).getBytes();
        endpoint = "/deserialize/some/bookstore";
        url = baseServerUrl + endpoint;
        configureStub(wireMockServer, responseBodyToBeUsedByMock, endpoint);
    }

    @Test
    @Description("Happy path test. Using built in assertions!")
    public void builtInAssertionsTest() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(2000)
                .assertResponseContainsHeader("Content-Type", "text/plain")
                .assertResponseContainsHeader("Matched-Stub-Id")
                .assertResponseBodyContainsText("The Lord of the Rings")
                .assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
    }

    @Test
    @Description("Happy path test! A bit more verbose mode.")
    public void builtInAssertionsVariation2Test() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes());
        httpDetailedResponse.assertExpectedStatusCode(200);
        httpDetailedResponse.assertResponseElapsedTimeInMs(2000);
        httpDetailedResponse.assertResponseContainsHeader("Content-Type", "text/plain");
        httpDetailedResponse.assertResponseContainsHeader("Matched-Stub-Id");
        httpDetailedResponse.assertResponseBodyContainsText("The Lord of the Rings");
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
    }

    @Test
    @Description("Not so happy path test!")
    public void builtInAssertionsConstraintsTest() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes());
        try {
            httpDetailedResponse.assertExpectedStatusCode(500);
        } catch (AssertionError e) {
            Assert.assertEquals("The status code 200 didn't match: 500", e.getMessage());
        }
        try {
            httpDetailedResponse.assertResponseElapsedTimeInMs(2);
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().contains("instead 2 ms expected"));
        }
        try {
            httpDetailedResponse.assertResponseContainsHeader("Content-Type", "xml/plain");
        } catch (AssertionError e) {
            Assert.assertEquals("The header {Content-Type:xml/plain} was not found in response! expected [true] but found [false]", e.getMessage());
        }
        try {
            httpDetailedResponse.assertResponseContainsHeader("Matched-Stub-Id-blah");
        } catch (AssertionError e) {
            Assert.assertEquals("The header [Matched-Stub-Id-blah] was not found in response! expected [true] but found [false]", e.getMessage());
        }
        try {
            httpDetailedResponse.assertResponseBodyContainsText("Blah Blah");
        } catch (AssertionError e) {
            Assert.assertEquals("The payload response does not contains: Blah Blah expected [true] but found [false]", e.getMessage());
        }
        try {
            httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][2]['title']", "The Lord of the Rings");
        } catch (AssertionError e) {
            Assert.assertEquals("expected [The Lord of the Rings] but found [Moby Dick]", e.getMessage());
        }
    }

    private void configureStub(WireMockServer wireMockServer, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .withHeader("stf", containing("qa"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }

    private String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }
}