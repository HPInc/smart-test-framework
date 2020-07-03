package system.test.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeaders;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedRequest;
import com.github.jeansantos38.stf.enums.http.HttpRequestLogLevel;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HttpLoggerTests extends HttpClientTestBase {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of easily creating HTTP request to REST APIs")
    @Description("Perform a http request using new headers object")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void testingLogger() throws Exception {
        //Configuring mock
        String endpoint = "/put/some/thing/new";
        configureStubForGetRequestWithNewHeaders(wireMockServer, endpoint);

        //configuring http request
        HttpDetailedHeader httpDetailedRequestHeader1 = new HttpDetailedHeader("request -> super secret header key", "request -> super secret request value");
        HttpDetailedHeaders httpDetailedHeaders = new HttpDetailedHeaders();
        httpDetailedHeaders.headerList.add(httpDetailedRequestHeader1);

        HttpDetailedRequest httpDetailedRequest = new HttpDetailedRequest();
        httpDetailedRequest.headers = httpDetailedHeaders;
        httpDetailedRequest.url = baseServerUrl + endpoint;
        httpDetailedRequest.method = HttpRequestMethod.PUT;
        httpDetailedRequest.payloadToBeSent = "{ super secret request body content}".getBytes();

        //executing the request
        for (HttpRequestLogLevel httpRequestLogLevel : HttpRequestLogLevel.values()) {
            testLog.logIt(String.format("\n\n###### Start of log check for  %s", httpRequestLogLevel));
            testLog.logIt(String.format("\n\nThis is the log result from %s", httpRequestLogLevel));
            httpDetailedRequest.logLevel = httpRequestLogLevel;
            httpClient.performHttpRequest(httpDetailedRequest);
            testLog.logIt(String.format("\n\n###### End of log check for  %s", httpRequestLogLevel));
        }
    }

    private void configureStubForGetRequestWithNewHeaders(WireMockServer wireMockServer, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withHeader("response -> super secret header key", "response -> super secret header value")
                        .withBody("{top secret response body content!}")
                ));
    }
}