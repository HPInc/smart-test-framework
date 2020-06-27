package com.github.hpinc.jeangiacomin.stf.framework.datadriven;

import com.github.hpinc.jeangiacomin.stf.framework.desktop.DesktopAutomationHelper;
import com.github.hpinc.jeangiacomin.stf.dataclasses.datadriven.Statement;
import com.github.hpinc.jeangiacomin.stf.dataclasses.web.datadriven.Element;
import com.github.hpinc.jeangiacomin.stf.framework.logger.TestLog;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.testng.Assert;

import java.io.IOException;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class AssertDesktopAutomationInstructionSet {

    private TestLog logger;
    private DesktopAutomationHelper desktopAutomationHelper;

    /***
     * Default constructor.
     * @param logger: LoggerQA class instance.
     * @param desktopAutomationHelper: Image Recognition Helper instance.
     */
    public AssertDesktopAutomationInstructionSet(TestLog logger, DesktopAutomationHelper desktopAutomationHelper) {
        this.logger = logger;
        this.desktopAutomationHelper = desktopAutomationHelper;
    }

    /***
     * This helper does all the magic required for asserting an image recognition data driven.
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param statement: Data driven statement object.
     * @throws Exception
     */
    public void assertImageDataDriven(String navigatorFileFullPath, Statement statement) throws Exception {
        Element element = this.desktopAutomationHelper.retrieveTargetElement(navigatorFileFullPath, statement.areaId, statement.selector).element;

        Pattern pattern = desktopAutomationHelper.retrievePatternFromNavigator(navigatorFileFullPath, statement.areaId, statement.selector);
        Region region;

        switch (statement.action) {
            case "assertField":
                switch (statement.method) {
                    case "byOCR":
                        region = this.desktopAutomationHelper.createRegionFromReferencePattern(pattern, element.rectangleTopLeftX, element.rectangleTopLeftY, element.rectangleBottomRightX, element.rectangleBottomRightY);
                        String currentValue = region.text();
                        //TODO As I've done in WebDriver assert, create a helper that checks if the operator does match with the content type before doing any cast and,or calling the correct validator.
                        validateString(statement.operator, currentValue, (String) statement.content[0], region);
                        break;

                    case "byImageFind":
                        validateImageExists(pattern, statement.operator);
                        break;

                    case "byImageFindInRegion":
                        Pattern refPattern = this.desktopAutomationHelper.retrievePatternFromNavigator(navigatorFileFullPath, statement.areaId, statement.selector, false);
                        region = this.desktopAutomationHelper.createRegionFromReferencePattern(refPattern, statement.rectangleTopLeftX, statement.rectangleTopLeftY, statement.rectangleBottomRightX, statement.rectangleBottomRightY);
                        boolean masterExists = this.desktopAutomationHelper.patternExistsInRegion(region, pattern, this.desktopAutomationHelper.getTakeRegionScreenshotIfPatternNotFound(), 2);

                        if (element.ctrlType.equals("checkBox") || element.ctrlType.equals("radioButton")) {
                            //This case besides checking the a patter does exist inside a region, it will also check if it's checked or not - accordingly the given statement content.
                            boolean expectedStateValue = (Boolean) statement.content[0];
                            if (expectedStateValue) {
                                Assert.assertTrue(masterExists, String.format("The element [%1$s] with selector id [%2$s] inside region [X:%3$s,Y:%4$s,W:%5$s,H:%6$s] ***IS NOT*** enabled\\checked!!!", element.ctrlType, element.selector, String.valueOf(statement.rectangleTopLeftX), String.valueOf(statement.rectangleTopLeftY), String.valueOf(statement.rectangleBottomRightX), String.valueOf(statement.rectangleBottomRightY)));
                            } else {
                                Assert.assertFalse(masterExists, String.format("The element [%1$s] with selector id [%2$s] inside region [X:%3$s,Y:%4$s,W:%5$s,H:%6$s] ***IS STILL*** enabled\\checked!!!", element.ctrlType, element.selector, String.valueOf(statement.rectangleTopLeftX), String.valueOf(statement.rectangleTopLeftY), String.valueOf(statement.rectangleBottomRightX), String.valueOf(statement.rectangleBottomRightY)));
                            }
                        } else { //Considering you're just looking for a pattern. This should be used when there are repeated images that could lead the test into a false-positive.
                            //Check if the master exists inside the given region.
                            Assert.assertTrue(masterExists, String.format("The pattern [%1$s] was not found inside the given region (check data driven for region coordinates).", pattern.getFilename()));
                        }
                        break;

                    default:
                        throw new Exception(String.format("The %1$s method is not supported yet", statement.method));
                }
                break;

            default:
                throw new Exception(String.format("The %1$s action type is not supported yet", statement.action));
        }

        //TODO This is just to keep in mind my original idea
        /*
        Some data driven statements won't require a content to be validated depending on its action, since its details(ctrlType) should be already described on its respective navigator.

        So this is the deal,
        -> Assert_byOCR does require content that could be string or int.
            So its support operators will be !equals,equals,contains,!contains, >,<,<=,>=

        ->Assert_byImageMatch, does not require content. Read it from its navigator.
            So its supported operators will be !exits,exists.

        **Before I forget why having  these two below (and I know I'll) instead of just the byImageMatch - this is why: The screen might have other possible matches from other unpredicted content leading the test to false-positive. So the idea here is to restrict the search area based on knowing the app (under test) behavior.

        ->Assert_byRegion, does require content that will be a region or a set of it (e.g.:["PrimaryRegion.Nearby"] or ["PrimaryRegion.Nearby","PrimaryRegion.Below","PrimaryRegion.Right"] or ["PrimaryRegion.Nearby.UP","PrimaryRegion.Above.Right","PrimaryRegion.Below.Nearby"].
            So its supported operators will be !exits,exists.

        ->Assert_byExactRegion, does require content that should be XYHW coordinates. (I believe this one will be quite difficult to use, but doesn't matter).
            So its supported operators will be !exits,exists,equals,!equals.
         */
    }

    /***
     * Helper that validates the extracted content (OCR) from a region and in case of failure take a screenshot from such area.
     * @param operator: The operator will defined what type of assert will be used.
     * @param current: The current value.
     * @param expected: The expected value accordingly data-driven.
     * @param region: A screen region.
     * @throws Exception
     */
    private void validateString(String operator, String current, String expected, Region region) throws Exception {
        boolean preValidationResultOCR = preValidateOcr(operator, current, expected);
        String customMessage = "The assert by OCR has failed. But remember that OCR feature isn't fully reliable%1$s";
        if (!preValidationResultOCR) {
            if (!this.desktopAutomationHelper.getTakeScreenshotIfOCRNotFound()) {
                customMessage = String.format(customMessage, ".");
            } else {
                customMessage = String.format(customMessage, ". The image to that corresponds to the given region was saved at: " + this.desktopAutomationHelper.saveScreenshotFromRegion(region));
            }
        }
        switch (operator) {
            case "EqualTo":
                Assert.assertEquals(current, expected, customMessage);
                break;
            case "Contains":
                Assert.assertTrue(current.contains(expected), customMessage + String.format(" The current value '%1%s' does not contains the expected value '%2$s'", current, expected));
                break;
            case "NotContains":
                Assert.assertFalse(current.contains(expected), customMessage + String.format(" The current value '%1%s' contains the expected value '%2$s'", current, expected));
                break;
            default:
                throw new Exception(String.format("The operator %1$s is not supported yet!!!", operator));
        }
    }

    /***
     * Helper that validates if an image does exist and save current desktop in case of failure.
     * @param pattern: An object from a master image file.
     * @throws IOException
     */
    private void validateImageExists(Pattern pattern, String operator) throws Exception {
        String customFailMessage = "";
        String customSuccessMessage = "";

        boolean imageExists = this.desktopAutomationHelper.exists(pattern, false, 0);

        switch (operator) {
            case "Exists":
                if (!imageExists) {
                    customFailMessage = String.format("The assert by Image find has failed!. ###The expected master is:[%1$s] ### It was not found at: [%2$s]", pattern.getFilename(), this.desktopAutomationHelper.saveDesktopScreenshot());
                    Assert.assertTrue(imageExists, customFailMessage);
                } else {
                    customSuccessMessage = "The pattern does exist as expected!";
                }
                break;
            case "NotExist":
                if (imageExists) {
                    customFailMessage = String.format("The assert by Image find has failed!. ###The expected master is:[%1$s] ### It was found!at: [%2$s]", pattern.getFilename(), this.desktopAutomationHelper.saveDesktopScreenshot());
                    Assert.assertFalse(imageExists, customFailMessage);
                } else {
                    customSuccessMessage = "The pattern does NOT exist as expected!";
                }
                break;
            default:
                throw new Exception(String.format("The operator %s is not supported for checking images", operator));
        }
        logger.logIt(customSuccessMessage);
    }

    /***
     * This seems to be the more stupid thing I've ever created, but there's a good reason for that. This method does what it says and the reason for that is that considering all versions from junit and testNG (at this time commit) for some unbelievable reason, are executing a method that returns a string - that's been used as custom message inside assert.equals. even when there are no failures. If you don't believe me, just test it. So this is a workaround for preventing that method to be executed.
     * @param operator: The operator will defined what type of assert will be used.
     * @param current: The current value.
     * @param expected: The expected value accordingly data-driven.
     * @return
     * @throws Exception
     */
    private boolean preValidateOcr(String operator, String current, String expected) throws Exception {
        switch (operator) {
            case "EqualTo":
                return expected.equals(current);
            case "Contains":
                return expected.contains(current);
            default:
                throw new Exception(String.format("The operator %1$s is not supported yet!!", operator));
        }
    }
}