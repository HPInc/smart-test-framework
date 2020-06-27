package com.github.hpinc.jeangiacomin.stf.framework.webdriver;

import com.github.hpinc.jeangiacomin.stf.enums.webdriver.SelectorType;
import com.github.hpinc.jeangiacomin.stf.framework.logger.TestLog;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class WebDriverAppiumHelper extends WebDriverCommonHelper {

    /***
     * Default constructor.
     */
    public WebDriverAppiumHelper() {
    }

    public WebDriverAppiumHelper(Platform platform, int webDriverTimeout, TestLog testLog) {
        setLogger(testLog);
        setAppiumTheMainUsage(true);
        setWebDriverWaitTimeoutSeconds(webDriverTimeout);
        setDevicePlatform(platform);


        warnStfUsage();
    }
    // region Common Area

    /***
     * Helper to send values to an web element.
     * @param platform What platform?
     * @param selectorType: What is the type of this selector? What selector?
     * @param selectorId: The element selector.
     * @param value: The value to be be sent.
     * @throws Exception
     */
    public void sendKeys(Platform platform, SelectorType selectorType, String selectorId, String value) throws Exception {
        WebElement element = first(selectorType, selectorId);
        switch (platform) {
            case ANDROID:
                element.sendKeys(value);
                break;
            case IOS:
                IOSElement tempElement = (IOSElement) element;
                tempElement.setValue(value);
                break;
            default:
                throw new Exception(String.format("The webdriver platform %1$s is not supported yet.", platform.toString()));
        }
    }

    /**
     * This helper performs a click in a element.
     * The difference between this helper and a _common First.Click() is that this one is intended to Mobile elements instead regular WebElements - otherwise I can guarantee that you'll face instabilities while testing Apps.
     *
     * @param selectorType: What is the type of this selector? What selector?
     * @param selectorId:   The element selector.
     * @throws Exception
     */
    public void tap(SelectorType selectorType, String selectorId) throws Exception {
        MobileElement element;
        element = (MobileElement) first(selectorType, selectorId);
        element.click();
    }

    //endregion

    // region iOS Area

    /***
     *  Helper to send values to an web element for iOS.
     * @param selectorType: What is the type of this selector? What selector?
     * @param selectorId: The element selector.
     * @param value: The value to be be sent.
     * @throws Exception
     */
    public void sendKeysToiOS(SelectorType selectorType, String selectorId, String value) throws Exception {
        sendKeys(Platform.IOS, selectorType, selectorId, value);
    }
    // endregion

    // region Android Area

    /***
     * Helper to send values to an web element for Android.
     * @param selectorType: What is the type of this selector? What selector?
     * @param selectorId: The element selector.
     * @param value: The value to be be sent.
     * @throws Exception
     */
    public void sendKeysToAndroid(SelectorType selectorType, String selectorId, String value) throws Exception {
        sendKeys(Platform.ANDROID, selectorType, selectorId, value);
    }
    // endregion
}