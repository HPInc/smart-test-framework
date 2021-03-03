package system.test;

import com.github.jeansantos38.stf.framework.misc.VisualWarningsHelper;
import com.github.jeansantos38.stf.framework.ui.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.ui.UiAutomationUtils;
import com.github.jeansantos38.stf.framework.ui.UiElement;
import com.github.jeansantos38.stf.framework.ui.UiVisualFeedback;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;
import org.testng.annotations.*;
import system.base.UiAutomationTestBase;

public class UiAutomationGeneric extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String navigator;
    String startVmScript;
    String endVmScript;
    String vmManagerBinPath;
    String vmName;
    String vmSnapshotName;
    boolean isVncScreen;
    boolean popUpDuringTest;


    @Parameters({"_isVncScreen",
            "_vmManagerBinPath",
            "_vmName",
            "_vmSnapshotName",
            "_vncServerIpAddress",
            "_vncServerPort",
            "_vncServerPassword",
            "_connectionTimeoutSec",
            "_operationTimeoutMs",
            "_popUpDuringTest"})
    @BeforeClass
    public void beforeClass(@Optional("false") Boolean _isVncScreen,
                            @Optional("") String _vmManagerBinPath,
                            @Optional("") String _vmName,
                            @Optional("") String _vmSnapshotName,
                            @Optional("") String _vncServerIpAddress,
                            @Optional("5900") Integer _vncServerPort,
                            @Optional("") String _vncServerPassword,
                            @Optional("15") Integer _connectionTimeoutSec,
                            @Optional("3000") Integer _operationTimeoutMs,
                            @Optional("false") Boolean _popUpDuringTest) throws Exception {
        navigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.yml");
        startVmScript = discoverAbsoluteFilePath("scripts/vBoxStartVmSnapshot.bat");
        endVmScript = discoverAbsoluteFilePath("scripts/vBoxShutdownVmRestoreSnapshot.bat");
        isVncScreen = _isVncScreen;
        vmManagerBinPath = _vmManagerBinPath;
        vmName = _vmName;
        vmSnapshotName = _vmSnapshotName;
        popUpDuringTest = _popUpDuringTest;

        UiVisualFeedback uiVisualFeedback = new UiVisualFeedback("green", "red", "blue", 0.2);
        uiVisualFeedback.setEnableHighlight(true);

        if (!_isVncScreen) {
            uiAutomationDriver = new UiAutomationDriver(new Screen(), testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, 500, 500, uiVisualFeedback);
            startWinDemoApp();
        } else {
            manageVm("Starting test VM!", startVmScript, _vmManagerBinPath, _vmName, _vmSnapshotName);
            VNCScreen screen = UiAutomationUtils.connectToVncScreen(_vncServerIpAddress, _vncServerPort, _vncServerPassword, _connectionTimeoutSec, _operationTimeoutMs, 3);
            uiAutomationDriver = new UiAutomationDriver(screen, testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, uiVisualFeedback);
            uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "stfIcon").doubleClick();
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
    public void demoTest() throws Exception {
        String testMsg = "This is a super demo test!";
        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "innerIco").assertVisible(3000);

        UiElement textBox1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox1Region");
        textBox1.clearText();
        textBox1.type(testMsg);

        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_1").click();

        if (popUpDuringTest)
            VisualWarningsHelper.showDialogInfo("Pause!", "Move the form around!", 1);

        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_2").click();

        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "dropdownLst").click();
        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "dropdownLst_item5").click();

        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "rbt_2").click();
        if (popUpDuringTest)
            VisualWarningsHelper.showDialogInfo("Pause!", "Move the form around!", 1);

        UiElement textBox2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox2Region");
        textBox2.clearText();
        String randomMsg = "wow a random value ->  " + RandomValuesHelper.generateRandomAlphanumeric(20);
        textBox2.type(randomMsg);


        UiElement trackbarOpt1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "tbr_value1");
        UiElement trackbarOpt2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "tbr_value3");

        trackbarOpt1.dragAndDrop(trackbarOpt2);

        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "x_close_btn").click();

    }
}