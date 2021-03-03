package system.test;

import com.github.jeansantos38.stf.contants.ui.LogMessages;
import com.github.jeansantos38.stf.framework.ui.UiAutomationDriver;
import com.github.jeansantos38.stf.framework.ui.UiElement;
import com.github.jeansantos38.stf.framework.ui.UiVisualFeedback;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import system.base.UiAutomationTestBase;

public class UiAutomationControls extends UiAutomationTestBase {

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

    @BeforeMethod
    public void initializeApp() throws Exception {
        endWinDemoApp();
        startWinDemoApp();
    }

    @AfterClass
    public void cleanup() throws Exception {
        endWinDemoApp();
    }

    @Test
    public void existNonExist() throws Exception {
        stfIcon.assertVisible();
        try {
            //Negative check 1
            stfIcon.assertNotVisible(3000);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.STILL_VISIBLE_ASSERT_MSG));
        }
        UiElement nonExistentPattern = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "nonExistentRedDice");
        nonExistentPattern.assertNotVisible(3000);
        try {
            //Negative check 2
            nonExistentPattern.assertVisible(3000);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.NOT_VISIBLE_ASSERT_MSG));
        }
    }

    @Test
    public void checkBoxTest() throws Exception {
        UiElement checkboxTrue = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "checkbox_true");
        UiElement checkboxFalse = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "checkbox_false");

        UiElement checkbox1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_1");
        checkbox1.click();
        stfIcon.moveCursorOver();

        //Expected checks
        checkbox1.assertRegionContainsPattern(checkboxTrue);
        checkbox1.assertRegionNotContainsPattern(checkboxFalse);

        try {
            //Negative check 1
            checkbox1.assertRegionContainsPattern(checkboxFalse);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_NOT_FOUND_IN_REGION));
        }
        try {
            //Negative check 2
            checkbox1.assertRegionNotContainsPattern(checkboxTrue);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_FOUND_IN_REGION));
        }

        UiElement checkbox2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_2");
        checkbox2.click();
        stfIcon.moveCursorOver();
        //Expected
        checkbox2.assertRegionContainsPattern(checkboxFalse);
        checkbox2.assertRegionNotContainsPattern(checkboxTrue);
        try {
            //Negative check 3
            checkbox2.assertRegionContainsPattern(checkboxTrue);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_NOT_FOUND_IN_REGION));
        }

        try {
            //Negative check 4
            checkbox2.assertRegionNotContainsPattern(checkboxFalse);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_FOUND_IN_REGION));
        }
    }

    @Test
    public void radioButtonTest() throws Exception {
        UiElement radioBtnTrue = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "radiobtn_true");
        UiElement radioBtnFalse = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "radiobtn_false");

        UiElement radioBtn1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "rbt_1");
        radioBtn1.assertRegionContainsPattern(radioBtnTrue);

        UiElement radioBtn2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "rbt_2");
        radioBtn2.assertRegionContainsPattern(radioBtnFalse);

        UiElement radioBtn3 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "rbt_3");
        radioBtn3.assertRegionContainsPattern(radioBtnFalse);
        radioBtn3.click();
        stfIcon.moveCursorOver();

        //Expected checks
        radioBtn3.assertRegionContainsPattern(radioBtnTrue);
        radioBtn3.assertRegionNotContainsPattern(radioBtnFalse);
        radioBtn1.assertRegionNotContainsPattern(radioBtnTrue);
        radioBtn3.assertRegionNotContainsPattern(radioBtnFalse);
        radioBtn2.assertRegionNotContainsPattern(radioBtnTrue);

        try {
            //Negative check 1
            radioBtn3.assertRegionContainsPattern(radioBtnFalse);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_NOT_FOUND_IN_REGION));
        }
        try {
            //Negative check 2
            radioBtn2.assertRegionNotContainsPattern(radioBtnFalse);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_FOUND_IN_REGION));
        }
        try {
            //Negative check 3
            radioBtn1.assertRegionContainsPattern(radioBtnTrue);
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_PATTERN_NOT_FOUND_IN_REGION));
        }
    }

    @Test
    public void textBoxClipboardTest() throws Exception {
        String textToBePasted = "Value pasted from clipboard!";
        UiElement textBox1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox1Region");
        textBox1.assertContainsTextViaClipboard("textbox1");
        textBox1.clearText();
        textBox1.assertContainsTextViaClipboard("");
        textBox1.paste(textToBePasted);
        textBox1.assertContainsTextViaClipboard(textToBePasted);
        Assert.assertEquals(textToBePasted, textBox1.extractTextViaClipboard());
        try {
            //Negative check 1
            textBox1.assertContainsTextViaClipboard(textToBePasted + "A");
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_TEXT_NOT_FOUND_VIA_CLIPBOARD));
        }

        UiElement textBox2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox2Region");
        textBox2.assertContainsTextViaClipboard("textbox2");
        textBox2.clearText();
        textBox2.assertContainsTextViaClipboard("");
        textBox2.paste(textToBePasted);
        textBox2.assertContainsTextViaClipboard(textToBePasted);
        try {
            //Negative check 2
            textBox2.assertContainsTextViaClipboard(textToBePasted + "B");
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_TEXT_NOT_FOUND_VIA_CLIPBOARD));
        }
    }

    @Test
    public void textBoxTypeAndOCRCheckTest() throws Exception {
        String textToBePasted = "Value extracted via OCR";
        //Remove focus from text box before start test. OCR can be affected if has any selection or cursor blinking.
        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "cbx_2").click();

        UiElement textBox1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox1Region");
        textBox1.assertContainsTextViaOCR("textbox");
        textBox1.clearText();
        textBox1.assertContainsTextViaOCR("");
        textBox1.type(textToBePasted);
        textBox1.assertContainsTextViaOCR(textToBePasted);
        Assert.assertTrue(textBox1.extractTextViaOCR().contains(textToBePasted));
        try {
            //Negative check 1
            textBox1.assertContainsTextViaOCR(textToBePasted + "A");
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_TEXT_NOT_FOUND_VIA_OCR));
        }

        UiElement textBox2 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "textBox2Region");
        textBox2.assertContainsTextViaOCR("textbox2");
        textBox2.clearText();
        textBox2.assertContainsTextViaOCR("");
        textBox2.type(textToBePasted);
        textBox2.assertContainsTextViaOCR(textToBePasted);
        Assert.assertTrue(textBox2.extractTextViaOCR().contains(textToBePasted));
        try {
            //Negative check 1
            textBox2.assertContainsTextViaOCR(textToBePasted + "B");
        } catch (AssertionError error) {
            Assert.assertTrue(error.getMessage().contains(LogMessages.ERROR_TEXT_NOT_FOUND_VIA_OCR));
        }
    }

    @Test
    public void dropDownTest() throws Exception {
        UiElement dropdownLst = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "dropdownLst");
        dropdownLst.click();
        uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "dropdownLst_item5").click();
        dropdownLst.assertContainsTextViaOCR("Brazil");
    }

    @Test
    public void trackBarTest() throws Exception {
        UiElement trackbarPointer = uiAutomationDriver.buildPatternFromNavigator(navigator, "desktop", "trackbar_pointer");
        UiElement trackbarOpt1 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "tbr_value1");
        UiElement trackbarOpt3 = uiAutomationDriver.buildPatternFromNavigator(navigator, "mainScreen", "tbr_value3");
        trackbarOpt1.dragAndDrop(trackbarOpt3);
        stfIcon.moveCursorOver();
        trackbarOpt3.assertRegionContainsPattern(trackbarPointer);
    }

}