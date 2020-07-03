package com.github.jeansantos38.stf.framework.datadriven;

import com.github.jeansantos38.stf.framework.webdriver.WebDriverAppiumHelper;
import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import com.github.jeansantos38.stf.dataclasses.datadriven.DataDriven;
import com.github.jeansantos38.stf.dataclasses.datadriven.SmartDataDriven;
import com.github.jeansantos38.stf.dataclasses.datadriven.Statement;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;


import java.util.Arrays;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class WebDriverDataDrivenHelper {

    private TestLog testLog;

    /***
     * Default constructor.
     * @param testLog: LoggerQA class instance.
     */
    public WebDriverDataDrivenHelper(TestLog testLog) {
        this.testLog = testLog;
    }

    /***
     * Helper that executes Appium data driven instruction sets.
     * @param webDriverAppiumHelper
     * @param dataDrivenFileFullPath
     * @param instructionSet
     * @throws Exception
     */
    public void executeDataDriven(WebDriverAppiumHelper webDriverAppiumHelper, String dataDrivenFileFullPath, String instructionSet) throws Exception {
        executeDataDriven(webDriverAppiumHelper, dataDrivenFileFullPath, instructionSet);
    }

    /***
     * Helper that executes Selenium data driven instruction sets.
     * @param webDriverSeleniumHelper
     * @param dataDrivenFileFullPath
     * @param instructionSet
     * @throws Exception
     */
    public void executeDataDriven(WebDriverSeleniumHelper webDriverSeleniumHelper, String dataDrivenFileFullPath, String instructionSet) throws Exception {
        executeDataDriven(null, webDriverSeleniumHelper, dataDrivenFileFullPath, instructionSet);
    }

    /***
     * Main execute data driven helper.
     * @param webDriverAppiumHelper
     * @param webDriverSeleniumHelper
     * @param dataDrivenFileFullPath
     * @param instructionSet
     * @throws Exception
     */
    private void executeDataDriven(WebDriverAppiumHelper webDriverAppiumHelper, WebDriverSeleniumHelper webDriverSeleniumHelper, String dataDrivenFileFullPath, String instructionSet) throws Exception {
        String dataDrivenContent = InputOutputHelper.readContentFromFile(dataDrivenFileFullPath);
        DataDriven dataDriven = DeserializeHelper.deserializeStringToObject(DataDriven.class, SerializationType.JSON, dataDrivenContent);
        SmartDataDriven smartDataDriven = Arrays.stream(dataDriven.SmartDataDriven).filter(x -> x.instructionSet.equals(instructionSet)).findAny().orElse(null);

        if (smartDataDriven == null) {
            throw new Exception(String.format("The provided instruction set [%1$s] does not exist in the given data driven file [%2$s]. Please review it and try again!!!", instructionSet, dataDrivenFileFullPath));
        }

        for (int i = 0; i < smartDataDriven.statements.length; i++) {
            SetWebDriverInstructionSet setWebDriverInstructionSet = null;
            AssertWebDriverInstructionSet assertWebDriverInstructionSet = null;

            boolean isAssert = smartDataDriven.statements[i].statement.purpose.equals("assert");
            Statement statement = smartDataDriven.statements[i].statement;

            testLog.logIt(String.format("Executing data driven for agent [%1$s] with [%2$s] operation!", smartDataDriven.statements[i].statement.agent, isAssert ? "ASSERT" : "SET"));
            switch (smartDataDriven.statements[i].statement.agent) {
                case "appium":
                    if (isAssert) {
                        if (assertWebDriverInstructionSet == null)
                            assertWebDriverInstructionSet = new AssertWebDriverInstructionSet(this.testLog, webDriverAppiumHelper);
                        assertWebDriverInstructionSet.dataDrivenAssert(statement);
                    } else {
                        if (setWebDriverInstructionSet == null)
                            setWebDriverInstructionSet = new SetWebDriverInstructionSet(this.testLog, webDriverAppiumHelper);
                        setWebDriverInstructionSet.dataDrivenSet(statement);
                    }
                    break;

                case "selenium":
                    if (isAssert) {
                        if (assertWebDriverInstructionSet == null)
                            assertWebDriverInstructionSet = new AssertWebDriverInstructionSet(this.testLog, webDriverSeleniumHelper);
                        assertWebDriverInstructionSet.dataDrivenAssert(statement);
                    } else {
                        if (setWebDriverInstructionSet == null)
                            setWebDriverInstructionSet = new SetWebDriverInstructionSet(this.testLog, webDriverSeleniumHelper);
                        setWebDriverInstructionSet.dataDrivenSet(statement);
                    }
                    break;

                default:
                    throw new Exception(String.format("The agent [%1$s] is not supported yet!", smartDataDriven.statements[i].statement.agent));
            }
        }
    }
}