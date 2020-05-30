package system.test.ui;

import hp.inc.jsg.qa.stf.enums.webdriver.SelectorType;
import hp.inc.jsg.qa.stf.framework.datadriven.WebDriverDataDrivenHelper;
import hp.inc.jsg.qa.stf.framework.webdriver.WebDriverSeleniumHelper;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import system.test.base.WebUiTestBase;

import java.io.File;

public class WebUiTests extends WebUiTestBase {
    WebDriverSeleniumHelper webDriverSeleniumHelper;
    WebDriverDataDrivenHelper webDriverDataDrivenHelper;
    String demoPageUrl;
    String dataDrivenFile = discoverAbsoluteFilePath("/webUiDatadriven/dataDriven_webcontent.json");

    @BeforeClass
    public void classInitialize() {
        webDriverDataDrivenHelper = new WebDriverDataDrivenHelper(testLog);
        webDriverSeleniumHelper = new WebDriverSeleniumHelper(30, testLog);
        demoPageUrl = uiTestsBaseUrl + "/form";
    }

    @BeforeMethod
    public void beforeClass() throws Exception {
        testLog.logIt("Starting chrome browser session!");
        webDriverSeleniumHelper.setSeleniumDriver(startChromeBrowser(desiredCapabilities, seleniumGridServerUrl));
    }

    @AfterMethod
    public void afterClass() {
        testLog.logIt("Ending selenium");
        webDriverSeleniumHelper.endSeleniumWebdriverUsage();
    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of using STF webdriver helpers to locate and interact with selenium web-elements")
    @Description("Setup an entire web FORM, then assert all configured values just using STF helpers")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void fillFormDirectly() throws Exception {
        webDriverSeleniumHelper.navigate(demoPageUrl);
        Assert.assertEquals(webDriverSeleniumHelper.getSeleniumDriver().getCurrentUrl(), demoPageUrl);

        testLog.logIt("Wait the first element to be visible");
        webDriverSeleniumHelper.waitForElementBecomeVisible(SelectorType.BY_CSS, "input[name='tbx1']", 10);

        //Sets
        testLog.logIt("Fill all text boxes");
        String valueToBeSetOnTextbox1 = "1 hello this is a test from STF";
        String valueToBeSetOnTextbox2 = "some input from stf";

        webDriverSeleniumHelper.first(SelectorType.BY_CSS, "input[name='tbx1']").sendKeys(valueToBeSetOnTextbox1);
        webDriverSeleniumHelper.first(SelectorType.BY_CSS, "input[name='tbx2']").sendKeys(valueToBeSetOnTextbox2);

        testLog.logIt("Select the 2nd radio button");
        webDriverSeleniumHelper.first(SelectorType.BY_ID, "radio-1").click();

        testLog.logIt("Select the 1st checkbox");
        webDriverSeleniumHelper.first(SelectorType.BY_CSS, "#option-0").click();

        testLog.logIt("Set the 4th value from drop down list");
        String optionToBeSet = "Super option 4";
        webDriverSeleniumHelper.first(SelectorType.BY_CSS, "#dropdown").sendKeys(optionToBeSet);

        //Asserts section
        Assert.assertEquals(
                webDriverSeleniumHelper.first(SelectorType.BY_CSS, "input[name='tbx1']").getAttribute("value"),
                valueToBeSetOnTextbox1);

        Assert.assertEquals(
                webDriverSeleniumHelper.first(SelectorType.BY_CSS, "input[name='tbx2']").getAttribute("value"),
                valueToBeSetOnTextbox2);

        Assert.assertTrue(webDriverSeleniumHelper.first(SelectorType.BY_ID, "radio-1").isSelected());

        Assert.assertTrue(webDriverSeleniumHelper.first(SelectorType.BY_ID, "option-0").isSelected());

        Assert.assertEquals(
                webDriverSeleniumHelper.first(SelectorType.BY_ID, "dropdown").getAttribute("value"),
                optionToBeSet);
    }

    @Test()
    @Severity(SeverityLevel.BLOCKER)
    @Story("A QA should be capable of using STF with PAGE-OBJECTS")
    @Description("Setup an entire web FORM, then assert all configured values just using page objects")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void fillFormUsingPageObject() throws Exception {
        webDriverSeleniumHelper.navigate(demoPageUrl);
        Assert.assertEquals(webDriverSeleniumHelper.getSeleniumDriver().getCurrentUrl(), demoPageUrl);

        By textBox1 = new By.ByCssSelector("input[name='tbx1']");
        By textBox2 = new By.ByCssSelector("input[name='tbx2']");
        By radioButton2 = new By.ById("radio-1");
        By checkbox1 = new By.ByCssSelector("#option-0");
        By dropDownList = new By.ByCssSelector("#dropdown");

        testLog.logIt("Wait the first element to be visible");
        webDriverSeleniumHelper.waitForElementBecomeVisible(textBox1, 10);

        //Sets
        testLog.logIt("Fill all text boxes");
        String valueToBeSetOnTextbox1 = "1 hello this is a test from STF";
        String valueToBeSetOnTextbox2 = "some input from stf";

        webDriverSeleniumHelper.first(textBox1).sendKeys(valueToBeSetOnTextbox1);
        webDriverSeleniumHelper.first(textBox2).sendKeys(valueToBeSetOnTextbox2);

        testLog.logIt("Select the 2nd radio button");
        webDriverSeleniumHelper.first(radioButton2).click();

        testLog.logIt("Select the 1st checkbox");
        webDriverSeleniumHelper.first(checkbox1).click();

        testLog.logIt("Set the 4th value from drop down list");
        String optionToBeSet = "Super option 4";
        webDriverSeleniumHelper.first(dropDownList).sendKeys(optionToBeSet);

        //Asserts section
        Assert.assertEquals(webDriverSeleniumHelper.first(textBox1).getAttribute("value"), valueToBeSetOnTextbox1);

        Assert.assertEquals(webDriverSeleniumHelper.first(textBox2).getAttribute("value"), valueToBeSetOnTextbox2);

        Assert.assertTrue(webDriverSeleniumHelper.first(radioButton2).isSelected());

        Assert.assertTrue(webDriverSeleniumHelper.first(checkbox1).isSelected());

        Assert.assertEquals(webDriverSeleniumHelper.first(dropDownList).getAttribute("value"), optionToBeSet);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("A QA should be capable of using STF Data-Driven feature for WEB-DRIVER")
    @Description("Setup an entire web FORM, then assert all configured values just using data-driven helper")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void fillFormUsingDataDriven() throws Exception {
        webDriverSeleniumHelper.navigate(demoPageUrl);
        Assert.assertEquals(webDriverSeleniumHelper.getSeleniumDriver().getCurrentUrl(), demoPageUrl);

        //Sets
        webDriverDataDrivenHelper.executeDataDriven(webDriverSeleniumHelper, dataDrivenFile, "set_validValues1");

        //Asserts
        webDriverDataDrivenHelper.executeDataDriven(webDriverSeleniumHelper, dataDrivenFile, "assert_validValues1");
    }


    private String discoverAbsoluteFilePath(String fileNameRelativePath) {
        return new File(String.format("src/test/resources%s", fileNameRelativePath)).getAbsolutePath();
    }
}
