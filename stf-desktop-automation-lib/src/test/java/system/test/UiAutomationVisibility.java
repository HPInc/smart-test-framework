package system.test;

import com.github.jeansantos38.stf.framework.ui.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.ui.UiElement;
import com.github.jeansantos38.stf.framework.ui.UiVisualFeedback;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import system.base.UiAutomationTestBase;

public class UiAutomationVisibility extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    String navigator;
    UiElement stfIcon;

    @BeforeClass
    public void beforeClass() throws Exception {
        navigator = discoverAbsoluteFilePath("masters/winApp/_navigator_STF_Win_Demo-App.yml");
        UiVisualFeedback uiVisualFeedback = new UiVisualFeedback();
        uiVisualFeedback.setEnableHighlight(true);
        uiAutomationDriver = new UiAutomationDriver(new Screen(), testLog, waitHelper, System.getProperty("user.home") + "/STF_Screenshots", true, 500, 500, uiVisualFeedback);
        stfIcon = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "innerIco");
    }

    @BeforeClass
    public void initializeApp() throws Exception {
        endWinDemoApp();
    }

    @Test
    public void waitExistsTest() throws Exception {
        try {
            stfIcon.waitExists(1, true);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("stf_ico.png"));
        }
        startWinDemoApp();
        stfIcon.waitExists(5, true);
    }


    @Test(dependsOnMethods = {"waitExistsTest"})
    public void waitVanishesTest() throws Exception {
        try {
            stfIcon.waitVanishes(5, true);
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
        }
        endWinDemoApp();
        stfIcon.waitVanishes(5, true);
    }
}