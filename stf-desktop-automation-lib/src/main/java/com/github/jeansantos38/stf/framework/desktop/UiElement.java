package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;
import org.testng.Assert;

import java.io.IOException;

public class UiElement extends UiAutomationHelper {
    private String imagePath;
    private int xCoordinate;
    private int yCoordinate;
    private int recTopLeftX;
    private int recTopLeftY;
    private int recBottomRightX;
    private int recBottomRightY;
    private Double similarity;
    private Screen screen;
    private VNCScreen vncScreen;
    private boolean isVncScreen;
    private String folderPathToSaveScreenshots;
    private boolean takeScreenshotWhenFail;
    private int msDelayBetweenClicks;
    private int msDelayBetweenActions;
    private final String NOT_VISIBLE_ASSERT_MSG = "The Ui element was not found! See logs for more details!";
    private final String STILL_VISIBLE_ASSERT_MSG = "The Ui element is still visible! See logs for more details!";
    private WaitHelper waitHelper;
    private TestLog testLog;

    public String getImagePath() {
        return imagePath;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public int getRecTopLeftX() {
        return recTopLeftX;
    }

    public int getRecTopLeftY() {
        return recTopLeftY;
    }

    public int getRecBottomRightX() {
        return recBottomRightX;
    }

    public int getRecBottomRightY() {
        return recBottomRightY;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public String getFolderPathToSaveScreenshots() {
        return folderPathToSaveScreenshots;
    }

    public boolean takeScreenshotWhenFail() {
        return takeScreenshotWhenFail;
    }

    public int getMsDelayBetweenClicks() {
        return msDelayBetweenClicks;
    }

    public int getMsDelayBetweenActions() {
        return msDelayBetweenActions;
    }

    public WaitHelper getWaitHelper() {
        return waitHelper;
    }

    public TestLog getTestLog() {
        return testLog;
    }

    public String get_NOT_VISIBLE_ASSERT_MSG() {
        return NOT_VISIBLE_ASSERT_MSG;
    }

    public String get_STILL_VISIBLE_ASSERT_MSG() {
        return STILL_VISIBLE_ASSERT_MSG;
    }

    public Screen getScreen() {
        return screen;
    }

    public VNCScreen getVncScreen() {
        return vncScreen;
    }

    public boolean isVncScreen() {
        return isVncScreen;
    }

    public UiElement() {
    }

    public UiElement(Object screen,
                     String imagePath,
                     int xCoordinate,
                     int yCoordinate,
                     Double similarity,
                     int msDelayBetweenClicks,
                     int msDelayBetweenActions,
                     boolean takeScreenshotWhenFail,
                     String folderPathToSaveScreenshots,
                     WaitHelper waitHelper,
                     TestLog testLog) {
        this(screen, imagePath, xCoordinate, yCoordinate, -0, -0, -0, -0, similarity, msDelayBetweenClicks, msDelayBetweenActions, takeScreenshotWhenFail, folderPathToSaveScreenshots, waitHelper, testLog);
    }

    public UiElement(Object screen,
                     String imagePath,
                     int xCoordinate,
                     int yCoordinate,
                     int recTopLeftX,
                     int recTopLeftY,
                     int recBottomRightX,
                     int recBottomRightY,
                     Double similarity,
                     int msDelayBetweenClicks,
                     int msDelayBetweenActions,
                     boolean takeScreenshotWhenFail,
                     String folderPathToSaveScreenshots,
                     WaitHelper waitHelper,
                     TestLog testLog) {

        checkScreenType(screen);
        this.imagePath = imagePath;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.recTopLeftX = recTopLeftX;
        this.recTopLeftY = recTopLeftY;
        this.recBottomRightX = recBottomRightX;
        this.recBottomRightY = recBottomRightY;
        this.similarity = similarity;
        this.msDelayBetweenClicks = msDelayBetweenClicks;
        this.msDelayBetweenActions = msDelayBetweenActions;
        this.takeScreenshotWhenFail = takeScreenshotWhenFail;
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;
        this.waitHelper = waitHelper;
        this.testLog = testLog;
    }

    public void moveCursorOver() throws FindFailed {
        moveCursorOver(this);
    }

    public void click() throws FindFailed, InterruptedException {
        click(this);
    }

    public void doubleClick() throws FindFailed, InterruptedException {
        doubleClick(this);
    }

    public void paste(String content) {
        paste(this, content);
    }

    public void type(String keys) throws Exception {
        throw new Exception("Not implemented!");
    }

    public String getTextViaOCR() throws Exception {
       return extractTextViaOCR(this);
    }

    public boolean containsString(String content) {
        return true;
    }

    public void assertVisible(int waitMs) throws IOException, FindFailed {
        Assert.assertTrue(exists(this, waitMs), NOT_VISIBLE_ASSERT_MSG);
    }

    public void assertVisible() throws Exception {
        Assert.assertTrue(exists(this), NOT_VISIBLE_ASSERT_MSG);
    }


    public void assertNotVisible(int waitMs) throws Exception {
        Assert.assertFalse(exists(this, waitMs, true), STILL_VISIBLE_ASSERT_MSG);
    }

    public void assertNotVisible() throws Exception {
        Assert.assertFalse(exists(this, 0, true), STILL_VISIBLE_ASSERT_MSG);
    }

    private void checkScreenType(Object screen) {
        if (UiAutomationUtils.isVncScreen(screen)) {
            this.isVncScreen = true;
            this.vncScreen = (VNCScreen) screen;
        } else {
            this.isVncScreen = false;
            this.screen = (Screen) screen;
        }
    }
}
