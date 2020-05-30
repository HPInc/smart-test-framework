package system.test.base;


import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.wait.WaitHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class MainTestBase {

    protected static TestLog testLog;
    protected WaitHelper waitHelper;


    @BeforeClass
    @Parameters({"optionalGlobalParameterExample"})
    public void mainTestBaseInitialize(@Optional("") String param1) throws Exception {
        testLog = new TestLog(true);
        testLog.logIt("Parameter value provided from testRunConfig:" + param1);
        waitHelper = new WaitHelper();
    }
}