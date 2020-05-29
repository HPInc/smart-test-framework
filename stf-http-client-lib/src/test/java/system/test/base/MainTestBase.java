package system.test.base;


import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.wait.WaitHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/************************************************************
 * Smart Test Framework
 *
 * Copyright (c) Hewlett-Packard Company, Aug / 2019
 * All rights are reserved.  Copying or other reproduction of
 * this program except for archival purposes is prohibited
 * without the prior written consent of Hewlett-Packard Company.
 *
 *RESTRICTED RIGHTS LEGEND
 * Use, duplication, or disclosure by the Government
 * is subject to restrictions as set forth in
 * paragraph (b) (3) (B) of the Rights in Technical
 * Data and Computer Software clause in DAR 7-104.9(a).
 *
 * HEWLETT-PACKARD COMPANY
 *
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