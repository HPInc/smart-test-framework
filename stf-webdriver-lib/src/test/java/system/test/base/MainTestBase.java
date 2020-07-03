package system.test.base;


import com.github.jeansantos38.stf.framework.logger.TestLog;
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