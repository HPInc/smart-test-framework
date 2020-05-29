package system.test;

import hp.inc.jsg.qa.stf.framework.datadriven.DesktopAutomationDataDrivenHelper;
import hp.inc.jsg.qa.stf.framework.desktop.DesktopAutomationHelper;
import hp.inc.jsg.qa.stf.framework.io.InputOutputHelper;
import io.qameta.allure.*;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import system.base.MainTestBase;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class DesktopAutomation extends MainTestBase {

    DesktopAutomationDataDrivenHelper dataDrivenHelper;
    String folderWithMasterImages;
    String navigatorFile;
    String dataDrivenFile;
    Pattern xButton;
    String stf_win_app_demo;
    Double similarity;
    DesktopAutomationHelper desktopAutomationHelper;

    @BeforeClass
    public void beforeClass() throws Exception {
        testLog.logIt("Running before class steps");
        stf_win_app_demo = getAbsoluteFilePathFromResources("bin/SmartTestFrameworkDemo.exe");
        similarity = 0.92;
        Screen screen = new Screen();

        String screenshotsDestination = System.getProperty("user.home") + "/STF_Screenshots";
        InputOutputHelper.createDirectory(screenshotsDestination);

        desktopAutomationHelper = new DesktopAutomationHelper(
                testLog,
                screen,
                screenshotsDestination,
                similarity,
                100,
                20,
                true,
                true,
                true);

        folderWithMasterImages = getAbsoluteFilePathFromResources("imgrDatadriven/desktop-apps/winformSTFdemo/1.0");

        navigatorFile = folderWithMasterImages + "/nav_winformSTFdemo_v1-0_1920x1080_Win-10_ALL-BTNS-AND-FUNCTIONS.json";
        dataDrivenFile = folderWithMasterImages + "/dd_winformSTFdemo_v1-0_SETS_AND_ASSERTS.json";

        dataDrivenHelper = new DesktopAutomationDataDrivenHelper(testLog);

        //Declaring the close button
        xButton = new Pattern(folderWithMasterImages + "/topBar.png");
        xButton.similar(similarity);
        xButton.targetOffset(206, -41);
    }

    @BeforeMethod
    public void runBeforeEachTest() throws IOException {
        testLog.logIt("Open the app");
        Runtime.getRuntime().exec(String.format("cmd /c %s", stf_win_app_demo));
    }

    @AfterMethod
    public void runAfterEachTest() throws Exception {
        testLog.logIt("Closing the app");
        desktopAutomationHelper.click(xButton);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of using SikuliX Pattern with STF DESKTOP-AUTOMATION action features")
    @Description("Declare Patterns for Windows form app controllers, setup several its fields, then assert all configured values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void testWinAppDeclaringPatterns() throws Exception {
        //These variables below are the well known PAGE\APP OBJECT - a way to describe its elements\features - to be reusable.
        Pattern textBox2 = new Pattern(folderWithMasterImages + "/reg_textBox2.png");
        textBox2.similar(similarity);

        Pattern checkBox1 = new Pattern(folderWithMasterImages + "/reg_checkBox1_default.png");
        checkBox1.similar(similarity);

        Pattern radioButton2 = new Pattern(folderWithMasterImages + "/reg_radionButton2_default.png");
        radioButton2.similar(similarity);

        //These are similar to the ones above, but containing its final state.
        Pattern textBox2Result = new Pattern(folderWithMasterImages + "/reg_textBox2_result.png");
        textBox2Result.similar(similarity);

        Pattern checkBox1Result = new Pattern(folderWithMasterImages + "/mst_winformSTFdemo_v1-0_1920x1080_Win-10_dd_assert_cbx1.png");
        checkBox1Result.similar(similarity);

        Pattern radioButton2Result = new Pattern(folderWithMasterImages + "/mst_winformSTFdemo_v1-0_1920x1080_Win-10_dd_assert_rbt2.png");
        radioButton2Result.similar(similarity);

        //Set Phase
        testLog.logIt("Fill textBox");
        desktopAutomationHelper.doubleClick(textBox2);
        desktopAutomationHelper.getScreen().paste("this is a test");

        testLog.logIt("Enable checkbox");
        desktopAutomationHelper.click(checkBox1);

        testLog.logIt("Enable Radio button");
        desktopAutomationHelper.click(radioButton2);

        //Assert Phase
        Assert.assertTrue(desktopAutomationHelper.exists(textBox2Result, 0.00));
        Assert.assertTrue(desktopAutomationHelper.exists(checkBox1Result, 0.00));
        Assert.assertTrue(desktopAutomationHelper.exists(radioButton2Result, 0.00));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of using STF Navigator feature for DESKTOP-AUTOMATION")
    @Description("Setup several fields from a Windows form app, then assert all configured values using Navigator helper")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void testWinAppUsingNavigator() throws Exception {
        /*For this usage, all elements were already described in a navigator file.
        And to use all these 3 buttons below, just required a common image for all - the feature icon, all the rest is using its relative coordinates.
         */

        //Set Phase
        testLog.logIt("Fill textBox");
        desktopAutomationHelper.doubleClick(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "mainScreen", "txtBox_2"));
        desktopAutomationHelper.getScreen().paste("this is a test");

        testLog.logIt("Enable checkbox");
        desktopAutomationHelper.click(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "mainScreen", "cbx_1"));

        testLog.logIt("Enable Radio button");
        desktopAutomationHelper.click(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "mainScreen", "rbt_2"));

        /*
        For the asserts we're still using the same 3 screenshots files taken in previous test. But it's quiet
        easier to use declared with all required settings inside the navigator.
         */

        //Assert Phase
        Assert.assertTrue(desktopAutomationHelper.exists(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "dataDrivenSpecific", "textBox2Result"), 0.00));
        Assert.assertTrue(desktopAutomationHelper.exists(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "dataDrivenSpecific", "checkBox1"), 0.00));
        Assert.assertTrue(desktopAutomationHelper.exists(desktopAutomationHelper.retrievePatternFromNavigator(navigatorFile, "dataDrivenSpecific", "radioButton2"), 0.00));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("A QA should be capable of using STF Data-Driven feature for DESKTOP-AUTOMATION")
    @Description("Setup several fields from a Windows form app, then assert all configured values just using data-driven helper")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void testWinAppUsingDataDriven() throws Exception {
        /*
        This is a powerful feature from STF, using the synergy from navigator plus data driven.
         */

        //Set Phase
        dataDrivenHelper.executeDataDriven(
                desktopAutomationHelper,
                navigatorFile,
                dataDrivenFile,
                "set_validValues_kickoffDemo");

        //Assert Phase
        dataDrivenHelper.executeDataDriven(
                desktopAutomationHelper,
                navigatorFile,
                dataDrivenFile,
                "assert_validValues_kickoffDemo");
    }

    private String getAbsoluteFilePathFromResources(String filename) throws URISyntaxException {
        URL res = getClass().getClassLoader().getResource(filename);
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }
}