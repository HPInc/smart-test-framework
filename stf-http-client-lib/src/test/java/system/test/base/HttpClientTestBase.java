package system.test.base;

import com.github.hpinc.jeangiacomin.stf.dataclasses.web.proxy.ProxySettings;
import com.github.hpinc.jeangiacomin.stf.framework.httpclient.HttpClient;
import com.github.hpinc.jeangiacomin.stf.framework.logger.TestLog;
import org.testng.annotations.BeforeClass;

public class HttpClientTestBase extends WireMockTestBase {

    protected HttpClient httpClient;

    @BeforeClass
    public void initializeWebdriver() throws Exception {
        ProxySettings proxySettings = new ProxySettings(true);
        httpClient = new HttpClient(new TestLog(), proxySettings);
    }
}
