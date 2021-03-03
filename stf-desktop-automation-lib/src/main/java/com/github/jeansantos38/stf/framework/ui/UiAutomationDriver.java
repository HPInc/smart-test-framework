package com.github.jeansantos38.stf.framework.ui;

import com.github.jeansantos38.stf.data.classes.ui.Details;
import com.github.jeansantos38.stf.data.classes.ui.Element;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.sikuli.script.FindFailed;

import java.io.File;
import java.io.IOException;

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
        return retrievePatternFromNavigatorString(navFilePath, areaId, selector);
    }

    public UiElement buildPattern(String imagePath) {
        return factory(this.screen, new Details(imagePath, 0, 0, SURE_MATCH_MIN_SCORE));
    }

    public UiElement buildPattern(String imagePath, Double similarity) {
        return factory(this.screen, new Details(imagePath, 0, 0, similarity));
    }

    public UiElement buildPattern(String imagePath, int xCoordinate, int yCoordinate) {
        return factory(this.screen, new Details(imagePath, xCoordinate, yCoordinate, SURE_MATCH_MIN_SCORE));
    }

    public UiElement buildPattern(String imagePath, int xCoordinate, int yCoordinate, Double similarity) {
        return factory(this.screen, new Details(imagePath, xCoordinate, yCoordinate, similarity));
    }

    public String takeScreenshot() throws IOException {
        return UiAutomationUtils.saveDesktopScreenshot(this.screen, this.folderPathToSaveScreenshots);
    }

    public String takeScreenshot(String filename) throws IOException {
        return UiAutomationUtils.saveDesktopScreenshot(this.screen, this.folderPathToSaveScreenshots, filename);
    }

    public void type(String... content) throws InterruptedException, FindFailed {
        StringBuilder finalContent = new StringBuilder();
        for (String a : content) finalContent.append(a);
        UiAutomationUtils.type(this.screen, finalContent.toString());
    }

    public void paste(String content) {
        UiAutomationUtils.paste(this.screen, content);
    }

    private UiElement retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String selector) throws Exception {
        Element element = UiAutomationUtils.retrievePatternFromNavigatorString(navigatorFileFullPath, areaId, selector);
      File navigator = new File(navigatorFileFullPath);
//        String masterAbsolutePath =
        element.details.imagePath = navigator.getAbsolutePath().replace(navigator.getName(), element.details.imagePath);
        return factory(this.screen,
                element.details);
    }

    private UiElement factory(Object screen,
                              Details details) {
        return new UiElement(screen,
                details,
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