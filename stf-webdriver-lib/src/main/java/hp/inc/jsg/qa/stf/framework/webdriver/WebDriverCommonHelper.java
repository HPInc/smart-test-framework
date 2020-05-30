package hp.inc.jsg.qa.stf.framework.webdriver;


import hp.inc.jsg.qa.stf.enums.webdriver.SelectorType;
import hp.inc.jsg.qa.stf.enums.webdriver.WebDriverAction;
import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.misc.RandomValuesHelper;
import hp.inc.jsg.qa.stf.framework.wait.WaitHelper;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class WebDriverCommonHelper {

    //region PROPERTIES
    protected TestLog testLog;

    /*
    Default Waits and delays
     */
    private final int DEFAULT_WEBDRIVER_WAIT_TIMEOUT_SECONDS = 15;
    private final int DEFAULT_WEBDRIVER_SAFE_WAIT_TIMEOUT_SECONDS = 3;
    private final int DEFAULT_WEBDRIVER_JAVASCRIPT_TIMEOUT_SECONDS = 30;
    protected int DEFAULT_FOR_DELAY_BETWEEN_ACTIONS_MS = 500;
    private int DEFAULT_WAIT_BETWEEN_ANGULARJS_ACTIONS_MS = 1000;
    private int DEFAULT_WAIT_FOR_HIGHLIGHT_MS = 200;
    private final int DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT = 5;
    private final String DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_FIRST = "red";
    private final String DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_QUERY = "yellow";

    public Platform getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(Platform devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    private Platform devicePlatform;

    /*
    Web Drivers
     */
    private WebDriver seleniumDriver;
    private AppiumDriver appiumDriver;

    /*
    HELPER PROPERTIES
     */

    private int waitTimeoutSeconds;

    private String webDriverRemoteAddressURL;
    private boolean angularJsEnabled;

    int waitForJavaScriptTimeoutInSec;
    private boolean enableHighlightWebElementsOnFirst;
    private boolean enableHighlightWebElementsOnQuery;
    private boolean definitiveHighlightForFirst;
    private boolean definitiveHighlightForQuery;
    private int borderLengthForFirst;
    private int borderLengthForQuery;
    private String borderColorForFirst;
    private String borderColorForQuery;
    private int waitBetweenAngularActionsInMs;
    private int highlightForFirstTimeoutInMs;
    private int highlightForQueryTimeoutInMs;
    private boolean isAppiumTheMainUsage;

    //endregion

    //region GETS AND SETS

    public WebDriver getSeleniumDriver() {
        return seleniumDriver;
    }

    public void setSeleniumDriver(WebDriver seleniumDriver) {
        this.seleniumDriver = seleniumDriver;
    }

    public AppiumDriver getAppiumDriver() {
        return appiumDriver;
    }

    public void setAppiumDriver(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public void setAppiumTheMainUsage(boolean appiumTheMainUsage) {
        isAppiumTheMainUsage = appiumTheMainUsage;
    }

    /***
     * Configure the loggerQA.
     * @param testLog: LoggerQA class instance.
     */
    public void setLogger(TestLog testLog) {
        this.testLog = testLog;
    }

    /***
     * Configure the timeout to be used by WebDriverWait class.
     * @param waitTimeoutSeconds: The timeout in seconds.
     */
    public void setWebDriverWaitTimeoutSeconds(int waitTimeoutSeconds) {
        this.waitTimeoutSeconds = waitTimeoutSeconds != 0 && waitTimeoutSeconds != DEFAULT_WEBDRIVER_WAIT_TIMEOUT_SECONDS ? waitTimeoutSeconds : DEFAULT_WEBDRIVER_WAIT_TIMEOUT_SECONDS;
    }

    /***
     * Retrieve the waitTimeout property value.
     * @return the configured timeout.
     */
    public int getWaitTimeoutSeconds() {
        return this.waitTimeoutSeconds;
    }

    /***
     * Configure the appium Server Remote Address in order to start its WebDriver properly.
     * @param webDriverRemoteAddressURL: Where appium was installed?
     */
    public void setRemoteAddressURL(String webDriverRemoteAddressURL) {
        this.webDriverRemoteAddressURL = webDriverRemoteAddressURL;
    }

    /**
     * Setter that configures the timeout for javascript actions.
     *
     * @param waitForJavaScriptTimeoutInSec
     */
    public void setForJavaScriptTimeoutInSec(int waitForJavaScriptTimeoutInSec) {
        this.waitForJavaScriptTimeoutInSec = waitForJavaScriptTimeoutInSec;
    }

    /**
     * Simple getter.
     *
     * @return: the current value of the respective property.
     */
    public int getWaitForJavaScriptTimeoutInSec() {
        return this.waitForJavaScriptTimeoutInSec;
    }

    /**
     * Setter that configures how long to wait while interacting with AngularJS Web Elements that contains multiple inner WebElements such as Drop-Down Lists\Combo-Boxes.
     *
     * @param waitBetweenAngularActionsInMs
     */
    public void setWaitBetweenAngularActionsInMs(int waitBetweenAngularActionsInMs) {
        this.waitBetweenAngularActionsInMs = waitBetweenAngularActionsInMs;
    }

    /**
     * Simple getter.
     *
     * @return: the current value of the respective property.
     */
    public int getWaitBetweenAngularActionsInMs() {
        return this.waitBetweenAngularActionsInMs;
    }

    /**
     * Enable or Disable HighlightWebElementsOnFirst feature.
     *
     * @param enable
     */
    public void setEnableHighlightWebElementsOnFirst(boolean enable) {
        this.enableHighlightWebElementsOnFirst = enable;
    }

    /**
     * Return current value of HighlightWebElementsOnFirst
     *
     * @return
     */
    public boolean getEnableHighlightWebElementsOnFirst() {
        return this.enableHighlightWebElementsOnFirst;
    }

    /**
     * Enable or Disable HighlightWebElementsOnQuery feature.
     *
     * @param enable
     */
    public void setEnableHighlightWebElementsOnQuery(boolean enable) {
        this.enableHighlightWebElementsOnQuery = enable;
    }

    /**
     * Return current value of HighlightWebElementsOnQuery
     *
     * @return
     */
    public boolean getEnableHighlightWebElementsOnQuery() {
        return this.enableHighlightWebElementsOnQuery;
    }

    //endregion

    //region JAVASCRIPT

    /***
     * Helper to execute javascript within an object.
     * @param object: object where selenium will perform the javascript.
     * @param javascript: The javascript code.
     * @param isAsync: True for Async operation, false for synchronous.
     * @throws InterruptedException
     */
    public void executeJavascript(Object object, String javascript, boolean isAsync) throws InterruptedException {
        executeJavascript(object, javascript, isAsync, 0);
    }

    /***
     * Helper to execute javascript within an object.
     * @param object: object where selenium will perform the javascript.
     * @param javascript: The javascript code.
     * @param isAsync: True for Async operation, false for synchronous.
     * @param waitAfterScriptSeconds: A wait after performing the script.
     * @throws InterruptedException
     */
    protected void executeJavascript(Object object, String javascript, boolean isAsync, int waitAfterScriptSeconds) throws InterruptedException {
        if (isAsync) {
            ((JavascriptExecutor) this.seleniumDriver).executeAsyncScript(javascript, object);
        } else {
            ((JavascriptExecutor) this.seleniumDriver).executeScript(javascript, object);
        }

        if (waitAfterScriptSeconds != 0) {
            new WaitHelper().waitMilliseconds(waitAfterScriptSeconds * 1000);
        }
    }

    /**
     * Helper that highlights a WebElement
     *
     * @param webElement:   WebElement to be highlighted.
     * @param borderLength: The border length.
     * @param borderColor:  The border color.
     * @throws InterruptedException
     */
    public void highlightWebElement(WebElement webElement, int borderLength, String borderColor) throws InterruptedException {
        List<WebElement> webElements = new ArrayList<>();
        webElements.add(webElement);
        highlightWebElements(webElements, borderLength, borderColor, true, 0);
    }

    /**
     * Helper that highlights a WebElement
     *
     * @param webElement:           WebElement to be highlighted.
     * @param borderLength:         The border length.
     * @param borderColor:          The border color.
     * @param highlightTimeoutInMs: How long the highlight will last.
     * @throws InterruptedException
     */
    public void highlightWebElement(WebElement webElement, int borderLength, String borderColor, int highlightTimeoutInMs) throws InterruptedException {
        List<WebElement> webElements = new ArrayList<>();
        webElements.add(webElement);
        highlightWebElements(webElements, borderLength, borderColor, false, highlightTimeoutInMs);
    }

    /**
     * Helper that highlights a list of WebElements
     *
     * @param webElements:  WebElements to be highlighted.
     * @param borderLength: The border length.
     * @param borderColor:  The border color.
     * @throws InterruptedException
     */
    public void highlightWebElements(List<WebElement> webElements, int borderLength, String borderColor) throws InterruptedException {
        highlightWebElements(webElements, borderLength, borderColor, true, 0);
    }

    /**
     * Helper that highlights a list of WebElements
     *
     * @param webElements:          WebElements to be highlighted.
     * @param borderLength:         The border length.
     * @param borderColor:          The border color.
     * @param highlightTimeoutInMs: How long the highlight will last.
     * @throws InterruptedException
     */
    public void highlightWebElements(List<WebElement> webElements, int borderLength, String borderColor, int highlightTimeoutInMs) throws InterruptedException {
        highlightWebElements(webElements, borderLength, borderColor, false, highlightTimeoutInMs);
    }

    /**
     * Helper that highlights a list of WebElements
     *
     * @param webElements:          WebElements to be highlighted.
     * @param borderLength:         The border length.
     * @param borderColor:          The border color.
     * @param leaveSet:             Leave the highlight set.
     * @param highlightTimeoutInMs: How long the highlight will last.
     * @throws InterruptedException
     */
    private void highlightWebElements(List<WebElement> webElements, int borderLength, String borderColor, boolean leaveSet, int highlightTimeoutInMs) throws InterruptedException {
        WaitHelper waitHelper = null;
        if (!leaveSet) {
            waitHelper = new WaitHelper();
        }

        for (WebElement webElement : webElements) {

            executeJavascript(webElement, String.format("arguments[0].style.border='%1$spx solid %2$s'", borderLength == 0 ? DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT : borderLength, borderColor == null || borderColor.isEmpty() ? DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_FIRST : borderColor), false);

        }

        if (!leaveSet) {
            //Wait just a few
            waitHelper.waitMilliseconds(highlightTimeoutInMs == 0 ? this.DEFAULT_WAIT_FOR_HIGHLIGHT_MS : highlightTimeoutInMs);

            for (WebElement webElement : webElements) {
                //Remove the border
                executeJavascript(webElement, "arguments[0].style.border=''", false);
            }
        }
    }

    /**
     * This helper configured how the highlight feature will behave when WebElements are located using QUERY.
     *
     * @param definitiveHighlight:Will leave the web element highlighted
     * @param borderLength:            The border length.
     * @param borderColor:             The border color.
     */
    public void configureHighlightWebElementsOnQuery(boolean definitiveHighlight, int borderLength, String borderColor) {
        configureHighlightWebElementsOnQuery(definitiveHighlight, borderLength, borderColor, 0);
    }

    /**
     * This helper configured how the highlight feature will behave when WebElements are located using QUERY.
     *
     * @param borderLength:         The border length.
     * @param borderColor:          The border color.
     * @param highlightTimeoutInMs: How long the highlight will last.
     */
    public void configureHighlightWebElementsOnQuery(int borderLength, String borderColor, int highlightTimeoutInMs) {
        configureHighlightWebElementsOnQuery(false, borderLength, borderColor, highlightTimeoutInMs);
    }

    /**
     * This helper configured how the highlight feature will behave when WebElements are located using QUERY.
     *
     * @param definitiveHighlight:Will leave the web element highlighted
     * @param borderLength:            The border length.
     * @param borderColor:             The border color.
     * @param highlightTimeoutInMs:    How long the highlight will last.
     */
    private void configureHighlightWebElementsOnQuery(boolean definitiveHighlight, int borderLength, String borderColor, int highlightTimeoutInMs) {
        this.definitiveHighlightForQuery = definitiveHighlight;
        this.borderColorForQuery = borderColor.isEmpty() ? DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_QUERY : borderColor;
        this.borderLengthForQuery = borderLength == 0 ? DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT : borderLength;
        this.highlightForQueryTimeoutInMs = highlightTimeoutInMs;
    }

    /**
     * This helper configured how the highlight feature will behave when WebElements are located using FIRST.
     *
     * @param definitiveHighlight:Will leave the web element highlighted
     * @param borderLength:            The border length.
     * @param borderColor:             The border color.
     */
    public void configureHighlightWebElementsOnFirst(boolean definitiveHighlight, int borderLength, String borderColor) {
        configureHighlightWebElementsOnFirst(definitiveHighlight, borderLength, borderColor, 0);
    }

    /**
     * This helper configured how the highlight feature will behave when WebElements are located using FIRST.
     *
     * @param borderLength:         The border length.
     * @param borderColor:          The border color.
     * @param highlightTimeoutInMs: How long the highlight will last.
     */
    public void configureHighlightWebElementsOnFirst(int borderLength, String borderColor, int highlightTimeoutInMs) {
        configureHighlightWebElementsOnFirst(false, borderLength, borderColor, highlightTimeoutInMs);
    }

    /**
     * This helper configured how the highlight feature will behave when a WebElement is located using FIRST.
     *
     * @param definitiveHighlight:Will leave the web element highlighted
     * @param borderLength:            The border length.
     * @param borderColor:             The border color.
     * @param highlightTimeoutInMs:    How long the highlight will last.
     */
    private void configureHighlightWebElementsOnFirst(boolean definitiveHighlight, int borderLength, String borderColor, int highlightTimeoutInMs) {
        this.definitiveHighlightForFirst = definitiveHighlight;
        this.borderColorForFirst = borderColor.isEmpty() ? DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_FIRST : borderColor;
        this.borderLengthForFirst = borderLength == 0 ? DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT : borderLength;
        this.highlightForFirstTimeoutInMs = highlightTimeoutInMs;
    }

    //endregion

    //region MAIN WEB-DRIVER CONDITION HELPERS

    /***
     * Main helper to navigate.
     * @param url: The destination URL.
     * @throws Exception
     */
    public void navigate(String url) throws Exception {
        testLog.logIt("Navigating to: " + url);
        if (!isAppiumTheMainUsage) {
            this.seleniumDriver.navigate().to(url);
        } else {
            this.appiumDriver.navigate().to(url);
        }
    }

    /***
     * Helper that will wait for an element exists during an interval of time.
     * @param selectorId: The WebElement selector id.
     * @throws Exception
     */
    public boolean elementIsVisible(String selectorId) throws Exception {
        return elementIsVisible(SelectorType.BY_ID, selectorId, DEFAULT_WEBDRIVER_SAFE_WAIT_TIMEOUT_SECONDS);
    }

    /***
     * Helper that will wait for an element exists during an interval of time.
     * @param selectorId: The WebElement selector id.
     * @param waitTimeout: How long it should keep trying to find the element.
     * @throws Exception
     */
    public boolean elementIsVisible(String selectorId, int waitTimeout) throws Exception {
        return elementIsVisible(SelectorType.BY_ID, selectorId, waitTimeout);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param selectorType: What selector type?
     * @param selectorId: The WebElement selector id.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public boolean elementIsVisible(SelectorType selectorType, String selectorId, int waitTimeout) throws Exception {
        return elementIsVisible(selectorType, waitTimeout, selectorId);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param by : The selenium find selector strategy.
     * @param waitTimeout : For how long - in seconds.
     * @throws Exception
     */
    public boolean elementIsVisible(By by, int waitTimeout) throws Exception {
        return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(by), waitTimeout);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param selectorType: What selector type?
     * @param selector_or_SelectorAndText: The WebElement selector id.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public boolean elementIsVisible(SelectorType selectorType, int waitTimeout, String... selector_or_SelectorAndText) throws Exception {
        switch (selectorType) {
            case BY_ID:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.id(selector_or_SelectorAndText[0])), waitTimeout);
            case BY_XPATH:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.xpath(selector_or_SelectorAndText[0])), waitTimeout);
            case BY_CLASS_NAME:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.className(selector_or_SelectorAndText[0])), waitTimeout);
            case BY_CSS:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector_or_SelectorAndText[0])), waitTimeout);
            case BY_NAME:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.name(selector_or_SelectorAndText[0])), waitTimeout);
            case BY_TAG_NAME:
                return safeWaitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.tagName(selector_or_SelectorAndText[0])), waitTimeout);
            default:
                throw new Exception(String.format("Selector type %1$s is not implemented yet.", selectorType.toString()));
        }
    }

    /**
     * Helper that will wait for an element does exist in the page during an interval of time.
     *
     * @param by                 : The selenium find selector strategy.
     * @param waitTimeoutSeconds
     * @return
     * @throws Exception
     */
    public boolean elementExists(By by, int waitTimeoutSeconds) throws Exception {
        return safeWaitUntil(true, ExpectedConditions.presenceOfElementLocated(by), waitTimeoutSeconds);
    }

    /**
     * Helper that will wait for an element does exist in the page during an interval of time.
     *
     * @param selectorType
     * @param selector
     * @param waitTimeoutSeconds
     * @return
     * @throws Exception
     */
    public boolean elementExists(SelectorType selectorType, String selector, int waitTimeoutSeconds) throws Exception {
        return safeWaitUntil(true, ExpectedConditions.presenceOfElementLocated((By) returnLocator(selectorType, selector)), waitTimeoutSeconds);
    }

    /***
     * Helper that wait for a given condition.
     * @param expectedCondition: What condition wait for.
     * @param isTheExpectedConditionAWebElement : True for webElement condition, false for Boolean condition.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public boolean conditionExists(Object expectedCondition, boolean isTheExpectedConditionAWebElement, int waitTimeout) throws Exception {
        return safeWaitUntil(isTheExpectedConditionAWebElement, expectedCondition, waitTimeout);
    }
    //endregion

    //region MAIN WEB-DRIVER FIRSTS, QUERIES and SOME OTHER HELPERS

    /**
     * Helper to query web elements.
     *
     * @param by
     * @return A list of all web elements that does match the search criteria.
     * @throws Exception
     */
    public List<WebElement> query(By by) throws Exception {
        List<WebElement> webElements;

        webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(by) : this.appiumDriver.findElements(by);

        if (enableHighlightWebElementsOnQuery) {
            highlightWebElements(webElements, this.borderLengthForQuery, this.borderColorForQuery, this.definitiveHighlightForQuery, this.highlightForQueryTimeoutInMs);
        }
        return webElements;

    }

    /***
     * Helper to query web elements.
     * @param selectorType: What selector type?
     * @param selector: The selector value.
     * @return A list of all web elements that does match the search criteria.
     * @throws Exception
     */
    public List<WebElement> query(SelectorType selectorType, String... selector) throws Exception {
        List<WebElement> webElements;

        switch (selectorType) {
            case BY_ID:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.id(selector[0])) : this.appiumDriver.findElements(By.id(selector[0]));
                break;
            case BY_XPATH:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.xpath(selector[0])) : this.appiumDriver.findElements(By.xpath(selector[0]));
                break;
            case BY_CLASS_NAME:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.className(selector[0])) : this.appiumDriver.findElements(By.className(selector[0]));
                break;
            case BY_CSS:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.cssSelector(selector[0])) : this.appiumDriver.findElements(By.cssSelector(selector[0]));
                break;
            case BY_NAME:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.name(selector[0])) : this.appiumDriver.findElements(By.name(selector[0]));
                break;
            case BY_LINK:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.linkText(selector[0])) : this.appiumDriver.findElements(By.linkText(selector[0]));
                break;
            case BY_PARTIAL_LINK:
                webElements = !isAppiumTheMainUsage ? this.seleniumDriver.findElements(By.partialLinkText(selector[0])) : this.appiumDriver.findElements(By.partialLinkText(selector[0]));
                break;
            default:
                throw new Exception(String.format("Selector type %1$s not implemented yet.", selectorType.toString()));
        }

        if (enableHighlightWebElementsOnQuery) {
            highlightWebElements(webElements, this.borderLengthForQuery, this.borderColorForQuery, this.definitiveHighlightForQuery, this.highlightForQueryTimeoutInMs);
        }
        return webElements;
    }

    /***
     * Helper to find an web element using ID as selector type.
     * @param selectorId: The WebElement selector id.
     * @return The WebElement it self.
     * @throws Exception
     */
    public WebElement first(String selectorId) throws Exception {
        return first(false, SelectorType.BY_ID, selectorId);
    }

    /***
     * TODO REALLY NEED TO INVESTIGATE IF THIS ONE CAN BE REPLACED BY ANY OTHER EXISTENT SELECTORS.
     * This helper should be used for locating CSS selector through its label.
     * It's a sort of primitive ByAngularCssContainingText
     * @param cssSelector: The css selector.
     * @param elementTextValueOrLabel: The css label.
     * @param useEqualsForComparing:  Use equals, otherwise Contains.
     * @return An WebElement or a list of it.
     * @throws Exception
     */
    public WebElement first(String cssSelector, String elementTextValueOrLabel, boolean useEqualsForComparing) throws Exception {
        Object obj = findElementsByCssContainingText(cssSelector, elementTextValueOrLabel, true, useEqualsForComparing);

        WebElement webElement = null;

        if (obj instanceof WebElement) {
            webElement = (WebElement) obj;
        } else {
            List<WebElement> l = (List<WebElement>) obj;

            if (l.size() != 0) {
                webElement = l.get(0);
            }
        }

        if (webElement == null) {
            throw new Exception(String.format("No element containing [text/value:%1$s] using By.CSS locator was found!\nCheck logs above to see what elements were found instead.", elementTextValueOrLabel));
        }
        return webElement;
    }

    /***
     * TODO REALLY NEED TO INVESTIGATE IF THIS ONE CAN BE REPLACED BY ANY OTHER EXISTENT SELECTORS.
     * This helper should be used for locating CSS selector through its label.
     * It's a sort of primitive ByAngularCssContainingText
     * @param cssSelector: The css selector.
     * @param elementTextValueOrLabel: The css label.
     * @param useEqualsForComparing:  Use equals, otherwise Contains.
     * @return An WebElement or a list of it.
     * @throws Exception
     */
    public List<WebElement> query(String cssSelector, String elementTextValueOrLabel, boolean useEqualsForComparing) throws Exception {
        List<WebElement> webElementList = (List<WebElement>) findElementsByCssContainingText(cssSelector, elementTextValueOrLabel, false, useEqualsForComparing);

        if (webElementList == null || webElementList.size() == 0) {
            throw new Exception(String.format("No elements containing [text/value:%1$s] using By.CSS locator was found!\nCheck logs above to see what elements were found instead.", elementTextValueOrLabel));
        }
        return webElementList;
    }

    /***
     * TODO REALLY NEED TO INVESTIGATE IF THIS ONE CAN BE REPLACED BY ANY OTHER EXISTENT SELECTORS.
     * This helper should be used for locating CSS selector through its label.
     * It's a sort of primitive ByAngularCssContainingText
     * @param cssSelector: The css selector.
     * @param cssNameOrLabel: The css label.
     * @param stopOnFirstFound: If true, the first element found will be returned - ending the search.
     * @param useEquals:  Use equals, otherwise Contains.
     * @return An WebElement or a list of it.
     * @throws Exception
     */
    private Object findElementsByCssContainingText(String cssSelector, String cssNameOrLabel, boolean stopOnFirstFound, boolean useEquals) throws Exception {

        List<WebElement> finalItems = new ArrayList<>();
        List<WebElement> tempItems = query(SelectorType.BY_CSS, cssSelector);

        for (Integer i = 0; i < tempItems.size(); i++) {
            String itemValue = tempItems.get(i).getText();
            testLog.logIt(String.format("CSS item number %1$s has this content: %2$s", String.valueOf(i), itemValue));
            if (useEquals) {
                if (itemValue.equals(cssNameOrLabel)) {
                    finalItems.add(tempItems.get(i));
                    if (stopOnFirstFound) {
                        return finalItems.get(0);
                    }
                }
            } else {
                if (itemValue.contains(cssNameOrLabel)) {
                    finalItems.add(tempItems.get(i));
                    if (stopOnFirstFound) {
                        return finalItems.get(0);
                    }
                }
            }
        }
        return finalItems;
    }

    /***
     * This helper should be used for locating web-elements.
     * @param by:  The selenium find selector strategy.
     * @return An WebElement or a list of it.
     * @throws Exception
     */
    public WebElement first(By by) throws Exception {
        return first(false, by);
    }

    /***
     * Helper to find an web element.
     * @param by:  The selenium find selector strategy.
     * @param returnNullIfNotFound: Will throw an exception if not found.
     * @return The WebElement it self.
     * @throws Exception
     */
    public WebElement first(boolean returnNullIfNotFound, By by) throws Exception {

        WebElement webElement;
        try {
            webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(by) : this.appiumDriver.findElement(by);

            if (enableHighlightWebElementsOnFirst) {
                List<WebElement> webElements = new ArrayList<>();
                webElements.add(webElement);
                highlightWebElements(webElements, this.borderLengthForFirst, this.borderColorForFirst, this.definitiveHighlightForFirst, this.highlightForFirstTimeoutInMs);
            }

            return webElement;

        } catch (
                Exception e) {
            if (returnNullIfNotFound) {
                //I've added this due to data driven that does perform batches of actions, if fails in the middle the internal stack trace from appium won't provide valuable information for triage.
                this.testLog.logIt(e.getMessage());
                return null;
            } else {
                throw new Exception(String.format("Selector [%1$s] was not found", by.toString()));
            }
        }

    }

    /***
     * This helper should be used for locating web-elements.
     * @param selectorType: What selector type?
     * @param selector_or_SelectorAndText: The selector value.
     * @return An WebElement or a list of it.
     * @throws Exception
     */
    public WebElement first(SelectorType selectorType, String... selector_or_SelectorAndText) throws Exception {
        return first(false, selectorType, selector_or_SelectorAndText);
    }

    /***
     * Helper to find an web element.
     * @param selectorType: What selector type?
     * @param returnNullIfNotFound: Will throw an exception if not found.
     *@param selectors : The selector or selectors (in case of css containing text)
     * @return The WebElement it self.
     * @throws Exception
     */
    public WebElement first(boolean returnNullIfNotFound, SelectorType selectorType, String... selectors) throws Exception {

        WebElement webElement;

        try {
            switch (selectorType) {
                case BY_ID:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.id(selectors[0])) : this.appiumDriver.findElement(By.id(selectors[0]));
                    break;
                case BY_XPATH:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.xpath(selectors[0])) : this.appiumDriver.findElement(By.xpath(selectors[0]));
                    break;
                case BY_CLASS_NAME:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.className(selectors[0])) : this.appiumDriver.findElement(By.className(selectors[0]));
                    break;
                case BY_CSS:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.cssSelector(selectors[0])) : this.appiumDriver.findElement(By.cssSelector(selectors[0]));
                    break;
                case BY_NAME:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.name(selectors[0])) : this.appiumDriver.findElement(By.name(selectors[0]));
                    break;
                case BY_TAG_NAME:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.tagName(selectors[0])) : this.appiumDriver.findElement(By.tagName(selectors[0]));
                    break;
                case BY_LINK:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.linkText(selectors[0])) : this.appiumDriver.findElement(By.linkText(selectors[0]));
                    break;
                case BY_PARTIAL_LINK:
                    webElement = !isAppiumTheMainUsage ? this.seleniumDriver.findElement(By.partialLinkText(selectors[0])) : this.appiumDriver.findElement(By.partialLinkText(selectors[0]));
                    break;
                default:
                    throw new Exception(String.format("Selector type %1$s not implemented yet.", selectorType.toString()));
            }

            if (enableHighlightWebElementsOnFirst) {
                List<WebElement> webElements = new ArrayList<>();
                webElements.add(webElement);
                highlightWebElements(webElements, this.borderLengthForFirst, this.borderColorForFirst, this.definitiveHighlightForFirst, this.highlightForFirstTimeoutInMs);
            }

            return webElement;

        } catch (Exception e) {
            if (returnNullIfNotFound) {
                //I've added this due to data driven that does perform batches of actions, if fails in the middle the internal stack trace from appium won't provide valuable information for triage.
                this.testLog.logIt(e.getMessage());
                return null;
            } else {
                throw new Exception(String.format("Selector [%1$s] was not found using selector type [%2$s].", selectors[0], selectorType.toString()));
            }
        }
    }

    /**
     * Helper that performs a Find Element after a condition is met.
     *
     * @param by:                      The selenium find selector strategy.
     * @param expectedCondition
     * @param timeoutForConditionInSec
     * @return
     * @throws Exception
     */
    public WebElement firstByCondition(By by, boolean isWebElementTheConditionSubType, Object expectedCondition, int timeoutForConditionInSec) throws Exception {
        waitUntil(isWebElementTheConditionSubType, expectedCondition, timeoutForConditionInSec);
        return first(by);
    }

    /**
     * Helper that performs a Query after a condition is met.
     *
     * @param by:                      The selenium find selector strategy.
     * @param expectedCondition
     * @param timeoutForConditionInSec
     * @return
     * @throws Exception
     */
    public List<WebElement> queryByCondition(By by, boolean isWebElementTheConditionSubType, Object expectedCondition, int timeoutForConditionInSec) throws Exception {
        waitUntil(isWebElementTheConditionSubType, expectedCondition, timeoutForConditionInSec);
        return query(by);
    }

    /**
     * Helper that performs a Find Element after a condition is met.
     *
     * @param selectorType
     * @param selector
     * @param expectedCondition
     * @param timeoutForConditionInSec
     * @return
     * @throws Exception
     */
    public WebElement firstByCondition(SelectorType selectorType, String selector, boolean isWebElementTheConditionSubType, Object expectedCondition, int timeoutForConditionInSec) throws Exception {
        waitUntil(isWebElementTheConditionSubType, expectedCondition, timeoutForConditionInSec);
        return first(selectorType, selector);
    }

    /**
     * Helper that performs a Query after a condition is met.
     *
     * @param selectorType
     * @param selector
     * @param expectedCondition
     * @param timeoutForConditionInSec
     * @return
     * @throws Exception
     */
    public List<WebElement> queryByCondition(SelectorType selectorType, String selector, boolean isWebElementTheConditionSubType, Object expectedCondition, int timeoutForConditionInSec) throws Exception {
        waitUntil(isWebElementTheConditionSubType, expectedCondition, timeoutForConditionInSec);
        return query(selectorType, selector);
    }

    /***
     *  This helper takes screenshot of the WebDriver instance at the current page.
     * @param pathToStoreImage
     * @throws IOException
     */
    public String saveScreenshot(String pathToStoreImage) throws IOException {
        File scrFile = takeScreenshot();
        String finalDestination = pathToStoreImage + scrFile.getName();
        FileUtils.moveFile(scrFile, new File(finalDestination));
        testLog.logIt(String.format("The screenshot was saved at:%1$s", finalDestination));
        return finalDestination;
    }

    /**
     * Helper that take a screenshot using web driver.
     *
     * @return
     */
    private File takeScreenshot() {
        return isAppiumTheMainUsage ? this.appiumDriver.getScreenshotAs(OutputType.FILE) : ((TakesScreenshot) this.seleniumDriver).getScreenshotAs(OutputType.FILE);
    }

    /**
     * Helper to take screenshot from a specific web element.
     *
     * @param webElement
     * @param whereToSaveIt
     * @return
     * @throws IOException
     */
    public String takeScreenshotFromWebElement(WebElement webElement, String whereToSaveIt) throws IOException {
        testLog.logIt("Get entire page screenshot");
        File screenshot = takeScreenshot();
        BufferedImage fullImg = ImageIO.read(screenshot);

        testLog.logIt("Get the location of element on the page");
        Point point = webElement.getLocation();

        testLog.logIt("Get width and height of the element");
        int eleWidth = webElement.getSize().getWidth();
        int eleHeight = webElement.getSize().getHeight();

        testLog.logIt("Crop the entire page screenshot to get only element screenshot");
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);

        testLog.logIt("Copy the element screenshot to disk");
        String location = whereToSaveIt + RandomValuesHelper.generateRandomAlphanumeric(10) + ".png";
        File screenshotLocation = new File(location);
        FileUtils.copyFile(screenshot, screenshotLocation);
        return location;
    }

    /**
     * Helper that switches to the next open tab.
     *
     * @param tabIndex
     */
    public void switchTabs(int tabIndex) {
        ArrayList<String> tabs = new ArrayList<String>(this.seleniumDriver.getWindowHandles());
        this.testLog.logIt("Tabs available are " + tabs);
        this.testLog.logIt("Switching to tab " + tabs.get(tabIndex));
        this.seleniumDriver.switchTo().window(tabs.get(tabIndex));
    }

    /**
     * Helper that counts the number of open tabs.
     */
    public int countTabs() {
        ArrayList<String> tabs = new ArrayList<String>(this.seleniumDriver.getWindowHandles());
        return tabs.size();
    }

    /**
     * Helper that closes a tab and set context back to the previous one.
     *
     * @param tabIndex
     */
    public void closeTab(int tabIndex) {
        switchTabs(tabIndex);
        this.seleniumDriver.close();
        switchTabs(countTabs() - 1);
    }

    //endregion

    //region MAIN WEB-DRIVER WAIT HELPERS

    /***
     * Helper that will wait for an element exists during an interval of time.
     * @param selectorId: The WebElement selector id.
     * @throws Exception
     */
    public void waitForElementBecomeVisible(String selectorId) throws Exception {
        waitForElementBecomeVisible(SelectorType.BY_ID, selectorId, DEFAULT_WEBDRIVER_WAIT_TIMEOUT_SECONDS);
    }

    /***
     * Helper that will wait for an element exists during an interval of time.
     * @param selectorId: The WebElement selector id.
     * @param waitTimeout: How long it should keep trying to find the element.
     * @throws Exception
     */
    public void waitForElementBecomeVisible(String selectorId, int waitTimeout) throws Exception {
        waitForElementBecomeVisible(SelectorType.BY_ID, selectorId, waitTimeout);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param selectorType: What selector type?
     * @param selectorId: The WebElement selector id.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public void waitForElementBecomeVisible(SelectorType selectorType, String selectorId, int waitTimeout) throws Exception {
        waitForElementBecomeVisible(selectorType, waitTimeout, selectorId);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param by: The selenium find selector strategy.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public void waitForElementBecomeVisible(By by, int waitTimeout) throws Exception {
        waitUntil(true, ExpectedConditions.visibilityOfElementLocated(by), waitTimeout);
    }

    /***
     * Helper that will wait for an element become visible during an interval of time.
     * @param selectorType: What selector type?
     * @param selector_or_SelectorAndText: The WebElement selector id.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public void waitForElementBecomeVisible(SelectorType selectorType, int waitTimeout, String... selector_or_SelectorAndText) throws Exception {
        switch (selectorType) {
            case BY_ID:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.id(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_XPATH:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.xpath(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_CLASS_NAME:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.className(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_CSS:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_NAME:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.name(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_TAG_NAME:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.tagName(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_LINK:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.linkText(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            case BY_PARTIAL_LINK:
                waitUntil(true, ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(selector_or_SelectorAndText[0])), waitTimeout);
                break;
            default:
                throw new Exception(String.format("Selector type %1$s is not implemented yet.", selectorType.toString()));
        }
    }

    /**
     * Helper that will wait until an specific element exists in the page - it does not matter what attributes it will have.
     *
     * @param by:                The selenium find selector strategy.
     * @param waitTimeoutSeconds
     * @throws Exception
     */
    public void waitForElementExists(By by, int waitTimeoutSeconds) throws Exception {
        waitForCondition(ExpectedConditions.presenceOfElementLocated(by), true, waitTimeoutSeconds);
    }

    /**
     * Helper that will wait until an specific element exists in the page - it does not matter what attributes it will have.
     *
     * @param selectorType
     * @param selector
     * @param waitTimeoutSeconds
     * @throws Exception
     */
    public void waitForElementExists(SelectorType selectorType, String selector, int waitTimeoutSeconds) throws Exception {
        waitForCondition(ExpectedConditions.presenceOfElementLocated((By) returnLocator(selectorType, selector)), true, waitTimeoutSeconds);
    }

    /***
     * Helper that wait for a given condition.
     * @param expectedCondition: What condition wait for.
     * @param isTheExpectedConditionAWebElement : True for webElement condition, false for Boolean condition.
     * @param waitTimeout: For how long - in seconds.
     * @throws Exception
     */
    public void waitForCondition(Object expectedCondition, boolean isTheExpectedConditionAWebElement, int waitTimeout) throws Exception {
        waitUntil(isTheExpectedConditionAWebElement, expectedCondition, waitTimeout);
    }

    /***
     * Main helper that performs WebDriver wait until native method.
     * @param expectedCondition: What Expected Condition WebElement\Boolean to wait.
     * @param waitTimeout: For how long - in seconds.
     */
    private void waitUntil(boolean isWebElementTheConditionSubType, Object expectedCondition, int waitTimeout) throws Exception {
        WebDriverWait webDriverWait = new WebDriverWait(isAppiumTheMainUsage ? this.appiumDriver : this.seleniumDriver, (long) waitTimeout);
        if (isWebElementTheConditionSubType) {
            webDriverWait.until((ExpectedCondition<WebElement>) expectedCondition);
        } else {
            webDriverWait.until((ExpectedCondition<Boolean>) expectedCondition);
        }
    }

    /***
     * Main helper that performs WebDriver wait until, but returns false if not found instead of throw new exception.
     * @param expectedCondition: What condition to wait.
     * @param waitTimeout: For how long - in seconds.
     */
    private boolean safeWaitUntil(boolean isWebElementTheConditionSubType, Object expectedCondition, int waitTimeout) throws Exception {
        WebDriverWait webDriverWait = new WebDriverWait(isAppiumTheMainUsage ? this.appiumDriver : this.seleniumDriver, (long) waitTimeout);
        try {
            if (isWebElementTheConditionSubType) {
                webDriverWait.until((ExpectedCondition<WebElement>) expectedCondition);
            } else {
                webDriverWait.until((ExpectedCondition<Boolean>) expectedCondition);
            }
            return true;
        } catch (Exception e) {
            this.testLog.logIt(e.getMessage());
        }
        return false;
    }

    //endregion

    //region MAIN ACTION HELPERS

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param xOffset: The X offset coordinate.
     * @param yOffset: The Y offset coordinate.
     * @throws Exception
     */
    public void actionClick(int xOffset, int yOffset) throws Exception {
        superAction(WebDriverAction.CLICK, null, null, xOffset, yOffset, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElement: The target webElement
     * @throws Exception
     */
    public void actionClick(WebElement webElement) throws Exception {
        superAction(WebDriverAction.EVALUATE_CLICK, webElement, null, 0, 0, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param xOffset: The X offset coordinate.
     * @param yOffset: The Y offset coordinate.
     * @throws Exception
     */
    public void actionDoubleClick(int xOffset, int yOffset) throws Exception {
        superAction(WebDriverAction.DOUBLE_CLICK, null, null, xOffset, yOffset, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElement: The target webElement
     * @throws Exception
     */
    public void actionDoubleClick(WebElement webElement) throws Exception {
        superAction(WebDriverAction.EVALUATE_DOUBLE_CLICK, webElement, null, 0, 0, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param xOffset: The X offset coordinate.
     * @param yOffset: The Y offset coordinate.
     * @throws Exception
     */
    public void actionContextClick(int xOffset, int yOffset) throws Exception {
        superAction(WebDriverAction.CONTEXT_CLICK, null, null, xOffset, yOffset, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElement: The target webElement
     * @throws Exception
     */
    public void actionContextClick(WebElement webElement) throws Exception {
        superAction(WebDriverAction.EVALUATE_CONTEXT_CLICK, webElement, null, 0, 0, null);
    }

    /**
     * Helper that perform move the mouse cursor over a element (hover action)
     *
     * @param webElement: The target webElement
     * @throws Exception
     */
    public void actionMoveToElement(WebElement webElement) throws Exception {
        superAction(WebDriverAction.MOVE_TO_ELEMENT, webElement, null, 0, 0, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param xOffset:       The X offset coordinate.
     * @param yOffset:       The Y offset coordinate.
     * @param charSequences: Sequence of chars to be input.
     * @throws Exception
     */
    public void actionSendKeys(int xOffset, int yOffset, CharSequence... charSequences) throws Exception {
        superAction(WebDriverAction.SEND_KEYS, null, null, xOffset, yOffset, charSequences);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElement:    The target webElement
     * @param charSequences: Sequence of chars to be input.
     * @throws Exception
     */
    public void actionSendKeys(WebElement webElement, CharSequence... charSequences) throws Exception {
        superAction(WebDriverAction.EVALUATE_SEND_KEYS, webElement, null, 0, 0, charSequences);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElement: The target webElement
     * @param xOffset:    The X offset coordinate.
     * @param yOffset:    The Y offset coordinate.
     * @throws Exception
     */
    public void actionDragAndDropBy(WebElement webElement, int xOffset, int yOffset) throws Exception {
        superAction(WebDriverAction.DRAG_AND_DROP_BY, webElement, null, xOffset, yOffset, null);
    }

    /**
     * Helper that perform actions WITHOUT USING web-driver context, it will perform the action related to method name on active context.
     *
     * @param webElementSource:      The target webElement
     * @param webElementDestination: The destination webElement.
     * @throws Exception
     */
    public void actionDragAndDropSendKeys(WebElement webElementSource, WebElement webElementDestination) throws Exception {
        superAction(WebDriverAction.DRAG_AND_DROP, webElementSource, webElementDestination, 0, 0, null);
    }

    /**
     * Main Helper that perform actions WITHOUT USING web-driver context, it will perform the given action on active context.
     *
     * @param webDriverAction: What action should be executed by Action class.
     * @param webElement:      The target webElement
     * @param webElement2:     The destination webElement.
     * @param xOffset:         The X offset coordinate.
     * @param yOffset:         The Y offset coordinate.
     * @param charSequences:   Sequence of chars to be input.
     * @throws Exception
     */
    private void superAction(WebDriverAction webDriverAction, WebElement webElement, WebElement webElement2, int xOffset, int yOffset, CharSequence... charSequences) throws Exception {
        Actions actions = new Actions(this.seleniumDriver);
        switch (webDriverAction) {
            case CLICK:
            case CONTEXT_CLICK:
            case DOUBLE_CLICK:
            case SEND_KEYS:
                actions.moveByOffset(xOffset, yOffset).build().perform();
                switch (webDriverAction) {
                    case CLICK:
                        actions.click().build().perform();
                        break;
                    case CONTEXT_CLICK:
                        actions.contextClick().build().perform();
                        break;
                    case DOUBLE_CLICK:
                        actions.doubleClick().build().perform();
                    case SEND_KEYS:
                        actions.sendKeys(charSequences).build().perform();
                        break;
                }
                break;

            case EVALUATE_CLICK:
                actions.click(webElement).build().perform();
                break;

            case EVALUATE_CONTEXT_CLICK:
                actions.contextClick(webElement).build().perform();
                break;

            case EVALUATE_DOUBLE_CLICK:
                actions.doubleClick(webElement).build().perform();
                break;

            case DRAG_AND_DROP:
                actions.dragAndDrop(webElement, webElement2).build().perform();
                break;

            case DRAG_AND_DROP_BY:
                actions.dragAndDropBy(webElement, xOffset, yOffset).build().perform();
                break;

            case EVALUATE_SEND_KEYS:
                actions.sendKeys(webElement, charSequences).perform();
                break;

            case MOVE_TO_ELEMENT:
                actions.moveToElement(webElement).perform();
                break;

            default:
                throw new Exception(String.format("The action %1$s is not supported by this helper. For using it create a local instance of Actions class.", webDriverAction.toString()));
        }
    }

    //endregion

    //region INTERNAL PRIVATE MINOR HELPERS

    /**
     * Simple helper that returns By locator given a selector type and its respective selector.
     *
     * @param selectorType
     * @param selector
     * @return
     * @throws Exception
     */
    public Object returnLocator(SelectorType selectorType, String... selector) throws Exception {
        switch (selectorType) {
            case BY_CLASS_NAME:
                return By.className(selector[0]);
            case BY_CSS:
                return By.cssSelector(selector[0]);
            case BY_ID:
                return By.id(selector[0]);
            case BY_NAME:
                return By.name(selector[0]);
            case BY_XPATH:
                return By.xpath(selector[0]);
            case BY_TAG_NAME:
                return By.tagName(selector[0]);
            case BY_LINK:
                return By.linkText(selector[0]);
            case BY_PARTIAL_LINK:
                return By.partialLinkText(selector[0]);
            default:
                throw new Exception(String.format("The selector %1$s is not supported yet!", selectorType.toString()));
        }
    }

    /**
     * Simple helper to prevent repeating code in other main helpers.
     *
     * @param performDelay
     * @param delayBetweenActionsInMs
     * @throws InterruptedException
     */
    private void delayBetweenActions(boolean performDelay, double delayBetweenActionsInMs) throws InterruptedException {
        if (performDelay) {
            new WaitHelper().waitMilliseconds(delayBetweenActionsInMs);
        }
    }

    /**
     * Simple Helper to set a system property if key-value pair are provided correctly.
     *
     * @throws Exception
     */
    private void setSystemPropertyForWebDriver(String key, String value) {
        if ((key != null && !key.isEmpty()) && (value != null && !value.isEmpty())) {
            System.setProperty(key, value);
        } else {
            this.testLog.logIt("Cannot set System Property if both Key-Value pair does not have a content!!!");
        }
    }

    /**
     * Create a URL from a string.
     *
     * @param url
     * @return
     * @throws Exception
     */
    private URL createUrl(String url) throws Exception {
        if (!this.webDriverRemoteAddressURL.isEmpty()) {
            return new URL(url);
        } else {
            throw new Exception("The url cannot be empty!!!");
        }
    }

    /**
     * Simple helper to throw exceptions.
     *
     * @param exceptionDetails
     * @throws Exception
     */
    private void throwWebDriverStartMissingParametersException(String exceptionDetails) throws Exception {
        throw new Exception(exceptionDetails);
    }

    //endregion

    //region WEB-DRIVERS SETUP

    /***
     * Close and quit any active Appium WebDriver.
     */
    public void endAppiumWebDriverUsage() {
        closeAppiumWebDriver();
        quitAppiumWebDriver();
    }

    /***
     * Close and Quit any active Selenium WebDriver.
     */
    public void endSeleniumWebdriverUsage() {
        quitSeleniumWebDriver();
        closeSeleniumWebDriver();
    }

    /***
     * Close the SeleniumDriver.
     */
    public void closeSeleniumWebDriver() {
        testLog.logIt("Close Selenium Driver.");
        try {
            this.seleniumDriver.close();
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
        }
    }

    /**
     * Helper to close selenium web driver.
     */
    public void quitSeleniumWebDriver() {
        testLog.logIt("Quit Selenium Driver.");
        try {
            this.seleniumDriver.quit();
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
        }
    }

    /***
     * Close the AppiumDriver.
     */
    public void closeAppiumWebDriver() {
        testLog.logIt("Close Appium Driver.");
        try {
            this.appiumDriver.close();
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
        }
    }

    /***
     * Quit the AppiumDriver.
     */
    public void quitAppiumWebDriver() {
        testLog.logIt("Quit Appium Driver.");
        try {
            this.appiumDriver.quit();
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
        }
    }

    protected void warnStfUsage() {
        this.testLog.logIt("\n######## Remember ########\n You must to set all the required WebDrivers via respective setters, BEFORE using this helper class.\n##################");
    }
    //endregion

    //region DEPRECATED ITEMS

    /***
     * Helper that will highlight web elements located using QUERY
     * @param enableHighlightElements: Will enable the highlight feature.
     * @param definitiveHighlight: Will leave the web element highlighted, or just perform a sort of 'blink'.
     * @param borderLength: The border length.
     * @param borderColor: The border color.
     * @deprecated use configureHighlightWebElementsOnQuery(...) and setEnableHighlightWebElementsOnQuery(...) instead.
     */
    @Deprecated
    public void setHighlightWebElementsOnQuery(boolean enableHighlightElements, boolean definitiveHighlight, int borderLength, String borderColor) {
        if (enableHighlightElements) {
            this.enableHighlightWebElementsOnQuery = true;
            this.definitiveHighlightForQuery = definitiveHighlight;
            this.borderColorForQuery = borderColor.isEmpty() ? DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_QUERY : borderColor;
            this.borderLengthForFirst = borderLength == 0 ? DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT : borderLength;
        } else {
            this.enableHighlightWebElementsOnQuery = false;
        }
    }

    /***
     * Helper that will highlight web elements located using FIRST
     * @param enableHighlightElements: Will enable the highlight feature.
     * @param definitiveHighlight: Will leave the web element highlighted, or just perform a sort of 'blink'.
     * @param borderLength: The border length.
     * @param borderColor: The border color.
     * @deprecated use configureHighlightWebElementsOnFirst(...) and setEnableHighlightWebElementsOnFirst(...) instead.
     */
    @Deprecated
    public void setHighlightWebElementsOnFirst(boolean enableHighlightElements, boolean definitiveHighlight, int borderLength, String borderColor) {
        if (enableHighlightElements) {
            this.enableHighlightWebElementsOnFirst = true;
            this.definitiveHighlightForFirst = definitiveHighlight;
            this.borderColorForFirst = borderColor.isEmpty() ? DEFAULT_BORDER_COLOR_FOR_HIGHLIGHT_FIRST : borderColor;
            this.borderLengthForFirst = borderLength == 0 ? DEFAULT_BORDER_LENGTH_FOR_HIGHLIGHT : borderLength;
        } else {
            this.enableHighlightWebElementsOnFirst = false;
        }
    }

    //endregion

}