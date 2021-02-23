package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.dataclasses.web.datadriven.DataDrivenNavigator;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.Elements;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.MasterImageDetails;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;

import java.io.File;
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
    private final double SURE_MATCH_MIN_SCORE = 0.95;
    private String folderPathToSaveScreenshots;
    private boolean takeScreenshotIfNotFound;

    public UiAutomationDriver() {
    }

    public UiAutomationDriver(Object screen, TestLog testLog, String folderPathToSaveScreenshots, boolean takeScreenshotIfNotFound) {
        this.screen = screen;
        this.testLog = testLog;
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;
        this.takeScreenshotIfNotFound = takeScreenshotIfNotFound;
    }

    public UiElement buildPatternFromNavigator(String navFilePath, String areaId, String selector) throws Exception {
        return retrievePatternFromNavigatorString(navFilePath, areaId, selector, false);
    }

    public UiElement buildPattern(String imagePath) {
        return factory(this.screen, imagePath, 0, 0, SURE_MATCH_MIN_SCORE, this.takeScreenshotIfNotFound, this.folderPathToSaveScreenshots);
    }

    public UiElement buildPattern(String imagePath, Double similarity) {
        return factory(this.screen, imagePath, 0, 0, similarity, this.takeScreenshotIfNotFound, this.folderPathToSaveScreenshots);
    }

    public UiElement buildPattern(String imagePath, int xCoordinate, int yCoordinate, Double similarity) {
        return factory(this.screen, imagePath, xCoordinate, yCoordinate, similarity, this.takeScreenshotIfNotFound, this.folderPathToSaveScreenshots);
    }

    private UiElement retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String selector, boolean retrieveMasterLocOnly) throws Exception {
        String navigatorContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
        DataDrivenNavigator dataDrivenNavigator = DeserializeHelper.deserializeStringToObject(DataDrivenNavigator.class, SerializationType.JSON, navigatorContent);
        MasterImageDetails masterImageDetails = Arrays.stream(dataDrivenNavigator.MasterImageDetails).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
        if (masterImageDetails == null)
            throw new Exception(String.format("The master image details for area '%1$s' was not retrieved from navigator, pls review the given parameters.", areaId));

        Elements elements = Arrays.stream(masterImageDetails.elements).filter(x -> x.element.selector.equals(selector)).findFirst().orElse(null);
        if (elements == null || elements.element == null)
            throw new Exception(String.format("The element for selector '%1$s' was not retrieved from navigator, pls review the given parameters.", selector));

        File navigator = new File(navigatorFileFullPath);
        String masterAbsolutePath = navigator.getAbsolutePath().replace(navigator.getName(), (retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc));
        this.testLog.logIt(String.format("Searching for the image '%1$s' related to the selector '%2$s'.", retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc, selector));
        return factory(this.screen, masterAbsolutePath, elements.element.xCoordinate, elements.element.yCoordinate, (double) elements.element.similarity, this.takeScreenshotIfNotFound, this.folderPathToSaveScreenshots);
    }

    private UiElement factory(Object screen, String imagePath, int xCoordinate, int yCoordinate, Double similarity, boolean takeScreenshotIfNotFound, String folderPathToSaveScreenshots) {
        UiElement uiElement = new UiElement(screen, imagePath, xCoordinate, yCoordinate, similarity);
        uiElement.setFolderPathToSaveScreenshots(folderPathToSaveScreenshots);
        uiElement.setTakeScreenshotIfNotFound(takeScreenshotIfNotFound);
        uiElement.setTestLog(testLog);
        return uiElement;
    }
}