package com.github.jeansantos38.stf.framework.datadriven;


import com.github.jeansantos38.stf.framework.desktop.DesktopAutomationHelper;
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
public class DesktopAutomationDataDrivenHelper {

    private TestLog testLog;

    /***
     * Default constructor.
     * @param testLog: LoggerQA class instance.
     */
    public DesktopAutomationDataDrivenHelper(TestLog testLog) {
        this.testLog = testLog;
    }

    /***
     * Main execute data driven helper.
     * @param desktopAutomationHelper: Image Recognition Helper instance.
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param dataDrivenFileFullPath
     * @param instructionSet
     * @throws Exception
     */
    public void executeDataDriven(DesktopAutomationHelper desktopAutomationHelper, String navigatorFileFullPath, String dataDrivenFileFullPath, String instructionSet) throws Exception {
        String dataDrivenContent = InputOutputHelper.readContentFromFile(dataDrivenFileFullPath);
        DataDriven dataDriven = DeserializeHelper.deserializeStringToObject(DataDriven.class, SerializationType.JSON, dataDrivenContent);
        SmartDataDriven smartDataDriven = Arrays.stream(dataDriven.SmartDataDriven).filter(x -> x.instructionSet.equals(instructionSet)).findAny().orElse(null);

        if (smartDataDriven == null) {
            throw new Exception(String.format("The provided instruction set [%1$s] does not exist in the given data driven file [%2$s]. Please review it and try again!!!", instructionSet, dataDrivenFileFullPath));
        }

        for (int i = 0; i < smartDataDriven.statements.length; i++) {
            AssertDesktopAutomationInstructionSet assertDesktopAutomationInstructionSet = null;
            SetDesktopAutomationInstructionSet setDesktopAutomationInstructionSet = null;

            boolean isAssert = smartDataDriven.statements[i].statement.purpose.equals("assert");
            Statement statement = smartDataDriven.statements[i].statement;

            testLog.logIt(String.format("Executing data driven for agent [%1$s] with [%2$s] operation!", smartDataDriven.statements[i].statement.agent, isAssert ? "ASSERT" : "SET"));
            switch (smartDataDriven.statements[i].statement.agent) {
                case "sikuli":
                    if (isAssert) {
                        if (assertDesktopAutomationInstructionSet == null)
                            assertDesktopAutomationInstructionSet = new AssertDesktopAutomationInstructionSet(this.testLog, desktopAutomationHelper);
                        assertDesktopAutomationInstructionSet.assertImageDataDriven(navigatorFileFullPath, statement);
                    } else {
                        if (setDesktopAutomationInstructionSet == null)
                            setDesktopAutomationInstructionSet = new SetDesktopAutomationInstructionSet(this.testLog, desktopAutomationHelper);
                        setDesktopAutomationInstructionSet.setImageDataDriven(navigatorFileFullPath, statement);
                    }
                    break;
                default:
                    throw new Exception(String.format("The agent [%1$s] is not supported yet!", smartDataDriven.statements[i].statement.agent));
            }
        }
    }
}