package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import com.google.common.base.Stopwatch;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

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

    protected void click(UiElement uiElement) throws FindFailed {
        if (!uiElement.isVncScreen) {
            Pattern pattern = createPattern(uiElement);
            uiElement.screen.click(pattern);
        } else {
            uiElement.vncScreen.click(createPattern(uiElement));
        }
    }

    public boolean exists(UiElement uiElement) throws Exception {
        return exists(uiElement, 0);
    }

    /***
     * Helper that checks if an image does exist.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean exists(UiElement uiElement, int timeoutSec) throws IOException, FindFailed {
        return superExists(false, null, uiElement, uiElement.takeScreenshotIfNotFound, timeoutSec);
    }


    /***
     * Helper that checks if an image does exist.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean patternExistsInRegion(Region region, UiElement uiElement, boolean screenCaptureIfMissing, double timeoutSec) throws IOException, FindFailed {
        return superExists(true, region, uiElement, screenCaptureIfMissing, timeoutSec);
    }


    /***
     * Helper that takes a screenshot from the entire desktop.
     * @return
     * @throws IOException
     */
    public String saveDesktopScreenshot(UiElement uiElement) throws IOException {
        String path = RandomValuesHelper.generateAlphabetic(10);
        String filename = CalendarHelper.getCurrentTimeAndDate();
        return saveDesktopScreenshot(uiElement, filename);
    }

    /***
     * Helper that takes a screenshot from the entire desktop.
     * @param filename: The screenshot filename.
     * @return
     * @throws IOException
     */
    protected String saveDesktopScreenshot(UiElement uiElement, String filename) throws IOException {
        String path = InputOutputHelper.createDirectory(uiElement.folderPathToSaveScreenshots);
        return !uiElement.isVncScreen ? uiElement.screen.capture().save(path, filename) :
                uiElement.vncScreen.capture().save(path, filename);
    }

    /***
     * Helper that takes a screenshot from a given region.
     * @param region: A screen region.
     * @return
     * @throws IOException
     */
    protected String saveScreenshotFromRegion(UiElement uiElement, Region region) throws IOException {
        return saveScreenshotFromRegion(region, uiElement.folderPathToSaveScreenshots,
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

    private boolean superExists(boolean isForRegion, Region region, UiElement uiElement, boolean screenCaptureIfMissing, double timeoutSec) throws IOException {
        Pattern pattern = createPattern(uiElement);
        Match match = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (stopwatch.elapsed(TimeUnit.SECONDS) <= timeoutSec) {
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
                    match = !uiElement.isVncScreen ? uiElement.screen.find(pattern) :
                            uiElement.vncScreen.find(pattern);
                }
                break;
            } catch (Exception e) {
                uiElement.testLog.logIt(e.getMessage());
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

        if (screenCaptureIfMissing && !wasFound) {
            uiElement.testLog.logIt(
                    String.format("The expected image [%s] was not found, instead found [%s]",
                            pattern.getFilename(),
                            isForRegion ? saveScreenshotFromRegion(uiElement, region) : saveDesktopScreenshot(uiElement)));
        }
        return wasFound;
    }

    /***
     * Simple helper that evaluates the min score required to consider a match successful.
     * @param currentScore: The score found on a match pattern.
     * @return
     */
    private boolean evaluateScore(UiElement uiElement, double currentScore) {
        uiElement.testLog.logIt(String.format("###Image Recognition match evaluation:[#Found pattern score:%1$s][#Minimum acceptance score:%2$s]", String.valueOf(currentScore), String.valueOf(uiElement.similarity)));
        return (currentScore >= uiElement.similarity);
    }

    private Pattern createPattern(UiElement uiElement) {
        Pattern pattern = new Pattern(uiElement.imagePath);
        if (uiElement.similarity != null)
            pattern.similar(uiElement.similarity);
        pattern.targetOffset(uiElement.xCoordinate, uiElement.yCoordinate);
        return pattern;
    }
}