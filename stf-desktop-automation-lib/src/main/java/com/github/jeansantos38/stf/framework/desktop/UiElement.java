package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.data.classes.ui.Details;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.sikuli.script.*;
import org.sikuli.vnc.VNCScreen;
import org.testng.Assert;

import java.io.IOException;

public class UiElement extends UiAutomationHelper {
    private Details details;
    private Screen screen;
    private VNCScreen vncScreen;
    private boolean isVncScreen;
    private String folderPathToSaveScreenshots;
    private boolean takeScreenshotWhenFail;
    private int msDelayBetweenClicks;
    private int msDelayBetweenActions;
    private UiVisualFeedback uiVisualFeedback;
    private WaitHelper waitHelper;
    private TestLog testLog;
    private Match match;
    private final String NOT_VISIBLE_ASSERT_MSG = "The Ui element was not found! See logs for more details!";
    private final String STILL_VISIBLE_ASSERT_MSG = "The Ui element is still visible! See logs for more details!";

    public Details getDetails() {
        return details;
    }

    public UiVisualFeedback getUiVisualFeedback() {
        return uiVisualFeedback;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
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
                     Details details,
                     int msDelayBetweenClicks,
                     int msDelayBetweenActions,
                     boolean takeScreenshotWhenFail,
                     String folderPathToSaveScreenshots,
                     UiVisualFeedback uiVisualFeedback,
                     WaitHelper waitHelper,
                     TestLog testLog) {

        checkScreenType(screen);
        this.details = details;
        this.msDelayBetweenClicks = msDelayBetweenClicks;
        this.msDelayBetweenActions = msDelayBetweenActions;
        this.takeScreenshotWhenFail = takeScreenshotWhenFail;
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;
        this.waitHelper = waitHelper;
        this.uiVisualFeedback = uiVisualFeedback;
        this.testLog = testLog;
    }


    public void moveCursorOver() throws Exception {
        moveCursorOver(this);
    }

    public void click() throws Exception {
        click(this);
    }

    public void doubleClick() throws Exception {
        doubleClick(this);
    }

    public void tripleClick() throws Exception {
        moveCursorOver();
        actionClick(this, true, 3);
    }


    public void paste(String content) throws Exception {
        click();
        paste(this, content);
    }

    public void dragAndDrop(UiElement destination) throws Exception {
        initializeMatch();
        if (destination.getMatch() == null) {
            destination.initializeMatch();
        }
        dragAndDrop(this, destination);
    }

    public void type(String content) throws Exception {
        click();
        UiAutomationUtils.type(!this.isVncScreen() ? this.getScreen() : this.getVncScreen(), content);
    }

    public void clearText() throws Exception {
        click();
        UiAutomationUtils.performKeyCombination(!this.isVncScreen() ? this.getScreen() : this.getVncScreen(), "a", KeyModifier.CTRL);
        UiAutomationUtils.type(!this.isVncScreen() ? this.getScreen() : this.getVncScreen(), Key.DELETE);
    }

    public String extractTextViaOCR() throws Exception {
        return extractTextFromRegionViaOCR(this);
    }


    public String extractTextViaClipboard() throws Exception {
        click();
        UiAutomationUtils.performKeyCombination(!this.isVncScreen() ? this.getScreen() : this.getVncScreen(), "a", KeyModifier.CTRL);
        UiAutomationUtils.performKeyCombination(!this.isVncScreen() ? this.getScreen() : this.getVncScreen(), "c", KeyModifier.CTRL);
        return UiAutomationUtils.getContentFromClipboard();
    }

    public void assertContainsTextViaOCR(String text) throws Exception {
        Assert.assertTrue(extractTextFromRegionViaOCR(this).contains(text));
    }

    public void assertContainsTextViaClipboard(String text) throws Exception {
        Assert.assertTrue(extractTextViaClipboard().contains(text));
    }

    public void assertVisible(int waitMs) throws IOException, FindFailed {
        Assert.assertTrue(exists(this, waitMs), NOT_VISIBLE_ASSERT_MSG);
    }

    public void assertVisible() throws Exception {
        Assert.assertTrue(exists(this), NOT_VISIBLE_ASSERT_MSG);
    }

    public void multipleLeftClicks(int howMany) throws InterruptedException, FindFailed {
        actionClick(this, true, howMany);
    }

    public void multipleRightClicks(int howMany) throws InterruptedException, FindFailed {
        actionClick(this, false, howMany);
    }

    public void assertNotVisible(int waitMs) throws Exception {
        Assert.assertFalse(exists(this, waitMs, true), STILL_VISIBLE_ASSERT_MSG);
    }

    public void initializeMatch() throws Exception {
        find(this);
    }

    public boolean regionHasPattern(UiElement pattern) throws Exception {
        return referenceAreaHasPattern(this, pattern);
    }

    public void assertRegionHasPattern(UiElement pattern) throws Exception {
        Assert.assertTrue(regionHasPattern(pattern));
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
