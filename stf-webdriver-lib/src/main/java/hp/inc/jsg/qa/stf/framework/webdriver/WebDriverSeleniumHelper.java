package hp.inc.jsg.qa.stf.framework.webdriver;

import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.wait.WaitHelper;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class WebDriverSeleniumHelper extends WebDriverCommonHelper {

    /***
     * Default constructor.
     */
    public WebDriverSeleniumHelper() {
    }

    public WebDriverSeleniumHelper(int webDriverTimeoutSeconds, TestLog testLog) {
        setLogger(testLog);
        setAppiumTheMainUsage(false);
        setWebDriverWaitTimeoutSeconds(webDriverTimeoutSeconds);
        warnStfUsage();
    }


    /***
     * Helper to scroll the page in order to reach an specific element.
     * @param webElement: Webelement.
     * @throws InterruptedException
     */
    public void scrollToWebElement(WebElement webElement) throws InterruptedException {
        executeJavascript(webElement, "arguments[0].scrollIntoView(true);", false, 1);
    }

    /***
     * Switch from default web context into a given webFrame.
     * @param webFrameId: The id of web frame.
     */
    public void switchToWebFrame(String webFrameId) {
        this.getSeleniumDriver().switchTo().frame(webFrameId);
    }

    /***
     * Switch from current web frame back to Default web context.
     */
    public void switchToDefaultWebContext() {
        this.getSeleniumDriver().switchTo().defaultContent();
    }

    /**
     * Helper that perform a key press by using Selenium Action Send keys.
     *
     * @param key: Key to be pressed.
     * @throws InterruptedException
     */
    public void pressKey(Keys key) throws InterruptedException {
        pressKeys(new Keys[]{key});
    }

    /**
     * Helper that perform key presses by using Selenium Action Send keys.
     *
     * @param keys: Keys to be pressed.
     * @throws InterruptedException
     */
    public void pressKeys(Keys[] keys) throws InterruptedException {
        Actions actions = new Actions(this.getSeleniumDriver());
        for (Keys key : keys) {
            actions.sendKeys(key).perform();
            new WaitHelper().waitMilliseconds(this.DEFAULT_FOR_DELAY_BETWEEN_ACTIONS_MS);
        }
    }
}