package com.github.jeansantos38.stf.framework.datadriven;

import com.github.jeansantos38.stf.framework.webdriver.WebDriverAppiumHelper;
import com.github.jeansantos38.stf.dataclasses.datadriven.Statement;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import io.appium.java_client.MobileElement;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class SetWebDriverInstructionSet extends WebDriverDataDrivenCommon {

    private TestLog testLog;
    private WebDriverAppiumHelper webDriverAppiumHelper;
    private WebDriverSeleniumHelper webDriverSeleniumHelper;

    /***
     * Default constructor for Appium data driven.
     * @param testLog: LoggerQA class instance.
     * @param webDriverAppiumHelper
     */
    public SetWebDriverInstructionSet(TestLog testLog, WebDriverAppiumHelper webDriverAppiumHelper) {
        this.testLog = testLog;
        this.webDriverAppiumHelper = webDriverAppiumHelper;
    }

    /***
     * Default constructor for Selenium data driven.
     * @param testLog: LoggerQA class instance.
     * @param webDriverSeleniumHelper
     */
    public SetWebDriverInstructionSet(TestLog testLog, WebDriverSeleniumHelper webDriverSeleniumHelper) {
        this.testLog = testLog;
        this.webDriverSeleniumHelper = webDriverSeleniumHelper;
    }

    /***
     * This helper does all the magic required for asserting WebDriver based data driven.
     * @param statement: Data driven statement object.
     * @throws Exception
     */
    public void dataDrivenSet(Statement statement) throws Exception {

        if (statement.delayActionInMs != null) {
            try {
                Integer wait = (Integer) statement.delayActionInMs;
                if (wait > 0) {
                    WaitHelper waitHelper = new WaitHelper();
                    waitHelper.waitMilliseconds(wait);
                }
            } catch (Exception e) {
                testLog.logIt(e.getMessage());
                testLog.logIt("The delay Action In Milliseconds didn't have a valid value!");
            }
        }

        //This case below was  necessary due to the possibility of setting a Angular ComboBox using its option selector, so no value (content) is required in the data driven statement. This is something we can live.
        String contentType = statement.contentType == null || statement.contentType.isEmpty() ? "NOT PROVIDED" : statement.contentType;

        this.testLog.logIt(String.format("[Action:%1$s][ContentType:%2$s][Selector:%3$s][Locator:%4$s]", statement.action, contentType, statement.selector, statement.locator));

        switch (statement.action) {
            case "setField":
                switch (statement.locator) {
                    case "byId":
                    case "byXpath":
                    case "byClass":
                    case "byCss":
                    case "byName":
                    case "byTagName":
                    case "byLink":
                    case "byPartialLink":
                        if (statement.agent.equals("appium")) {
                            MobileElement mobileElement = (MobileElement) this.webDriverAppiumHelper.first(false, returnSelectorType(statement.locator), statement.selector);
                            switch (statement.contentType) {
                                case "string":
                                    setAppiumString(mobileElement, extractAndProcessStringValueFromDataDriven(statement), statement.contentType);
                                    break;

                                case "boolean":
                                    setAppiumBoolean(mobileElement, (Boolean) statement.content[0], statement.contentType);
                                    break;

                                default:
                                    throw new Exception(String.format("The content type:[%1$s] is not supported yet for the action:[%2$s]!!!", statement.contentType, statement.action));
                            }
                        } else {//Selenium
                            switch (statement.contentType) {
                                case "string":
                                    setSeleniumString(statement.locator, extractAndProcessStringValueFromDataDriven(statement), statement.selector);
                                    break;

                                case "boolean":
                                    setSeleniumBoolean(statement.locator, (Boolean) statement.content[0], statement.selector);
                                    break;

                                default:
                                    throw new Exception(String.format("The locator:[%1$s] is not supported yet for the action:[%2$s]!!!", statement.locator, statement.action));
                            }
                        }
                        break;

                    default:
                        throw new Exception(String.format("The action type:[%1$s] is not supported yet!!!", statement.action));
                }
                break;

            default:
                throw new Exception(String.format("The action type:[%1$s] is not supported yet!!!", statement.action));
        }
    }

    /***
     * Simple helper that prevents a Selenium statement from using Appium-only supported actions. It might happen when number of tests starts to grow as copy and paste...
     * @param agent
     * @param action
     * @throws Exception
     */
    private void validateAgent(String agent, String action) throws Exception {
        if (agent.equals("selenium")) {
            throw new Exception(String.format("The [action:%1$s] is not supported by [agent:%2$s]", action, agent));
        }
    }

    /***
     * Simple helper to set a string value into a mobile element.
     * @param mobileElement
     * @param mStringContent
     * @param mContentType
     */
    private void setAppiumString(MobileElement mobileElement, String mStringContent, String mContentType) {
        mobileElement.sendKeys(mStringContent);
        if (mContentType.toLowerCase().equals("string")) {
            //Why using this hideous try-catch block? Simple, Keyboard does appears for real devices while emulator don't. And the way it was described I don't have to know what we're dealing with - and at this point I really don't care. Feel free to change it.
            try {
                this.webDriverAppiumHelper.getAppiumDriver().hideKeyboard();
            } catch (Exception e) {
                testLog.logIt(e.getMessage());
            }
        }
    }

    /***
     * Simple helper to set a boolean value in checked property of web elements.
     * @param mobileElement
     * @param mBooleanContent
     * @param mContentType
     */
    private void setAppiumBoolean(MobileElement mobileElement, boolean mBooleanContent, String mContentType) {
        boolean mobileElementState = Boolean.valueOf(mobileElement.getAttribute("checked"));
        if (mBooleanContent && !mobileElementState) {
            this.testLog.logIt("Setting element as [true].");
            mobileElement.click();
        } else if (!mBooleanContent && mobileElementState) {
            this.testLog.logIt("Setting element as [false].");
            mobileElement.click();
        } else {
            this.testLog.logIt(String.format("Element is already [%1$s].", String.valueOf(mobileElement.isSelected())));
        }
    }

    /**
     * Simple helper to set a string value into a web element.
     *
     * @param locator
     * @param mStringContent
     * @param mSelector
     * @throws Exception
     */
    private void setSeleniumString(String locator, String mStringContent, String mSelector) throws Exception {
        this.webDriverSeleniumHelper.first(false, returnSelectorType(locator), mSelector).sendKeys(mStringContent);
    }

    /**
     * Simple helper to set a boolean value in checked property of web elements.
     *
     * @param locator
     * @param mBooleanContent
     * @param mSelector
     * @throws Exception
     */
    private void setSeleniumBoolean(String locator, boolean mBooleanContent, String mSelector) throws Exception {
        boolean webElement = Boolean.valueOf(this.webDriverSeleniumHelper.first(false, returnSelectorType(locator), mSelector).getAttribute("checked"));

        if (mBooleanContent && !webElement) {
            this.testLog.logIt("Setting element as [true].");
            this.webDriverSeleniumHelper.first(false, returnSelectorType(locator), mSelector).click();
        } else if (!mBooleanContent && webElement) {
            this.testLog.logIt("Setting element as [false].");
            this.webDriverSeleniumHelper.first(false, returnSelectorType(locator), mSelector).click();
        } else {
            this.testLog.logIt(String.format("Element is already [%1$s].", String.valueOf(webElement)));
        }
    }

    /**
     * Helper that will set a random value directly or adding it into a string format.
     *
     * @param baseValueForStringFormat
     * @param randomValueLength
     * @param randomValueType
     * @param useStringFormat
     * @throws Exception
     */
    private String GenerateRandomValueForDataDriven(Integer randomValueLength, String randomValueType, boolean useStringFormat, String baseValueForStringFormat) throws Exception {
        String finalValue = "";
        switch (randomValueType) {
            case "alphanumeric":
                finalValue = useStringFormat ? String.format(baseValueForStringFormat, RandomValuesHelper.generateRandomAlphanumeric(randomValueLength)) : RandomValuesHelper.generateRandomAlphanumeric(randomValueLength);
                break;
            case "alphabetic":
                finalValue = useStringFormat ? String.format(baseValueForStringFormat, RandomValuesHelper.generateAlphabetic(randomValueLength)) : RandomValuesHelper.generateAlphabetic(randomValueLength);
                break;
            case "numeric":
                finalValue = useStringFormat ? String.format(baseValueForStringFormat, RandomValuesHelper.generateRandomNumeric(randomValueLength)) : RandomValuesHelper.generateRandomNumeric(randomValueLength);
                break;
            default:
                throw new Exception(String.format("The random value type '%1$s' is not supported yet!", randomValueType));
        }
        return finalValue;
    }

    /**
     * Helper that extracts and process a String value from data driven accordingly its settings.
     *
     * @param statement
     * @return
     * @throws Exception
     */
    private String extractAndProcessStringValueFromDataDriven(Statement statement) throws Exception {
        String finalValue = "";
        if (statement.contentModifier != null && !statement.contentModifier.isEmpty()) {
            if (statement.contentModifier.equals("sortValueFromArray")) {
                int randomChosenIndex = statement.content.length == 1 ? 0 : (RandomValuesHelper.generateRandomInt(0, statement.content.length - 1));//Remember that this is a 0 based index.
                finalValue = (String) statement.content[randomChosenIndex];
            } else if (statement.contentModifier.equals("stringFormatValue") || statement.contentModifier.equals("generateRandomValue")) {
                finalValue = GenerateRandomValueForDataDriven(statement.randomValueMaxLengthForContent, statement.randomValueCompositionForContent, statement.contentModifier.equals("stringFormatValue"), statement.content == null ? "" : (String) statement.content[0]);
            } else {//Use the original content
                throw new Exception(String.format("If you intent to use Content Modifier, it should be 'stringFormatValue' or 'generateRandomValue', instead you've set:'%1$s'", statement.contentModifier));
            }
        } else {//Use the original content
            finalValue = (String) statement.content[0];
        }
        return finalValue;
    }
}