package system.test;

import com.github.jeansantos38.stf.framework.desktop.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationUtils;
import com.github.jeansantos38.stf.framework.desktop.UiElement;
import com.github.jeansantos38.stf.framework.desktop.UiVisualFeedback;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;
import org.testng.annotations.*;
import system.base.UiAutomationTestBase;

public class UiAutomationControls extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String navigator;
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
//        navigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.json");
        navigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.yml");
        startVmScript = discoverAbsoluteFilePath("scripts/vBoxStartVmSnapshot.bat");
        endVmScript = discoverAbsoluteFilePath("scripts/vBoxShutdownVmRestoreSnapshot.bat");
        isVncScreen = _isVncScreen;
        vmManagerBinPath = _vmManagerBinPath;
        vmName = _vmName;
        vmSnapshotName = _vmSnapshotName;

        UiVisualFeedback uiVisualFeedback = new UiVisualFeedback("green", "red", 0.2, 0.2);
        uiVisualFeedback.setEnableHighlight(true);

        if (!_isVncScreen) {
            uiAutomationDriver = new UiAutomationDriver(new Screen(), testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, 500, 500, uiVisualFeedback);
            endWinDemoApp();
            startWinDemoApp();
        } else {
            manageVm("Starting test VM!", startVmScript, _vmManagerBinPath, _vmName, _vmSnapshotName);
            VNCScreen screen = UiAutomationUtils.connectToVncScreen(_vncServerIpAddress, _vncServerPort, _vncServerPassword, _connectionTimeoutSec, _operationTimeoutMs, 3);
            uiAutomationDriver = new UiAutomationDriver(screen, testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, uiVisualFeedback);
            //uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "stfIcon").doubleClick();
        }
    }

    @AfterClass
    public void cleanup() throws Exception {
        if (!isVncScreen) {

        } else {
//            manageVm("Ending test VM!", endVmScript, vmManagerBinPath, vmName, vmSnapshotName);
        }
    }

    @Test
    public void demoTest() throws Exception {
        UiElement stfIcon = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "innerIco");
        UiElement checkboxTrue = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "checkbox_true");
        UiElement checkboxFalse = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "checkbox_false");

        UiElement checkbox1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_1");
        checkbox1.click();
        stfIcon.moveCursorOver();
        checkbox1.assertRegionHasPattern(checkboxTrue);

        UiElement checkbox2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_2");
        checkbox2.click();
        stfIcon.moveCursorOver();
        checkbox2.assertRegionHasPattern(checkboxFalse);

    }
}