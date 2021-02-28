package system.test;

import com.github.jeansantos38.stf.framework.desktop.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.desktop.UiElement;
import org.sikuli.hotkey.Keys;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import system.base.UiAutomationTestBase;

import java.io.IOException;

public class UiAutomation extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String radioBtn2ImgUnchecked;
    String textBoxFilledImg;
    String radioBtn2ImgChecked;
    String textBoxDefaultImg;
    String stfIcoImg;

    @BeforeClass
    public void beforeClass() throws Exception {
        Screen screen = new Screen();
        uiAutomationDriver = new UiAutomationDriver(screen, testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true);
        radioBtn2ImgUnchecked = discoverAbsoluteFilePath("masters/winApp/radioButton2_unchecked.png");
        radioBtn2ImgChecked = discoverAbsoluteFilePath("masters/winApp/radioButton2_checked.png");
        textBoxFilledImg = discoverAbsoluteFilePath("masters/winApp/textBox2_filled.png");
        textBoxDefaultImg = discoverAbsoluteFilePath("masters/winApp/textBox2_default.png");
        stfIcoImg = discoverAbsoluteFilePath("masters/winApp/stf_ico.png");
    }

    @AfterMethod
    public void runAfterEachTest() throws Exception {
        testLog.logIt("Closing the app");
        endWinDemoApp();
    }

    @BeforeMethod
    public void runBeforeEachTest() throws Exception {
        testLog.logIt("Open the app");
        startWinDemoApp();
    }

    @Test
    public void radioButtonTest() throws Exception {
        UiElement radioBtn2Unchecked = uiAutomationDriver.buildPattern(radioBtn2ImgUnchecked, 0.99);
        radioBtn2Unchecked.assertVisible();
        radioBtn2Unchecked.assertVisible(2000);
        radioBtn2Unchecked.click();
        uiAutomationDriver.buildPattern(stfIcoImg).moveCursorOver();
        /*
        Once clicked the original pattern won't exist anymore, since the original img is not selected.
        */
        radioBtn2Unchecked.assertNotVisible(2000);
        radioBtn2Unchecked.assertNotVisible();
        uiAutomationDriver.buildPattern(radioBtn2ImgChecked, 0.99).assertVisible();
    }

    @Test
    public void textBoxTest1() throws Exception {
        UiElement textBox2 = uiAutomationDriver.buildPattern(textBoxDefaultImg, 0.95);
        textBox2.assertVisible(2000);
        textBox2.doubleClick();
        textBox2.paste("this is a test");
        uiAutomationDriver.type(Keys.TAB);
        uiAutomationDriver.buildPattern(textBoxFilledImg, 0.92).assertVisible(2000);
        textBox2.assertNotVisible();
    }

    @Test
    public void checkNonExistentPattern() throws IOException, FindFailed {
        UiElement uiElement = uiAutomationDriver.buildPattern(textBoxFilledImg);
        try {
            uiElement.assertVisible(1000);
        } catch (AssertionError a) {
            Assert.assertTrue(a.getMessage().contains(uiElement.get_NOT_VISIBLE_ASSERT_MSG()));
        }
    }
}