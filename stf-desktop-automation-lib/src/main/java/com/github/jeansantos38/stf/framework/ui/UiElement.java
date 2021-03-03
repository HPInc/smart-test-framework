package com.github.jeansantos38.stf.framework.ui;

import com.github.jeansantos38.stf.contants.ui.Logs;
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
        return Logs.NOT_VISIBLE_ASSERT_MSG;
    }

    public String get_STILL_VISIBLE_ASSERT_MSG() {
        return Logs.STILL_VISIBLE_ASSERT_MSG;
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
        String current = extractTextFromRegionViaOCR(this);
        Assert.assertTrue(current.contains(text), Logs.ERROR_TEXT_NOT_FOUND_VIA_OCR + String.format(Logs.ERROR_SUFFIX_EXPECTED_ACTUAL, text, current));
    }

    public void assertContainsTextViaClipboard(String text) throws Exception {
        String current = extractTextViaClipboard();
        Assert.assertTrue(current.contains(text), Logs.ERROR_TEXT_NOT_FOUND_VIA_CLIPBOARD + String.format(Logs.ERROR_SUFFIX_EXPECTED_ACTUAL, text, current));
    }

    public void assertVisible(int waitMs) throws IOException, FindFailed {
        Assert.assertTrue(exists(this, waitMs), Logs.NOT_VISIBLE_ASSERT_MSG);
    }

    public void assertVisible() throws Exception {
        Assert.assertTrue(exists(this), Logs.NOT_VISIBLE_ASSERT_MSG);
    }

    public void multipleLeftClicks(int howMany) throws InterruptedException, FindFailed {
        actionClick(this, true, howMany);
    }

    public void multipleRightClicks(int howMany) throws InterruptedException, FindFailed {
        actionClick(this, false, howMany);
    }

    public void assertNotVisible(int waitMs) throws Exception {
        Assert.assertFalse(exists(this, waitMs, true), Logs.STILL_VISIBLE_ASSERT_MSG);
    }

    public void initializeMatch() throws Exception {
        find(this);
    }

    public boolean regionContainsPattern(UiElement pattern) throws Exception {
        return referenceAreaHasPattern(this, pattern);
    }

    public void assertRegionContainsPattern(UiElement pattern) throws Exception {
        Assert.assertTrue(regionContainsPattern(pattern), Logs.ERROR_PATTERN_NOT_FOUND_IN_REGION);
    }

    public void assertRegionNotContainsPattern(UiElement pattern) throws Exception {
        Assert.assertFalse(regionContainsPattern(pattern), Logs.ERROR_PATTERN_FOUND_IN_REGION);
    }

    public void assertNotVisible() throws Exception {
        Assert.assertFalse(exists(this, 0, true), Logs.STILL_VISIBLE_ASSERT_MSG);
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
