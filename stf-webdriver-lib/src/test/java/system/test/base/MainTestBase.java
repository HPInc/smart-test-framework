package system.test.base;


import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import org.testng.annotations.*;

public class MainTestBase {

    protected static TestLog testLog;

    @BeforeClass
    @Parameters({"optionalGlobalParameterExample"})
    public void mainTestBaseInitialize(@Optional("") String param1) {
        testLog = new TestLog(true);

        testLog.logIt("Parameter value provided from testRunConfig:" + param1);
    }
}