package com.github.jeansantos38.stf.framework.ui;

import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import com.google.common.base.Stopwatch;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class UiAutomationHelper {

    private final String SINGLE_DOUBLE_CLICK_LOG = "[#action: %1$s],[#on: %2$s],[#at: %3$s],[#similatiry: %4$s]";
    private final String WAIT_EXISTS_LOG = "[#action: %1$s],[#on: %2$s],[#similatiry: %3$s],[#will wait: %4$s seconds]";
    private final String FIND_LOG = "[#action: %1$s],[#on: %2$s],[#similatiry: %3$s]";
    private final String DRAG_AND_DROP_LOG = "[#action: %1$s],[#Source: %2$s],[#Destination: %3$s]";

    protected void click(UiElement uiElement) throws Exception {
        find(uiElement);
        if (!uiElement.isVncScreen()) {
            uiElement.getScreen().click(uiElement.getMatch());
        } else {
            uiElement.getVncScreen().click(uiElement.getMatch());
        }
        uiElement.getWaitHelper().waitMilliseconds(uiElement.getMsDelayBetweenClicks());
    }

    protected void paste(UiElement uiElement, String content) {
        UiAutomationUtils.paste(!uiElement.isVncScreen() ? uiElement.getScreen() : uiElement.getVncScreen(), content);
    }

    protected void dragAndDrop(UiElement target, UiElement destination) throws FindFailed {
        if (!target.isVncScreen()) {
            target.getScreen().dragDrop(target.getMatch(), destination.getMatch());
        } else {
            target.getVncScreen().dragDrop(target.getMatch(), destination.getMatch());
        }
    }

    protected void type(UiElement uiElement, String content) throws InterruptedException, FindFailed {
        UiAutomationUtils.type(!uiElement.isVncScreen() ? uiElement.getScreen() : uiElement.getVncScreen(), content);
    }

    protected String copyString(UiElement uiElement) throws Exception {
        doubleClick(uiElement);
//       return UiAutomationUtils.performKeyCombination(!uiElement.isVncScreen() ? uiElement.getScreen() : uiElement.getVncScreen(),
//                "c", KeyModifier.CTRL);
        return "";
    }

    protected void moveCursorOver(UiElement uiElement) throws Exception {
        find(uiElement);
        if (!uiElement.isVncScreen()) {
            uiElement.getScreen().hover(uiElement.getMatch());
        } else {
            uiElement.getVncScreen().hover(uiElement.getMatch());
        }
    }

    protected void doubleClickRegion(UiElement uiElement, Region region) throws FindFailed {
        region.doubleClick(createPattern(uiElement));

    }

    protected void doubleClick(UiElement uiElement) throws Exception {
        find(uiElement);

        if (!uiElement.isVncScreen()) {
            uiElement.getScreen().doubleClick(uiElement.getMatch());
        } else {
            uiElement.getVncScreen().doubleClick(uiElement.getMatch());
        }
        uiElement.getWaitHelper().waitMilliseconds(uiElement.getMsDelayBetweenClicks());
    }

    public boolean exists(UiElement uiElement) throws Exception {
        return exists(uiElement, 0);
    }

    /***
     * Helper that checks if an image does exist.
     * @param timeoutMs: How long will keep trying before given up from this operation.
     * @return
     */
    protected boolean exists(UiElement uiElement, int timeoutMs, boolean screenCaptureIfFound) throws IOException, FindFailed {
        return superExists(false, null, uiElement, screenCaptureIfFound, timeoutMs);
    }

    protected boolean exists(UiElement uiElement, int timeoutMs) throws IOException, FindFailed {
        return superExists(false, null, uiElement, false, timeoutMs);
    }


    /***
     * Helper that takes a screenshot from a given region.
     * @param region: A screen region.
     * @return
     * @throws IOException
     */
    protected String saveScreenshotFromRegion(UiElement uiElement, Region region) throws IOException {
        return saveScreenshotFromRegion(region, uiElement.getFolderPathToSaveScreenshots(),
                String.format("%s_%s", RandomValuesHelper.generateAlphabetic(10), CalendarHelper.getCurrentTimeAndDate()));
    }

    /***
     * Helper that takes a screenshot from a given region.
     * @param region: A screen region.
     * @param path: Folder where it should be save.
     * @param filename: The screenshot filename.
     * @return
     * @throws IOException
     */
    protected String saveScreenshotFromRegion(Region region, String path, String filename) throws IOException {
        InputOutputHelper.createDirectory(path);
        region.getImage().save(filename + ".png", path);
        return String.format("%s/%s", path, filename);
    }


    protected Region createRegionFromReferencePattern(UiElement uiElement) throws Exception {
        Match patternMatch = find(uiElement);
        int recH = uiElement.getDetails().regionBottomRightY - uiElement.getDetails().regionTopLeftY;
        int recW = uiElement.getDetails().regionBottomRightX - uiElement.getDetails().regionTopLeftX;
        int finalX = patternMatch.x + ((patternMatch.w / 2) + uiElement.getDetails().regionTopLeftX);
        int finalY = patternMatch.y + ((patternMatch.h / 2) + uiElement.getDetails().regionTopLeftY);
        Region region = new Region(finalX, finalY, recW, recH);
        highlight(uiElement, region);
        return region;
    }

    protected String extractTextFromRegionViaOCR(UiElement uiElement) throws Exception {
        Settings.OcrTextRead = true;
        Settings.OcrTextSearch = true;
        Region region = createRegionFromReferencePattern(uiElement);
        return region.text();
    }

    protected boolean referenceAreaHasPattern(UiElement reference, UiElement expectedPattern) throws Exception {
        Region region = createRegionFromReferencePattern(reference);
        return superExists(true, region, expectedPattern, reference.takeScreenshotWhenFail(), 0);
    }

    protected Match find(UiElement uiElement) throws Exception {
        Pattern pattern = createPattern(uiElement);
        try {
            if (uiElement.getMatch() == null) {
                uiElement.setMatch(!uiElement.isVncScreen() ? uiElement.getScreen().find(pattern) : uiElement.getVncScreen().find(pattern));
            }
            highlight(uiElement, uiElement.getMatch());
            return uiElement.getMatch();
        } catch (Exception e) {
            if (uiElement.takeScreenshotWhenFail()) {
                uiElement.getTestLog().logIt(String.format("The pattern was not found! ###The expected master is:[%1$s] ### It was not found at: [%2$s]",
                        pattern.getFilename(), UiAutomationUtils.saveDesktopScreenshot(!uiElement.isVncScreen() ? uiElement.getScreen() : uiElement.getVncScreen(), uiElement.getFolderPathToSaveScreenshots())));
            }
            throw new Exception(e);
        }
    }

    private void highlight(UiElement uiElement, Region region) {
        UiVisualFeedback visualFeedback = uiElement.getUiVisualFeedback();
        if (visualFeedback != null && visualFeedback.enableHighlight && !uiElement.isVncScreen()) {
            region.highlight(visualFeedback.highlightTimeSec, visualFeedback.areaHighlightColor);
        } else {
            uiElement.getTestLog().logIt("Nothing to highlight here!");
        }
    }

    private void highlight(UiElement uiElement, Match match) {
        UiVisualFeedback visualFeedback = uiElement.getUiVisualFeedback();
        if (visualFeedback != null && visualFeedback.enableHighlight && !uiElement.isVncScreen()) {
            match.highlight(visualFeedback.highlightTimeSec, visualFeedback.masterHighlightColor);
            if (uiElement.getDetails().xCoordinate != 0 || uiElement.getDetails().yCoordinate != 0) {
                new Region(match.getCenter().x + uiElement.getDetails().xCoordinate,
                        match.getCenter().y + uiElement.getDetails().yCoordinate, visualFeedback.relHighlightW, visualFeedback.relHighlightH)
                        .highlight(visualFeedback.highlightTimeSec, visualFeedback.coordinateHighlightColor);
            }
        } else {
            uiElement.getTestLog().logIt("Nothing to highlight here!");
        }
    }

    private boolean superExists(boolean isForRegion, Region region, UiElement uiElement, boolean screenCaptureIfFound, double timeoutSec) throws IOException {
        Pattern pattern = createPattern(uiElement);
        Match match = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (stopwatch.elapsed(TimeUnit.MILLISECONDS) <= timeoutSec) {
            try {
                /*
                If you're thinking "Why the heck we're using find here instead exists?!"
                Well - I've some bad news for you - or not. The exists method is not reliable.
                It fails often on MAC and Windows.
                After some research I've found that there's a defect for that in the LIB repo (at the date of this sentence first commit).
                So as workaround I'm using the FIND, but making it behave as Exists.
                 It might not be the right thing to do, but the world is cruel sometimes.
                 This way it simple works - it's reliable. That's it.
                 */
                if (isForRegion) {
                    match = region.find(pattern);
                } else {
                    match = find(uiElement);
                }
                break;
            } catch (Exception e) {
                uiElement.getTestLog().logIt(e.getMessage());
            }
        }
        boolean wasFound = false;
        /*
        You might be asking why we've this insanity here. The truth is quite simple.
         Sometimes this lib is capable of evaluating the given pattern inside a region and return a percentage below 1,
         meaning that we don't have a match. In other occasions it simply returns null because was not able to find it at all.
         Both results have the same meaning - THERE'S NO MATCH. Any complains about it please go ask for MIT that has developed it. Thanks!
         */
        if (match != null) {
            double current = match.getScore();
            wasFound = evaluateScore(uiElement, current);
        }

        if (uiElement.takeScreenshotWhenFail() && !wasFound) {
            uiElement.getTestLog().logIt(
                    String.format("The expected image [%s] was not found, instead found [%s]",
                            pattern.getFilename(),
                            isForRegion ? saveScreenshotFromRegion(uiElement, region) :
                                    UiAutomationUtils.saveDesktopScreenshot(
                                            uiElement.isVncScreen() ? uiElement.getVncScreen() : uiElement.getScreen(),
                                            uiElement.getFolderPathToSaveScreenshots())));
        } else if (screenCaptureIfFound && wasFound) {
            uiElement.getTestLog().logIt(
                    String.format("The expected image [%s] was found, see it on captured screenshot [%s]",
                            pattern.getFilename(),
                            isForRegion ? saveScreenshotFromRegion(uiElement, region) :
                                    UiAutomationUtils.saveDesktopScreenshot(
                                            uiElement.isVncScreen() ? uiElement.getVncScreen() : uiElement.getScreen(),
                                            uiElement.getFolderPathToSaveScreenshots())));
        }
        return wasFound;
    }

    /***
     * Simple helper that evaluates the min score required to consider a match successful.
     * @param currentScore: The score found on a match pattern.
     * @return
     */
    private boolean evaluateScore(UiElement uiElement, double currentScore) {
        uiElement.getTestLog().logIt(String.format("###Image Recognition match evaluation:[#Found pattern score:%1$s][#Minimum acceptance score:%2$s]",
                String.valueOf(currentScore), String.valueOf(uiElement.getDetails().similarity)));
        return (currentScore >= uiElement.getDetails().similarity);
    }

    private Pattern createPattern(UiElement uiElement) {
        Pattern pattern = new Pattern(uiElement.getDetails().imagePath);

        pattern.similar(uiElement.getDetails().similarity);
        pattern.targetOffset(uiElement.getDetails().xCoordinate, uiElement.getDetails().yCoordinate);
        return pattern;
    }

    protected void actionClick(UiElement uiElement, boolean isLeftClick, int howManyClicks) throws FindFailed, InterruptedException {
        for (int i = 0; i < howManyClicks; i++) {
            if (!uiElement.isVncScreen()) {
                uiElement.getScreen().mouseDown(isLeftClick ? Button.LEFT : Button.RIGHT);
                uiElement.getWaitHelper().waitMilliseconds(uiElement.getMsDelayBetweenActions());
                uiElement.getScreen().mouseUp(isLeftClick ? Button.LEFT : Button.RIGHT);
            } else {
                uiElement.getVncScreen().mouseDown(isLeftClick ? Button.LEFT : Button.RIGHT);
                uiElement.getWaitHelper().waitMilliseconds(uiElement.getMsDelayBetweenActions());
                uiElement.getVncScreen().mouseUp(isLeftClick ? Button.LEFT : Button.RIGHT);
            }
            uiElement.getWaitHelper().waitMilliseconds(uiElement.getMsDelayBetweenClicks());
        }
    }
}