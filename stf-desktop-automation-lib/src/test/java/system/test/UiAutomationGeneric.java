package system.test;

import com.github.jeansantos38.stf.enums.wait.ThreadWait;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationUtils;
import com.github.jeansantos38.stf.framework.desktop.UiElement;
import com.github.jeansantos38.stf.framework.desktop.UiVisualFeedback;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;
import org.testng.annotations.*;
import system.base.UiAutomationTestBase;

public class UiAutomationGeneric extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String stfNavigator;
    String startVmScript;
    String endVmScript;
    String vmManagerBinPath;
    String vmName;
    String vmSnapshotName;
    boolean isVncScreen;

    @Parameters({"_isVncScreen",
            "_vmManagerBinPath",
            "_vmName",
            "_vmSnapshotName",
            "_vncServerIpAddress",
            "_vncServerPort",
            "_vncServerPassword",
            "_connectionTimeoutSec",
            "_operationTimeoutMs"})
    @BeforeClass
    public void beforeClass(@Optional("false") Boolean _isVncScreen,
                            @Optional("") String _vmManagerBinPath,
                            @Optional("") String _vmName,
                            @Optional("") String _vmSnapshotName,
                            @Optional("") String _vncServerIpAddress,
                            @Optional("5900") Integer _vncServerPort,
                            @Optional("") String _vncServerPassword,
                            @Optional("15") Integer _connectionTimeoutSec,
                            @Optional("3000") Integer _operationTimeoutMs) throws Exception {
        stfNavigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.json");
        startVmScript = discoverAbsoluteFilePath("scripts/vBoxStartVmSnapshot.bat");
        endVmScript = discoverAbsoluteFilePath("scripts/vBoxShutdownVmRestoreSnapshot.bat");
        isVncScreen = _isVncScreen;
        vmManagerBinPath = _vmManagerBinPath;
        vmName = _vmName;
        vmSnapshotName = _vmSnapshotName;

        UiVisualFeedback uiVisualFeedback = new UiVisualFeedback("green", "red", 0.2, 0.2);
        uiVisualFeedback.setEnableHighlight(true);

        if (!_isVncScreen) {
            uiAutomationDriver = new UiAutomationDriver(new Screen(), testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, 50, 50, uiVisualFeedback);
            startWinDemoApp();
        } else {
            manageVm("Starting test VM!", startVmScript, _vmManagerBinPath, _vmName, _vmSnapshotName);
            VNCScreen screen = UiAutomationUtils.connectToVncScreen(_vncServerIpAddress, _vncServerPort, _vncServerPassword, _connectionTimeoutSec, _operationTimeoutMs, 3);
            uiAutomationDriver = new UiAutomationDriver(screen, testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, uiVisualFeedback);
            uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "desktop", "stfIcon").doubleClick();
        }
    }

    @AfterClass
    public void cleanup() throws Exception {
        if (!isVncScreen) {
            endWinDemoApp();
        } else {
            manageVm("Ending test VM!", endVmScript, vmManagerBinPath, vmName, vmSnapshotName);
        }
    }

    @Test
    public void radioButtonTest() throws Exception {
        waitHelper.wait(ThreadWait.WAIT_1_SEC);
        testLog.logIt("abc123xx");
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "innerIco").assertVisible(3000);
        uiAutomationDriver.type("UHUM SEI");
        String something = uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "textBox1Region").getTextViaOCR();
        testLog.logIt("abc123");
        testLog.logIt(String.format("You've typed: \n %s", something));
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "cbx_2").click();
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "rbt_2").click();
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "dropdownLst").click();
        waitHelper.wait(ThreadWait.WAIT_1_SEC);
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "dropdownLst_item1").click();
        testLog.logIt("fimasassa");
        waitHelper.wait(ThreadWait.WAIT_1_SEC);

        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "x_close_btn").click();
        testLog.logIt("fim");

    }

//    @Test
//    public void radioButtonTest2() throws Exception {
//        waitHelper.wait(ThreadWait.WAIT_1_SEC);
//        testLog.logIt("abc123xx");
//        UiElement uiElement = uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "innerIco");
//        uiElement.assertNotVisible(3000);
//
//
//
//        uiAutomationDriver.type("UHUM SEI");
//        String something = uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "textBox1Region").getTextViaOCR();
//        testLog.logIt("abc123");
//        testLog.logIt(String.format("You've typed: \n %s", something));
//        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "cbx_2").click();
//        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "rbt_2").click();
//        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "dropdownLst").click();
//        waitHelper.wait(ThreadWait.WAIT_1_SEC);
//        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "dropdownLst_item1").click();
//        testLog.logIt("fimasassa");
//        waitHelper.wait(ThreadWait.WAIT_1_SEC);
//
//        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "x_close_btn").click();
//        testLog.logIt("fim");
//
  //  }
}