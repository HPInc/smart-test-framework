package system.test.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestBase extends MainTestBase {

    protected Integer wiremockPort = 7654;
    protected WireMockServer wireMockServer;
    protected String baseServerUrl;

    @BeforeClass
    public void initializeMockServer() {
        wireMockServer = new WireMockServer(options().port(wiremockPort));
        wireMockServer.start();
        baseServerUrl = String.format("http://127.0.0.1:%s", wiremockPort);
    }

    @AfterClass
    public void endMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}