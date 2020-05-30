package system.test.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.net.SocketException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestBase extends MainTestBase {

    protected WireMockServer wireMockServer;
    protected String uiTestsBaseUrl;

    @Parameters({
            "param_WiremockExternalIP",
            "param_WiremockPort",
            "param_WiremockUiEndpoint"
    })
    @BeforeClass
    public void initializeMockServer(
            String param_WiremockExternalIP,
            Integer param_WiremockPort,
            String param_WiremockUiEndpoint) throws SocketException {
        wireMockServer = new WireMockServer(options().port(param_WiremockPort));
        wireMockServer.start();

        uiTestsBaseUrl = String.format("http://%s:%s%s", param_WiremockExternalIP, param_WiremockPort, param_WiremockUiEndpoint);

        mockHtmlPage(wireMockServer, param_WiremockUiEndpoint + "/form");
        mockHtmlPageWithTable(wireMockServer, param_WiremockUiEndpoint + "/table");
    }

    @AfterClass
    public void endMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    private void mockHtmlPage(WireMockServer wireMockServer, String endpoint) {
        wireMockServer.stubFor(
                get(urlEqualTo(endpoint))
                        .willReturn(aResponse().withBodyFile("stf_demo_form.html")));
    }

    private void mockHtmlPageWithTable(WireMockServer wireMockServer, String endpoint) {
        wireMockServer.stubFor(
                get(urlEqualTo(endpoint))
                        .willReturn(aResponse().withBodyFile("table.html")));
    }
}