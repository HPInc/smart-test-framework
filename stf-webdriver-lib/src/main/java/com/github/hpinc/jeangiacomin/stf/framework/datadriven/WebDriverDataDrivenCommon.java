package com.github.hpinc.jeangiacomin.stf.framework.datadriven;

import com.github.hpinc.jeangiacomin.stf.enums.webdriver.SelectorType;
import com.github.hpinc.jeangiacomin.stf.framework.regex.RegexHelper;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public abstract class WebDriverDataDrivenCommon {

    /***
     * This helper prevents wrong combinations of operators and the content to be validated.
     * @param operator: The operator will defined what type of assert will be used.
     * @param contentType: The content type will define how the values will be asserted.
     * @throws Exception
     */
    protected void verifyIfOperatorSupportsContent(String operator, String contentType) throws Exception {
        boolean notSupported = false;

        switch (operator) {
            case "EqualTo":
            case "NotEqualTo":
                break;

            case "Contains":
                if (contentType.equals("boolean") || !contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "NotContain":
                if (contentType.equals("boolean") || !contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "GreaterThan":
                if (contentType.equals("boolean") || contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "GreaterThanOrEqualTo":
                if (contentType.equals("boolean") || contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "LesserThan":
                if (contentType.equals("boolean") || contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "LesserThanOrEqualTo":
                if (contentType.equals("boolean") || contentType.equals("string")) {
                    notSupported = true;
                }
                break;

            case "Exists":
                if (contentType.equals("boolean") || contentType.contains("picture_")) {
                    notSupported = true;
                }
                break;

            case "NotExist":
                if (contentType.equals("boolean") || contentType.contains("picture_")) {
                    notSupported = true;
                }
                break;

            default:
                throw new Exception(String.format("The action type:[%1$s] is not supported!!!", operator));

        }
        if (notSupported) {
            throw new Exception(String.format("The operator:[%1$s] cannot be applied to the action:[%2$s].", contentType, operator));
        }
    }

    /***
     * Simple helper that returns the Selector type based on data driven WebDriver action.
     * @param locator
     * @return
     * @throws Exception
     */
    protected SelectorType returnSelectorType(String locator) throws Exception {
        String filteredAction = RegexHelper.returnFirstMatchFrom(locator, "(by[A-Z])\\w+");
        switch (filteredAction) {
            case "byId":
                return SelectorType.BY_ID;
            case "byXpath":
                return SelectorType.BY_XPATH;
            case "byClass":
                return SelectorType.BY_CLASS_NAME;
            case "byCss":
                return SelectorType.BY_CSS;
            case "byName":
                return SelectorType.BY_NAME;
            case "byTagName":
                return SelectorType.BY_TAG_NAME;
            case "byLink":
                return SelectorType.BY_LINK;
            case "byPartialLink":
                return SelectorType.BY_PARTIAL_LINK;
            default:
                throw new Exception(String.format("The action type:[%1$s] is not supported!!!"));
        }
    }
}