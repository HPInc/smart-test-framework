package system.test;

import com.github.jeansantos38.stf.enums.wait.ThreadWait;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationUtils;
import org.sikuli.vnc.VNCScreen;
import org.testng.annotations.*;
import system.base.UiAutomationTestBase;

public class UiAutomationVnc extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String stfIconInRemotePC;
    String stfNavigator;


    @BeforeClass
    public void beforeClass() throws Exception {
        //Start the test VM
        Runtime.getRuntime().exec(String.format("cmd /c %s", "C:\\Users\\giacomin\\zicaStart.bat"));

        stfIconInRemotePC = discoverAbsoluteFilePath("masters/winApp/stfShortcutIcon.png");
        stfNavigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.json");

        VNCScreen screen = UiAutomationUtils.connectToVncScreen("192.168.0.46", 5900, "Abc123*", 15, 3000, 3);
        uiAutomationDriver = new UiAutomationDriver(screen, testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true);
    }

    @AfterClass
    public void done() throws Exception {
        testLog.logIt("Closing the app");
        Runtime.getRuntime().exec(String.format("cmd /c %s", "C:\\Users\\giacomin\\zicaEnd.bat"));
    }

    @AfterMethod
    public void runAfterEachTest() throws Exception {
        testLog.logIt("Closing the app");

    }

    @BeforeMethod
    public void runBeforeEachTest() throws Exception {
        testLog.logIt("Open the app in the remote computer");
        uiAutomationDriver.buildPattern(stfIconInRemotePC).doubleClick();
        waitHelper.wait(ThreadWait.WAIT_3_SEC);
    }


    @Test
    public void radioButtonTest() throws Exception {

        uiAutomationDriver.type("UHUM SEI");
        uiAutomationDriver.takeScreenshot();
        uiAutomationDriver.buildPatternFromNavigator(stfNavigator, "mainScreen", "x_close_btn").click();

    }

//    @Test
//    public void radioButtonTest() throws Exception {
//        UiElement radioBtn2Unchecked = uiAutomationDriver.buildPattern(radioBtn2ImgUnchecked, 0.99);
//        radioBtn2Unchecked.assertVisible();
//        radioBtn2Unchecked.assertVisible(2000);
//        radioBtn2Unchecked.click();
//        uiAutomationDriver.buildPattern(stfIcoImg).moveCursorOver();
//        /*
//        Once clicked the original pattern won't exist anymore, since the original img is not selected.
//        */
//        radioBtn2Unchecked.assertNotVisible(2000);
//        radioBtn2Unchecked.assertNotVisible();
//        uiAutomationDriver.buildPattern(radioBtn2ImgChecked, 0.99).assertVisible();
//    }

//    @Test
//    public void textBoxTest1() throws Exception {
//        UiElement textBox2 = uiAutomationDriver.buildPattern(textBoxDefaultImg, 0.95);
//        textBox2.assertVisible(2000);
//        textBox2.doubleClick();
//        textBox2.type("this is a test");
//        textBox2.type(Keys.TAB);
//        uiAutomationDriver.buildPattern(textBoxFilledImg, 0.92).assertVisible(2000);
//        textBox2.assertNotVisible();
//    }

//    @Test
//    public void checkNonExistentPattern() throws IOException, FindFailed {
//        UiElement uiElement = uiAutomationDriver.buildPattern(textBoxFilledImg);
//        try {
//            uiElement.assertVisible(1000);
//        } catch (AssertionError a) {
//            Assert.assertTrue(a.getMessage().contains(uiElement.NOT_VISIBLE_ASSERT_MSG));
//        }
//    }
}