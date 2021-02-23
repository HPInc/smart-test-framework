package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.framework.logger.TestLog;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;
import org.testng.Assert;

import java.io.IOException;

public class UiElement extends UiAutomationHelper {
    String imagePath;
    int xCoordinate;
    int yCoordinate;
    Double similarity;
    Screen screen;
    VNCScreen vncScreen;
    boolean isVncScreen;
    boolean takeScreenshotIfNotFound;
    TestLog testLog;
    public final String NOT_VISIBLE_ASSERT_MSG = "The Ui element was not found! See logs for more details!";


    public void setTestLog(TestLog testLog) {
        this.testLog = testLog;
    }

    public String getFolderPathToSaveScreenshots() {
        return folderPathToSaveScreenshots;
    }

    public void setFolderPathToSaveScreenshots(String folderPathToSaveScreenshots) {
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;
    }

    String folderPathToSaveScreenshots;

    public boolean isTakeScreenshotIfNotFound() {
        return takeScreenshotIfNotFound;
    }

    public void setTakeScreenshotIfNotFound(boolean takeScreenshotIfNotFound) {
        this.takeScreenshotIfNotFound = takeScreenshotIfNotFound;
    }

    public UiElement() {
    }

    public UiElement(String imagePath) {
        this(imagePath, null);
    }

    public UiElement(String imagePath, Double similarity) {
        this(imagePath, 0, 0, similarity);
    }

    public UiElement(String imagePath, int xCoordinate, int yCoordinate, Double similarity) {
        this(null, imagePath, xCoordinate, yCoordinate, similarity);
    }

    public UiElement(Object screen, String imagePath, int xCoordinate, int yCoordinate, Double similarity) {
        if (similarity != null) {
            this.similarity = similarity;
        }
        this.imagePath = imagePath;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        checkScreenType(screen);
    }

    public void click() throws FindFailed {
        click(this);
    }

    public void assertIsVisible(int waitMs) throws IOException, FindFailed {
        Assert.assertTrue(exists(this, waitMs), NOT_VISIBLE_ASSERT_MSG);
    }

    public void assertIsVisible() throws Exception {
        Assert.assertTrue(exists(this), NOT_VISIBLE_ASSERT_MSG);
    }


    private void checkScreenType(Object screen) {
        if (screen.getClass().equals(Screen.class)) {
            this.isVncScreen = false;
            this.screen = (Screen) screen;
        } else {
            this.isVncScreen = true;
            this.vncScreen = (VNCScreen) screen;
        }
    }
}
