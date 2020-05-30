package hp.inc.jsg.qa.stf.framework.datadriven;

import hp.inc.jsg.qa.stf.dataclasses.datadriven.Statement;
import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.webdriver.WebDriverAppiumHelper;
import hp.inc.jsg.qa.stf.framework.webdriver.WebDriverSeleniumHelper;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class AssertWebDriverInstructionSet extends WebDriverDataDrivenCommon {

    private TestLog testLog;
    private WebDriverAppiumHelper webDriverAppiumHelper;
    private WebDriverSeleniumHelper webDriverSeleniumHelper;
    private static final String COMMON_ERROR_MESSAGE = "Comparison has failed! [Operator:%1$s][Provided value:%2$s][Current value:%3$s]";

    /**
     * Main constructor for Asserting appium statements.
     *
     * @param testLog:              LoggerQA class instance.
     * @param webDriverAppiumHelper
     */
    public AssertWebDriverInstructionSet(TestLog testLog, WebDriverAppiumHelper webDriverAppiumHelper) {
        this.testLog = testLog;
        this.webDriverAppiumHelper = webDriverAppiumHelper;
    }

    /***
     * Main constructor for Asserting selenium statements.
     * @param testLog: LoggerQA class instance.
     * @param webDriverSeleniumHelper
     */
    public AssertWebDriverInstructionSet(TestLog testLog, WebDriverSeleniumHelper webDriverSeleniumHelper) {
        this.webDriverSeleniumHelper = webDriverSeleniumHelper;
        this.testLog = testLog;
    }

    /***
     * Main helper for asserting WebDriver statements.
     * @param statement: Data driven statement object.
     * @throws Exception
     */
    public void dataDrivenAssert(Statement statement) throws Exception {
        //Declaring some variables.
        WebElement webElement;

        this.testLog.logIt("Extracting the content from statement as string.");

        testLog.logIt(String.format("[Action:%1$s][ContentType:%2$s][Content:%3$s][Selector:%4$s][Selector:%5$s]", statement.action, statement.contentType, statement.content[0], statement.selector, statement.locator));

        testLog.logIt("Retrieve webElement based on provided action.");
        webElement = retrieveWebElementBasedOnAction(statement);

        if (webElement == null) {
            throw new Exception(String.format("The statement that has [selector:%1$s]#[content:%2$s]#[action:%3$s] -WAS NOT FOUND- check its respective **instructionSet id** above in the log and make sure all its data are correct.", statement.selector, statement.content[0], statement.action));
        }

        MobileElement mobileElement = null;
        if (statement.agent.equals("appium")) {
            this.testLog.logIt("Cast webElement to mobileElement.");
            mobileElement = (MobileElement) webElement;
        }

        this.testLog.logIt("Check if the request operator does support the content provided.");
        verifyIfOperatorSupportsContent(statement.operator, statement.contentType);

        this.testLog.logIt("Start asserting the statement!");
        assertContent(statement, webElement, mobileElement);
    }

    /**
     * Helper that retrieve the element based on the action provided.
     *
     * @param statement
     * @return
     * @throws Exception
     */
    private WebElement retrieveWebElementBasedOnAction(Statement statement) throws Exception {
        boolean isAgentSelenium = isSeleniumAgent(statement.agent);

        switch (statement.action) {
            case "assertField":
                switch (statement.locator) {
                    case "byId":
                    case "byXpath":
                    case "byClass":
                    case "byCss":
                    case "byName":
                    case "byTagName":
                    case "byLink":
                    case "byPartialLink":
                        return isAgentSelenium ? webDriverSeleniumHelper.first(true, returnSelectorType(statement.locator), statement.selector) : webDriverAppiumHelper.first(true, returnSelectorType(statement.locator), statement.selector);
                    default:
                        throw new Exception(String.format("The locator:[%1$s] is not supported!!!", statement.locator));
                }

            default:
                throw new Exception(String.format("The action type:[%1$s] is not supported!!!", statement.action));
        }
    }

    /**
     * Helper that handles assertions accordingly to the given instructions.
     *
     * @param statement
     * @param webElement
     * @param mobileElement
     * @throws Exception
     */
    private void assertContent(Statement statement, WebElement webElement, MobileElement mobileElement) throws Exception {
        // This is the tricky part. I'm quite sure as we start using it, issues related to the content type and the request operators might become problems. So it's just a matter of fixing its 'shape'.
        switch (statement.operator) {
            case "EqualTo":
                validateEqualTo(statement.operator, statement.agent, statement.contentType, statement.content[0], webElement, mobileElement);
                break;

            case "NotEqualTo":
                validateNotEqualTo(statement.operator, statement.agent, statement.contentType, statement.content[0], webElement, mobileElement);
                break;

            case "Contains":
                validateContains(statement.operator, statement.agent, true, statement.content[0], webElement, mobileElement);
                break;

            case "NotContain":
                validateContains(statement.operator, statement.agent, false, statement.content[0], webElement, mobileElement);
                break;

            case "GreaterThan":
            case "GreaterThanOrEqualTo":
            case "LesserThan":
            case "LesserThanOrEqualTo":
                validateNumbers(statement.operator, statement.agent, statement.content[0], webElement, mobileElement);
                break;

            case "Exists":
                Assert.assertNotNull(statement.agent.equals("appium") ? mobileElement : webElement, String.format("The selector %1$s  - DOES NOT EXIST - !!!", statement.selector));
                break;

            case "NotExist":
                Assert.assertNull(statement.agent.equals("appium") ? mobileElement : webElement, String.format("The selector %1$s  - DOES EXIST - !!!", statement.selector));
                break;

            default:
                throw new Exception(String.format("The operator type:[%1$s] is not supported!!!", statement.operator));
        }
    }

    /**
     * Simple validation helper
     *
     * @param operator:     The operator will defined what type of assert will be used.
     * @param agent
     * @param expectedValue
     * @param webElement
     * @param mobileElement
     * @throws Exception
     */
    private void validateNumbers(String operator, String agent, Object expectedValue, WebElement webElement, MobileElement mobileElement) throws Exception {

        String mExpectedValue = String.valueOf(expectedValue);
        long expectedNumValue = Long.valueOf(mExpectedValue);
        String mCurrentValue = agent.equals("appium") ? mobileElement.getText() : webElement.getText();
        long currentNumValue = Long.valueOf(mCurrentValue);

        switch (operator) {
            case "GreaterThan":
                Assert.assertTrue(expectedNumValue > currentNumValue, String.format(COMMON_ERROR_MESSAGE, operator, mExpectedValue, mCurrentValue));
                break;

            case "GreaterThanOrEqualTo":
                Assert.assertTrue(expectedNumValue >= currentNumValue, String.format(COMMON_ERROR_MESSAGE, operator, mExpectedValue, mCurrentValue));
                break;

            case "LesserThan":
                Assert.assertTrue(expectedNumValue < currentNumValue, String.format(COMMON_ERROR_MESSAGE, operator, mExpectedValue, mCurrentValue));
                break;

            case "LesserThanOrEqualTo":
                Assert.assertTrue(expectedNumValue <= currentNumValue, String.format(COMMON_ERROR_MESSAGE, operator, mExpectedValue, mCurrentValue));
                break;
            default:
                throw new Exception(String.format("The operator type:[%1$s] is not supported!!!", operator));
        }
    }

    /**
     * Simple validation helper.
     *
     * @param operator:       The operator will defined what type of assert will be used.
     * @param agent
     * @param lookForContains
     * @param expectedValue
     * @param webElement
     * @param mobileElement
     */
    private void validateContains(String operator, String agent, boolean lookForContains, Object expectedValue, WebElement webElement, MobileElement mobileElement) throws Exception {
        String currentValue = isSeleniumAgent(agent) ? webElement.getAttribute("value") : mobileElement.getText();
        String mExpectedValue = String.valueOf(expectedValue);
        if (lookForContains) {
            //Assuming that does not matter the contentType here, string will do the job.
            Assert.assertTrue(currentValue.contains(mExpectedValue), String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, currentValue));
        } else { //Assuming that does not matter the contentType here, string will do the job.
            Assert.assertFalse(currentValue.contains(mExpectedValue), String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, currentValue));
        }
    }

    /**
     * Simple validation helper
     *
     * @param operator:     The operator will defined what type of assert will be used.
     * @param agent
     * @param contentType
     * @param expectedValue
     * @param webElement
     * @param mobileElement
     */
    private void validateEqualTo(String operator, String agent, String contentType, Object expectedValue, WebElement webElement, MobileElement mobileElement) throws Exception {
        if (contentType.equals("boolean")) {
            boolean finalExpectedValue = (boolean) expectedValue;
            boolean currentValue = isSeleniumAgent(agent) ? Boolean.valueOf(webElement.getAttribute("checked")) : Boolean.valueOf(mobileElement.getAttribute("checked"));

            if (finalExpectedValue) {
                Assert.assertTrue(currentValue, String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, String.valueOf(currentValue)));
            } else {
                Assert.assertFalse(currentValue, String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, String.valueOf(currentValue)));
            }

        } else {//Assuming that does not matter the contentType here, since string will do the job.
            String currentValue = isSeleniumAgent(agent) ? webElement.getAttribute("value") : mobileElement.getText();
            Assert.assertEquals(currentValue, String.valueOf(expectedValue), String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, currentValue));
        }
    }

    /**
     * Simple validation helper.
     *
     * @param operator:     The operator will defined what type of assert will be used.
     * @param agent
     * @param contentType
     * @param expectedValue
     * @param webElement
     * @param mobileElement
     */
    private void validateNotEqualTo(String operator, String agent, String contentType, Object expectedValue, WebElement webElement, MobileElement mobileElement) throws Exception {
        if (contentType.equals("boolean")) {
            boolean finalExpectedValue = (boolean) expectedValue;
            boolean currentValue = isSeleniumAgent(agent) ? Boolean.valueOf(webElement.getAttribute("checked")) : Boolean.valueOf(mobileElement.getAttribute("checked"));

            if (finalExpectedValue) {
                Assert.assertFalse(currentValue, String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, String.valueOf(currentValue)));
            } else {
                Assert.assertTrue(currentValue, String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, String.valueOf(currentValue)));
            }

        } else {//Assuming that does not matter the contentType here, since string will do the job.
            String currentValue = isSeleniumAgent(agent) ? webElement.getAttribute("value") : mobileElement.getText();
            Assert.assertNotEquals(currentValue, String.valueOf(expectedValue), String.format(COMMON_ERROR_MESSAGE, operator, expectedValue, currentValue));
        }
    }

    /**
     * Simple helper that validates if provided agent is supported and true if selenium.
     *
     * @param agent
     * @return
     * @throws Exception
     */
    private boolean isSeleniumAgent(String agent) throws Exception {
        String tempAgent = agent.toLowerCase();
        if (!tempAgent.equals("selenium") && !tempAgent.equals("appium")) {
            throw new Exception(String.format("The agent %1$s is not supported! Only Selenium or Appium!", agent));
        }
        return tempAgent.equals("selenium");
    }
}