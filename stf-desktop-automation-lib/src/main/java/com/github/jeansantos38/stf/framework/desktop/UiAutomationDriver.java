package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.dataclasses.web.datadriven.DataDrivenNavigator;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.Elements;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.MasterImageDetails;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class UiAutomationDriver {

    private Object screen;
    private TestLog testLog;
    private final double SURE_MATCH_MIN_SCORE = 0.92;
    private final int MS_DELAY_BETWEEN_ACTIONS = 500;
    private final int MS_DELAY_BETWEEN_CLICKS = 200;
    private String folderPathToSaveScreenshots;
    private boolean takeScreenshotWhenFail;
    private WaitHelper waitHelper;
    private int msDelayBetweenClicks;
    private int msDelayBetweenActions;
    private UiVisualFeedback uiVisualFeedback;


    public UiAutomationDriver(
            Object screen,
            TestLog testLog,
            WaitHelper waitHelper,
            String folderPathToSaveScreenshots,
            boolean takeScreenshotWhenFail) throws Exception {
        this(screen, testLog, waitHelper, folderPathToSaveScreenshots, takeScreenshotWhenFail, 100, 100);
    }

    public UiAutomationDriver(
            Object screen,
            TestLog testLog,
            WaitHelper waitHelper,
            String folderPathToSaveScreenshots,
            boolean takeScreenshotWhenFail,
            UiVisualFeedback uiVisualFeedback) throws Exception {
        this(screen, testLog, waitHelper, folderPathToSaveScreenshots, takeScreenshotWhenFail, 100, 100, uiVisualFeedback);
    }

    public UiAutomationDriver(
            Object screen,
            TestLog testLog,
            WaitHelper waitHelper,
            String folderPathToSaveScreenshots,
            boolean takeScreenshotWhenFail,
            int msDelayBetweenClicks,
            int msDelayBetweenActions) throws Exception {
        this(screen, testLog, waitHelper, folderPathToSaveScreenshots, takeScreenshotWhenFail, msDelayBetweenClicks, msDelayBetweenActions, null);
    }

    public UiAutomationDriver(
            Object screen,
            TestLog testLog,
            WaitHelper waitHelper,
            String folderPathToSaveScreenshots,
            boolean takeScreenshotWhenFail,
            int msDelayBetweenClicks,
            int msDelayBetweenActions,
            UiVisualFeedback uiVisualFeedback) throws Exception {

        this.screen = screen;
        this.testLog = testLog;
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;
        this.takeScreenshotWhenFail = takeScreenshotWhenFail;
        this.waitHelper = waitHelper;
        this.msDelayBetweenActions = msDelayBetweenActions == 0 ? MS_DELAY_BETWEEN_ACTIONS : msDelayBetweenActions;
        this.msDelayBetweenClicks = msDelayBetweenClicks == 0 ? MS_DELAY_BETWEEN_CLICKS : msDelayBetweenClicks;
        this.uiVisualFeedback = uiVisualFeedback;
        checkScreen(screen);
    }

    public UiElement buildPatternFromNavigator(String navFilePath, String areaId, String selector) throws Exception {
        return retrievePatternFromNavigatorString(navFilePath, areaId, selector, true);
    }

    public UiElement buildPattern(String imagePath) {
        return factory(this.screen, imagePath, 0, 0, SURE_MATCH_MIN_SCORE);
    }

    public UiElement buildPattern(String imagePath, Double similarity) {
        return factory(this.screen, imagePath, 0, 0, similarity);
    }

    public UiElement buildPattern(String imagePath, int xCoordinate, int yCoordinate) {
        return factory(this.screen, imagePath, xCoordinate, yCoordinate, SURE_MATCH_MIN_SCORE);
    }

    public UiElement buildPattern(String imagePath, int xCoordinate, int yCoordinate, Double similarity) {
        return factory(this.screen, imagePath, xCoordinate, yCoordinate, similarity);
    }

    public String takeScreenshot() throws IOException {
        return UiAutomationUtils.saveDesktopScreenshot(this.screen, this.folderPathToSaveScreenshots);
    }

    public String takeScreenshot(String filename) throws IOException {
        return UiAutomationUtils.saveDesktopScreenshot(this.screen, this.folderPathToSaveScreenshots, filename);
    }

    public void type(String content) {
        UiAutomationUtils.type(this.screen, content);
    }

    public void paste(String content) {
        UiAutomationUtils.paste(this.screen, content);
    }

    private UiElement retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String
            selector, boolean retrieveMasterLocOnly) throws Exception {
       Elements elements = UiAutomationUtils.retrievePatternFromNavigatorString(navigatorFileFullPath,areaId,selector,retrieveMasterLocOnly);
        File navigator = new File(navigatorFileFullPath);
        String masterAbsolutePath = navigator.getAbsolutePath().replace(navigator.getName(), (retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc));
        return factory(this.screen,
                masterAbsolutePath,
                elements.element.xCoordinate,
                elements.element.yCoordinate,
                elements.element.rectangleTopLeftX,
                elements.element.rectangleTopLeftY,
                elements.element.rectangleBottomRightX,
                elements.element.rectangleBottomRightY,
                (double) elements.element.similarity);
    }




//private UiElement retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String
//            selector, boolean retrieveMasterLocOnly) throws Exception {
//        String navigatorContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
//        DataDrivenNavigator dataDrivenNavigator = DeserializeHelper.deserializeStringToObject(DataDrivenNavigator.class, SerializationType.JSON, navigatorContent);
//        MasterImageDetails masterImageDetails = Arrays.stream(dataDrivenNavigator.MasterImageDetails).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
//        if (masterImageDetails == null)
//            throw new Exception(String.format("The master image details for area '%1$s' was not retrieved from navigator, pls review the given parameters.", areaId));
//
//        Elements elements = Arrays.stream(masterImageDetails.elements).filter(x -> x.element.selector.equals(selector)).findFirst().orElse(null);
//        if (elements == null || elements.element == null)
//            throw new Exception(String.format("The element for selector '%1$s' was not retrieved from navigator, pls review the given parameters.", selector));
//
//        File navigator = new File(navigatorFileFullPath);
//        String masterAbsolutePath = navigator.getAbsolutePath().replace(navigator.getName(), (retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc));
//        this.testLog.logIt(String.format("Searching for the image '%1$s' related to the selector '%2$s'.", retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc, selector));
//        return factory(this.screen,
//                masterAbsolutePath,
//                elements.element.xCoordinate,
//                elements.element.yCoordinate,
//                elements.element.rectangleTopLeftX,
//                elements.element.rectangleTopLeftY,
//                elements.element.rectangleBottomRightX,
//                elements.element.rectangleBottomRightY,
//                (double) elements.element.similarity);
//    }


    private UiElement factory(Object screen,
                              String imagePath,
                              int xCoordinate,
                              int yCoordinate,
                              Double similarity) {
        return new UiElement(screen,
                imagePath,
                xCoordinate,
                yCoordinate,
                similarity,
                this.msDelayBetweenClicks,
                this.msDelayBetweenActions,
                this.takeScreenshotWhenFail,
                this.folderPathToSaveScreenshots,
                this.uiVisualFeedback,
                this.waitHelper,
                this.testLog);
    }


    private UiElement factory(Object screen,
                              String imagePath,
                              int xCoordinate,
                              int yCoordinate,
                              int recTopLeftX,
                              int recTopLeftY,
                              int recBottomRightX,
                              int recBottomRightY,
                              Double similarity) {
        return new UiElement(screen,
                imagePath,
                xCoordinate,
                yCoordinate,
                recTopLeftX,
                recTopLeftY,
                recBottomRightX,
                recBottomRightY,
                similarity,
                this.msDelayBetweenClicks,
                this.msDelayBetweenActions,
                this.takeScreenshotWhenFail,
                this.folderPathToSaveScreenshots,
                this.uiVisualFeedback,
                this.waitHelper,
                this.testLog);
    }

    private void checkScreen(Object screen) throws Exception {
        if (screen == null)
            throw new Exception("Screen cannot be null!");
    }
}