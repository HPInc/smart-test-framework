package system.base;


import com.github.hpinc.jeangiacomin.stf.framework.logger.TestLog;
import com.github.hpinc.jeangiacomin.stf.framework.wait.WaitHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
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