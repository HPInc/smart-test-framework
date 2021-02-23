package system.test;

import com.github.jeansantos38.stf.framework.datadriven.DesktopAutomationDataDrivenHelper;
import com.github.jeansantos38.stf.framework.desktop.DesktopAutomationHelper;
import com.github.jeansantos38.stf.framework.desktop.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.desktop.UiElement;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import io.qameta.allure.*;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.TestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import system.base.MainTestBase;
import system.base.UiAutomationTestBase;

import java.io.File;
import java.io.IOException;

public class UiAutomation extends UiAutomationTestBase {

    UiAutomationDriver uiAutomationDriver;
    private String radioButton2;
    private String textBoxFilled;

    @BeforeClass
    public void beforeClass() throws Exception {
        Screen screen = new Screen();
        uiAutomationDriver = new UiAutomationDriver(screen, testLog, "C:\\Users\\giacomin\\alopra_Screenshots", true);
        radioButton2 = "imgrDatadriven/desktop-apps/winformSTFdemo/1.0/reg_radionButton2_default.png";
        textBoxFilled = "imgrDatadriven/desktop-apps/winformSTFdemo/1.0/reg_textBox2_result.png";
    }


    @Test
    public void testWinAppDeclaringPatterns() throws Exception {
        UiElement checkbox1 = uiAutomationDriver.buildPattern(discoverAbsoluteFilePath(radioButton2));
        checkbox1.assertIsVisible();
        checkbox1.assertIsVisible(2);
        checkbox1.click();    }

    @Test
    public void checkNonExistentPattern() throws IOException, FindFailed {
        UiElement uiElement = uiAutomationDriver.buildPattern(textBoxFilled);
        try {
            uiElement.assertIsVisible(2);
        } catch (AssertionError a) {
            Assert.assertTrue(a.getMessage().contains(uiElement.NOT_VISIBLE_ASSERT_MSG));
        }
    }
}