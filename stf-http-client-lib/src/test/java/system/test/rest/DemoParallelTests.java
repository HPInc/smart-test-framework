package system.test.rest;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;
import system.test.pojo.CommonPayloadBody;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class DemoParallelTests extends HttpClientTestBase {

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
    @Description("A simple rest test.")
    public void simpleTest1() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test.")
    public void simpleTest2() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test.")
    public void simpleTest3() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test.")
    public void simpleTest4() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test.")
    public void simpleTest5() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody.extractBytes())
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }


    private void configureStub(WireMockServer wireMockServer, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .withHeader("stf", containing("qa"))
                .willReturn(aResponse()
                        .withLogNormalRandomDelay(5000, 0.1)
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }

    private String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }
}