package system.test.rest;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Description;
import net.minidev.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import system.test.base.HttpClientTestBase;
import system.test.pojo.BooksStore;


import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class ResponseParsingTests extends HttpClientTestBase {

    byte[] requestBody;
    byte[] responsePayload;
    String endpoint;
    String url;
    HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader("stf", "qa");

    @BeforeClass
    public void initializeMocks() throws IOException {
        requestBody = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("requestBody.json")).getBytes();
        responsePayload = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBooksStore.json")).getBytes();
        endpoint = "/deserialize/some/bookstore2";
        url = baseServerUrl + endpoint;
        configureStub(wireMockServer, responsePayload, endpoint);
    }

    @Test
    @Description("Deserialize response using fasterxml api.")
    public void deserializeToPojoTest1() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        BooksStore booksStore = httpDetailedResponse.deserializeResponseBodyToObject(BooksStore.class, SerializationType.JSON);
        Assert.assertEquals(booksStore.store.book[3].author, "J. R. R. Tolkien");
        Assert.assertEquals(booksStore.store.book[3].title, "The Lord of the Rings");
        Assert.assertEquals(booksStore.store.book[3].isbn, "0-395-19395-8");
        //or
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['isbn']", "0-395-19395-8");
    }


    @Test
    @Description("Deserialize response using jsonPath api.")
    public void readJsonPathFromResponseTest1() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        JSONArray author = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['author']");
        JSONArray title = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['title']");
        JSONArray isbn = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['isbn']");
        Assert.assertEquals(author.get(0), "J. R. R. Tolkien");
        Assert.assertEquals(title.get(0), "The Lord of the Rings");
        Assert.assertEquals(isbn.get(0), "0-395-19395-8");
        //or
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
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