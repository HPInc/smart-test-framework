package system.base;

import com.github.jeansantos38.stf.enums.wait.ThreadWait;

import java.io.File;
import java.io.IOException;

public class UiAutomationTestBase extends MainTestBase {

    protected String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }

    protected void startWinDemoApp() throws Exception {
        String stf_win_app_demo = discoverAbsoluteFilePath("bin/SmartTestFrameworkDemo.exe");
        Runtime.getRuntime().exec(String.format("cmd /c %s", stf_win_app_demo));
        waitHelper.wait(ThreadWait.WAIT_3_SEC);
    }

    protected void endWinDemoApp() throws Exception {
        Runtime.getRuntime().exec("taskkill /F /IM \"SmartTestFrameworkDemo.exe\" /T");
        waitHelper.wait(ThreadWait.WAIT_5_SEC);
    }

    protected void manageVm(String msg, String script, String vmManagerBinPath, String vmName, String vmSnapshotName) throws IOException {
        String fullCommand = String.format("cmd /c %s \"%s\" \"%s\" \"%s\"", script, vmManagerBinPath, vmName, vmSnapshotName);
        testLog.logIt(msg + "\n" + fullCommand);
        Runtime.getRuntime().exec(fullCommand);
    }
}