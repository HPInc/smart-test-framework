package system.test.base;

import hp.inc.jsg.qa.stf.dataclasses.web.proxy.ProxySettings;
import hp.inc.jsg.qa.stf.framework.httpclient.HttpClient;
import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import org.testng.annotations.BeforeClass;

public class HttpClientTestBase extends WireMockTestBase {

    protected HttpClient httpClient;

    @BeforeClass
    public void initializeWebdriver() throws Exception {
        ProxySettings proxySettings = new ProxySettings(true);
        httpClient = new HttpClient(new TestLog(), proxySettings);
    }
}
