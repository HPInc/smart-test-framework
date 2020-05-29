package hp.inc.jsg.qa.stf.framework.desktop;

import com.google.common.base.Stopwatch;
import hp.inc.jsg.qa.stf.dataclasses.web.datadriven.DataDrivenNavigator;
import hp.inc.jsg.qa.stf.dataclasses.web.datadriven.Elements;
import hp.inc.jsg.qa.stf.dataclasses.web.datadriven.MasterImageDetails;
import hp.inc.jsg.qa.stf.enums.image.Action;
import hp.inc.jsg.qa.stf.enums.serialization.SerializationType;
import hp.inc.jsg.qa.stf.framework.io.InputOutputHelper;
import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import hp.inc.jsg.qa.stf.framework.serialization.DeserializeHelper;
import hp.inc.jsg.qa.stf.framework.wait.WaitHelper;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class DesktopAutomationHelper {

    private final String SINGLE_DOUBLE_CLICK_LOG = "[#action: %1$s],[#on: %2$s],[#at: %3$s],[#similatiry: %4$s]";
    private final String WAIT_EXISTS_LOG = "[#action: %1$s],[#on: %2$s],[#similatiry: %3$s],[#will wait: %4$s seconds]";
    private final String FIND_LOG = "[#action: %1$s],[#on: %2$s],[#similatiry: %3$s]";
    private final String DRAG_AND_DROP_LOG = "[#action: %1$s],[#Source: %2$s],[#Destination: %3$s]";
    private final double SURE_MATCH_MIN_SCORE = 0.95;
    private final double DEFAULT_WAIT_AFTER_SINGLE_CLICK = 100;
    private final double DEFAULT_BETWEEN_MULTIPLE_CLICK = 20;
    private String folderPathToSaveScreenshots;

    private Screen screen;
    private TestLog testLog;
    private boolean shouldWaitAfterClick;
    private boolean shouldWaitAfterMultipleClicks;
    private WaitHelper waitHelper;

    private boolean takeFullScreenshotIfPatternNotFound;
    private boolean takeRegionScreenshotIfPatternNotFound;
    private boolean takeScreenshotIfOCRNotFound;
    private double delayAfterSingleClick;
    private double delayBetweenMultipleClicks;
    private double sureMatchMinimumScore;

    public boolean getTakeFullScreenshotIfPatternNotFound() {
        return takeFullScreenshotIfPatternNotFound;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public boolean getTakeRegionScreenshotIfPatternNotFound() {
        return takeRegionScreenshotIfPatternNotFound;
    }

    public boolean getTakeScreenshotIfOCRNotFound() {
        return takeScreenshotIfOCRNotFound;
    }

    /***
     *
     * @param testLog
     * @param screen
     * @param folderPathToSaveScreenshots
     * @throws Exception
     */
    public DesktopAutomationHelper(TestLog testLog, Screen screen, String folderPathToSaveScreenshots) throws Exception {
        this(testLog, screen, folderPathToSaveScreenshots, 0.95, 100, 20, true, true, true);
    }

    /**
     * @param testLog
     * @param screen
     * @param folderPathToSaveScreenshots
     * @param sureMatchMinimumScore
     * @param delayAfterSingleClickInMs
     * @param delayBetweenMultipleClicksInMs
     * @param takeFullScreenshotIfPatternNotFound
     * @param takeRegionScreenshotIfPatternNotFound
     * @param takeScreenshotIfOCRNotFound
     * @throws Exception
     */
    public DesktopAutomationHelper(TestLog testLog, Screen screen, String folderPathToSaveScreenshots, double sureMatchMinimumScore, double delayAfterSingleClickInMs, double delayBetweenMultipleClicksInMs, boolean takeFullScreenshotIfPatternNotFound, boolean takeRegionScreenshotIfPatternNotFound, boolean takeScreenshotIfOCRNotFound) throws Exception {
        this.screen = screen;

        this.testLog = testLog;
        this.waitHelper = new WaitHelper();

        if (folderPathToSaveScreenshots.isEmpty()) {
            throw new Exception("Folder path to save screenshots cannot be empty!");
        }
        this.folderPathToSaveScreenshots = folderPathToSaveScreenshots;

        this.shouldWaitAfterClick = delayAfterSingleClickInMs > 0;
        this.shouldWaitAfterMultipleClicks = delayBetweenMultipleClicksInMs > 0;
        this.takeFullScreenshotIfPatternNotFound = takeFullScreenshotIfPatternNotFound;
        this.takeRegionScreenshotIfPatternNotFound = takeRegionScreenshotIfPatternNotFound;
        this.takeScreenshotIfOCRNotFound = takeScreenshotIfOCRNotFound;
        this.delayAfterSingleClick = delayAfterSingleClickInMs > 0 ? delayAfterSingleClickInMs : DEFAULT_WAIT_AFTER_SINGLE_CLICK;
        this.delayBetweenMultipleClicks = delayBetweenMultipleClicksInMs > 0 ? delayBetweenMultipleClicksInMs : DEFAULT_BETWEEN_MULTIPLE_CLICK;
        this.sureMatchMinimumScore = sureMatchMinimumScore > 0 ? sureMatchMinimumScore : SURE_MATCH_MIN_SCORE;
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    public void click(Pattern pattern) throws Exception {
        superAction(Action.CLICK, true, pattern, null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    public void click(Region region, Pattern pattern) throws Exception {
        superAction(Action.CLICK, true, region, pattern, null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @throws Exception
     */
    public void click(String imageFilenamePath) throws Exception {
        superAction(Action.CLICK, true, createPattern(imageFilenamePath, 0, 0, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @throws Exception
     */
    public void click(String imageFilenamePath, int xCoordinate, int yCoordinate) throws Exception {
        superAction(Action.CLICK, true, createPattern(imageFilenamePath, xCoordinate, yCoordinate, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @throws Exception
     */
    public void click(String imageFilenamePath, int xCoordinate, int yCoordinate, float similarity) throws Exception {
        superAction(Action.CLICK, true, createPattern(imageFilenamePath, xCoordinate, yCoordinate, similarity), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @throws Exception
     */
    public void rightClick(String imageFilenamePath, int xCoordinate, int yCoordinate, float similarity) throws Exception {
        superAction(Action.CLICK, false, createPattern(imageFilenamePath, xCoordinate, yCoordinate, similarity), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    public void rightClick(Pattern pattern) throws Exception {
        superAction(Action.CLICK, false, pattern, null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @throws Exception
     */
    public void rightClick(String imageFilenamePath) throws Exception {
        superAction(Action.CLICK, false, createPattern(imageFilenamePath, 0, 0, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @throws Exception
     */
    public void rightClick(String imageFilenamePath, int xCoordinate, int yCoordinate) throws Exception {
        superAction(Action.CLICK, false, createPattern(imageFilenamePath, xCoordinate, yCoordinate, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    public void doubleClick(Pattern pattern) throws Exception {
        superAction(Action.DOUBLE_CLICK, true, pattern, null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @throws Exception
     */
    public void doubleClick(String imageFilenamePath) throws Exception {
        superAction(Action.DOUBLE_CLICK, true, createPattern(imageFilenamePath, 0, 0, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @throws Exception
     */
    public void doubleClick(String imageFilenamePath, int xCoordinate, int yCoordinate) throws Exception {
        superAction(Action.DOUBLE_CLICK, true, createPattern(imageFilenamePath, xCoordinate, yCoordinate, 0), null, 0);
    }

    /***
     * Helper that moves the cursor over an image offset then performs a click action related to the method name.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @throws Exception
     */
    public void doubleClick(String imageFilenamePath, int xCoordinate, int yCoordinate, float similarity) throws Exception {
        superAction(Action.DOUBLE_CLICK, true, createPattern(imageFilenamePath, xCoordinate, yCoordinate, similarity), null, 0);
    }

    /***
     * Helper that checks if an image does exist.
     * @param imageFilenamePath: The full filename path from an master image.
     * @return
     */
    public boolean exists(String imageFilenamePath) throws IOException, FindFailed {
        return exists(imageFilenamePath, 0, 0);
    }

    /***
     * Helper that checks if an image does exist.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean exists(String imageFilenamePath, double timeoutSec) throws IOException, FindFailed {
        return exists(imageFilenamePath, 0, timeoutSec);
    }

    /***
     * Helper that checks if an image does exist.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean exists(String imageFilenamePath, float similarity, double timeoutSec) throws IOException, FindFailed {
        testLog.logIt(String.format(WAIT_EXISTS_LOG, "Exists", imageFilenamePath, String.valueOf(similarity), String.valueOf(timeoutSec)));
        return exists(createPattern(imageFilenamePath, 0, 0, similarity), timeoutSec);
    }

    /***
     * Helper that checks if an image does exist.
     * @param pattern: An object from a master image file.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean exists(Pattern pattern, double timeoutSec) throws IOException, FindFailed {
        return exists(pattern, this.takeFullScreenshotIfPatternNotFound, timeoutSec);
    }

    /***
     * Helper that checks if an image does exist.
     * @param pattern: An object from a master image file.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean patternExistsInRegion(Region region, Pattern pattern, boolean screenCaptureIfMissing, double timeoutSec) throws IOException, FindFailed {
        return superExists(true, region, pattern, screenCaptureIfMissing, timeoutSec);
    }

    /***
     * Helper that checks if an image does exist.
     * @param pattern: An object from a master image file.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean exists(Pattern pattern, boolean screenCaptureIfMissing, double timeoutSec) throws IOException, FindFailed {
        return superExists(false, null, pattern, screenCaptureIfMissing, timeoutSec);
    }

    /**
     * THis is the main helper to check if a pattern does exist.
     *
     * @param isForRegion:           Is for a region or false for the entire screen.
     * @param region:                A screen region.
     * @param pattern:               A object from a master image file.
     * @param screenCaptureIfMissing
     * @param timeoutSec:            How long will keep trying before given up from this operation.
     * @return
     * @throws IOException
     */
    private boolean superExists(boolean isForRegion, Region region, Pattern pattern, boolean screenCaptureIfMissing, double timeoutSec) throws IOException {
        Match match = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (stopwatch.elapsed(TimeUnit.SECONDS) <= timeoutSec) {
            try {
                // If you're thinking "Why the heck we're using find here instead exists?!", Well - I've some bad news for you - or not. Exists method is not reliable. It fails often on MAC and Windows. After some research I've found that there's a defect for that in the LIB repo (at the date of this sentence first commit). So as workaround I'm using the FIND, but making it behave as Exists. It might not be the right thing to do, but the world is cruel sometimes. This way it simple works - it's reliable. That's it.
                match = isForRegion ? region.find(pattern) : this.screen.find(pattern);
                break;
            } catch (Exception e) {
                this.testLog.logIt(e.getMessage());
            }
        }
        boolean wasFound = false;
        //You might be asking why we've this insanity here. The truth is quite simple. Sometimes this lib is capable of evaluating the given patter inside a region and return a percentage below 1, meaning that we don't have a match. In other occasions it simply returns null because was not able to find it at all. Both results have the same meaning - THERE'S NO MATCH. Any complains about it please go ask for MIT that has developed SikuliX. Thanks!
        if (match != null) {
            double current = match.getScore();
            wasFound = evaluateScore(current);
        }

        if (screenCaptureIfMissing && !wasFound) {
            this.testLog.logIt(String.format("The expected image [%1$s] was not found, instead found [%2$s]", pattern.getFilename(), isForRegion ? saveScreenshotFromRegion(region) : saveDesktopScreenshot()));
        }
        return wasFound;
    }

    /***
     * Helper that does perform a Find action.
     * @param imageFilenamePath: The full filename path from an master image.
     * @return
     * @throws Exception
     */
    public Match find(String imageFilenamePath) throws Exception {
        return (Match) superAction(Action.FIND, false, createPattern(imageFilenamePath, 0, 0, 0), null, 0);
    }

    /**
     * Simple helper to find and return a pattern match. The difference between this override and the native match is that this one provides more logs.
     *
     * @param pattern: An object from a master image file.
     * @return
     * @throws Exception
     */
    public Match find(Pattern pattern) throws Exception {
        return (Match) superAction(Action.FIND, false, pattern, null, 0);
    }

    /***
     * Helper that does perform a Find action.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @return
     * @throws Exception
     */
    public Match find(String imageFilenamePath, float similarity) throws Exception {
        return (Match) superAction(Action.FIND, false, createPattern(imageFilenamePath, 0, 0, similarity), null, 0);
    }

    /***
     * Helper that wait for an image become available.
     * @param pattern: An object from a master image file.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @throws Exception
     */
    public void waitPatternExists(Pattern pattern, double timeoutSec) throws Exception {
        superAction(Action.WAIT_FOR, false, pattern, null, timeoutSec);
    }

    /***
     * Helper that wait for an image become available.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @throws Exception
     */
    public void waitPatternExists(String imageFilenamePath, double timeoutSec) throws Exception {
        waitPatternExists(imageFilenamePath, 0, timeoutSec);
    }

    /***
     * Helper that wait for an image become available.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @throws Exception
     */
    public void waitPatternExists(String imageFilenamePath, float similarity, double timeoutSec) throws Exception {
        superAction(Action.WAIT_FOR, false, createPattern(imageFilenamePath, 0, 0, similarity), null, timeoutSec);
    }

    /***
     * Helper that wait until an image isn't available anymore.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean waitPatternVanishes(String imageFilenamePath, double timeoutSec) {
        return waitPatternVanishes(imageFilenamePath, 0, timeoutSec);
    }

    /***
     * Helper that wait until an image isn't available anymore.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return
     */
    public boolean waitPatternVanishes(String imageFilenamePath, float similarity, double timeoutSec) {
        return screen.waitVanish(createPattern(imageFilenamePath, 0, 0, similarity), timeoutSec);
    }

    /***
     * Helper that abstract how a pattern is created, leaving to the user just the easy(fun) part.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xCoordinate: The X coordinate from where a pattern should be clicked for instance. 
     * @param yCoordinate: The Y coordinate from where a pattern should be clicked for instance. 
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @return
     */
    private Pattern createPattern(String imageFilenamePath, int xCoordinate, int yCoordinate, float similarity) {
        Pattern pattern;
        pattern = new Pattern(imageFilenamePath);
        if (xCoordinate != 0 || yCoordinate != 0) {
            pattern.targetOffset(new Location(xCoordinate, yCoordinate));
        }
        //Otherwise (==0) it will leave default 70%
        if (similarity != 0 && similarity != 0.0) {
            pattern.similar(similarity);
        }
        return pattern;
    }

    /***
     * This helper retrieves an navigator element based on given file and area ID.
     *
     * I was not able to have both methods above in a single method (without the need of adding one more parameter), because it's assembled dynamically and the base paths relative for OS and apps is different in number of sub-folders and its structure. So it was easy to add a new property in the constructor then know if we need to read one or another in order to assemble the path correctly.
     *
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param areaId: The areaId is just an identifier related to where a navigator is located. You should provide this value by looking into navigator files.
     * @param selector: The selector corresponds to a master image file details. You should provide this value by looking into navigator files.
     * @return
     * @throws Exception
     */
    public Pattern retrievePatternFromNavigator(String navigatorFileFullPath, String areaId, String selector) throws Exception {
        return retrievePatternFromNavigator(navigatorFileFullPath, areaId, selector, true);
    }

    /***
     * This helper retrieves an navigator element based on given file and area ID.
     *
     * I was not able to have both methods above in a single method (without the need of adding one more parameter), because it's assembled dynamically and the base paths relative for OS and apps is different in number of sub-folders and its structure. So it was easy to add a new property in the constructor then know if we need to read one or another in order to assemble the path correctly.
     *
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param areaId: The areaId is just an identifier related to where a navigator is located. You should provide this value by looking into navigator files.
     * @param selector: The selector corresponds to a master image file details. You should provide this value by looking into navigator files.
     * @param retrieveMasterLocOnly: All navigators have a master file(image), but some others requires a reference master. E.g. An app that has several identical checkboxes - that will probably lead the test into a false positive (by finding the first one available). So we combine a reference master (from the navigator) with region coordinates(from data driven), then using the master we can locate a specific item among several identical others.
     * @return
     * @throws Exception
     */
    public Pattern retrievePatternFromNavigator(String navigatorFileFullPath, String areaId, String selector, boolean retrieveMasterLocOnly) throws Exception {

        String navigatorContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
        DataDrivenNavigator dataDrivenNavigator = DeserializeHelper.deserializeStringToObject(DataDrivenNavigator.class, SerializationType.JSON, navigatorContent);
        MasterImageDetails masterImageDetails = Arrays.stream(dataDrivenNavigator.MasterImageDetails).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
        if (masterImageDetails == null)
            throw new Exception(String.format("The master image details for area '%1$s' was not retrieved from navigator, pls review the given parameters.", areaId));
        String masterAbsolutePath = "";
        Elements elements = Arrays.stream(masterImageDetails.elements).filter(x -> x.element.selector.equals(selector)).findFirst().orElse(null);
        if (elements == null || elements.element == null)
            throw new Exception(String.format("The element for selector '%1$s' was not retrieved from navigator, pls review the given parameters.", selector));

        File navigator = new File(navigatorFileFullPath);
        masterAbsolutePath = navigator.getAbsolutePath().replace(navigator.getName(), (retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc));
        this.testLog.logIt(String.format("Searching for the image '%1$s' related to the selector '%2$s'.", retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc, selector));

        return createPattern(masterAbsolutePath, elements.element.xCoordinate, elements.element.yCoordinate, elements.element.similarity);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param yHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverClick(boolean isLeftButton, String imageFilenamePath, int xHoverCoordinate, int yHoverCoordinate) throws FindFailed, InterruptedException {
        hoverClick(isLeftButton, imageFilenamePath, xHoverCoordinate, yHoverCoordinate, 0);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param yHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverClick(boolean isLeftButton, String imageFilenamePath, int xHoverCoordinate, int yHoverCoordinate, float similarity) throws FindFailed, InterruptedException {
        superHoverClick(isLeftButton, 1, createPattern(imageFilenamePath, xHoverCoordinate, yHoverCoordinate, similarity));
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param yHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverDoubleClick(boolean isLeftButton, String imageFilenamePath, int xHoverCoordinate, int yHoverCoordinate) throws FindFailed, InterruptedException {
        hoverDoubleClick(isLeftButton, imageFilenamePath, xHoverCoordinate, yHoverCoordinate, 0);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param imageFilenamePath: The full filename path from an master image.
     * @param xHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param yHoverCoordinate: The X coordinate from an unknown area, using a known pattern as reference.
     * @param similarity: The similarity percentage when searching for a pattern. Default is 70% 
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverDoubleClick(boolean isLeftButton, String imageFilenamePath, int xHoverCoordinate, int yHoverCoordinate, float similarity) throws FindFailed, InterruptedException {
        superHoverClick(isLeftButton, 2, createPattern(imageFilenamePath, xHoverCoordinate, yHoverCoordinate, similarity));
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverClick(Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(true, 1, pattern);
    }

    /**
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     *
     * @param region:  A screen region.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverClick(Region region, Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(true, 1, region, pattern);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverTripleClick(Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(true, 3, pattern);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverDoubleClick(Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(true, 2, pattern);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverClick(boolean isLeftButton, Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(isLeftButton, 1, pattern);
    }

    /***
     * Helper that moves the cursor over an area (outside the pattern) then performs a click action related to the method name.
     * @param isLeftButton: Is a left click or false for right click.
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    public void hoverDoubleClick(boolean isLeftButton, Pattern pattern) throws FindFailed, InterruptedException {
        superHoverClick(isLeftButton, 2, pattern);
    }

    /***
     * Move the mouse cursor over the requested pattern
     * @param pattern: The target image
     * @throws FindFailed
     */
    public void mouseOver(Pattern pattern) throws FindFailed {
        this.screen.hover(pattern);
    }

    /***
     * This helper returns the deserialized navigator element from a given selector and area.
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param areaId: The areaId is just an identifier related to where a navigator is located. You should provide this value by looking into navigator files.
     * @param selector: The selector corresponds to a master image file details. You should provide this value by looking into navigator files.
     * @return
     * @throws Exception
     */
    public Elements retrieveTargetElement(String navigatorFileFullPath, String areaId, String selector) throws Exception {
        String dataDrivenContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
        DataDrivenNavigator dataDrivenNavigator = DeserializeHelper.deserializeStringToObject(DataDrivenNavigator.class, SerializationType.JSON, dataDrivenContent);
        MasterImageDetails masterImageDetails = Arrays.stream(dataDrivenNavigator.MasterImageDetails).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
        return Arrays.stream(masterImageDetails.elements).filter(x -> x.element.selector.equals(selector)).findFirst().orElse(null);
    }

    /***
     * This helper creates a region using as reference a pattern along with coordinates given. It will abort if the pattern is not found.
     * In order to create these values, you'll need to use Sikuli IDE.
     * Considering the region could be a square or a rectangle, so ....
     * @param pattern: An object from a master image file.
     * @param recTopLeftX: The top left X coordinate.
     * @param recTopLeftY: The top left Y coordinate.
     * @param recBottomRightX: The bottom right X coordinate.
     * @param recBottomRightY: The bottom right Y coordinate.
     * @return
     * @throws FindFailed
     */
    public Region createRegionFromReferencePattern(Pattern pattern, int recTopLeftX, int recTopLeftY, int recBottomRightX, int recBottomRightY) throws Exception {
        Settings.OcrTextRead = true;
        Settings.OcrTextSearch = true;

        Match patternMatch = find(pattern);

        int recH = recBottomRightY - recTopLeftY;
        int recW = recBottomRightX - recTopLeftX;
        int finalX = patternMatch.x + ((patternMatch.w / 2) + recTopLeftX);
        int finalY = patternMatch.y + ((patternMatch.h / 2) + recTopLeftY);

        return new Region(finalX, finalY, recW, recH);
    }

    /**
     * Drag and Drop helper. It will pick an image (by clicking at it center) then leaving at the center of destination image.
     *
     * @param sourceImageFullFilename:      The full filename that contains the pattern of item to be dragged and dropped in another place.
     * @param destinationImageFullFilename: The full filename that contains the pattern where an item will be dropped into.
     * @throws Exception
     */
    public void dragAndDrop(String sourceImageFullFilename, String destinationImageFullFilename) throws Exception {
        superAction(Action.DRAG_AND_DROP, false, createPattern(sourceImageFullFilename, 0, 0, 0), createPattern(destinationImageFullFilename, 0, 0, 0), 0);
    }

    /**
     * Drag and Drop helper. It will pick the source image and move it into the destination image.
     *
     * @param source:      The pattern of item to be dragged and dropped in another place.
     * @param destination: The pattern where an item will be dropped into.
     * @throws Exception
     */
    public void dragAndDrop(Pattern source, Pattern destination) throws Exception {
        superAction(Action.DRAG_AND_DROP, false, source, destination, 0);
    }

    /***
     * Helper that takes a screenshot from the entire desktop.
     * @return
     * @throws IOException
     */
    public String saveDesktopScreenshot() throws IOException {
        return saveDesktopScreenshot(this.folderPathToSaveScreenshots, "");
    }

    /***
     * Helper that takes a screenshot from the entire desktop.
     * @param path: Folder where it should be save.
     * @param filename: The screenshot filename.
     * @return
     * @throws IOException
     */
    public String saveDesktopScreenshot(String path, String filename) throws IOException {
        InputOutputHelper.createDirectory(this.folderPathToSaveScreenshots);
        return this.screen.saveScreenCapture(path, filename);
    }

    /***
     * Helper that takes a screenshot from a given region.
     * @param region: A screen region.
     * @return
     * @throws IOException
     */
    public String saveScreenshotFromRegion(Region region) throws IOException {
        return saveScreenshotFromRegion(region, this.folderPathToSaveScreenshots, "");
    }

    /***
     * Helper that takes a screenshot from a given region.
     * @param region: A screen region.
     * @param path: Folder where it should be save.
     * @param filename: The screenshot filename.
     * @return
     * @throws IOException
     */
    public String saveScreenshotFromRegion(Region region, String path, String filename) throws IOException {
        InputOutputHelper.createDirectory(path);
        return region.saveScreenCapture(path, filename);
    }

    /***
     * This helper perform the mouse actions that does perform click actions.
     * @param isLeftClick: Use false for right click.
     * @param howManyClicks: How many times it should click repeatedly?
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    private void superHoverClick(boolean isLeftClick, int howManyClicks, Pattern pattern) throws InterruptedException, FindFailed {
        superHoverClick(isLeftClick, howManyClicks, null, pattern);
    }

    /***
     * This helper perform the mouse actions that does perform click actions.
     * @param isLeftClick: Use false for right click.
     * @param howManyClicks: How many times it should click repeatedly?
     * @param pattern: An object from a master image file.
     * @throws FindFailed
     * @throws InterruptedException
     */
    private void superHoverClick(boolean isLeftClick, int howManyClicks, Region region, Pattern pattern) throws FindFailed, InterruptedException {
        if (region == null) {
            screen.hover(pattern);
        } else {
            region.hover(pattern);
        }
        for (int i = 0; i < howManyClicks; i++) {
            screen.mouseDown(isLeftClick ? Button.LEFT : Button.RIGHT);
            //This is hardcoded! Yes, I know. Without it, Windows and Linux works well, but MAC becomes unreliable, go figure..
            waitAfterClickAction(120);
            screen.mouseUp(isLeftClick ? Button.LEFT : Button.RIGHT);
            if (this.shouldWaitAfterMultipleClicks) {
                waitAfterClickAction(this.delayBetweenMultipleClicks);
            }
        }
    }

    /***
     * This is the main action for the some of the most important features of SikuliX API. Use it for screen actions.
     * @param action: It's what should be done.
     * @param isLeftClick: Use false for right click.
     * @param pattern1: The main pattern that will be receive an action.
     * @param pattern2: Only for drag and drop. The pattern where an item will be dropped into. Leave null for other actions.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return Usually null, but just for Find it might return a MATCH.
     * @throws Exception
     */
    private Object superAction(Action action, boolean isLeftClick, Pattern pattern1, Pattern pattern2, double timeoutSec) throws Exception {
        return superAction(action, isLeftClick, null, pattern1, pattern2, timeoutSec);
    }

    /***
     * This is the main action for the some of the most important features of SikuliX API. Use it for screen or region actions.
     * @param action: It's what should be done.
     * @param isLeftClick: Use false for right click.
     * @param region: A screen region.
     * @param pattern1: The main pattern that will be receive an action.
     * @param pattern2: Only for drag and drop. The pattern where an item will be dropped into. Leave null for other actions.
     * @param timeoutSec: How long will keep trying before given up from this operation.
     * @return Usually null, but just for Find it might return a MATCH.
     * @throws Exception
     */
    private Object superAction(Action action, boolean isLeftClick, Region region, Pattern pattern1, Pattern pattern2, double timeoutSec) throws Exception {
        String mainImagePath = pattern1.getImage().getName();
        String mainImageOffset = pattern1.getTargetOffset().toString();
        String mainImageSimilarity = String.valueOf(pattern1.getSimilar());

        switch (action) {
            case CLICK:
                testLog.logIt(String.format(SINGLE_DOUBLE_CLICK_LOG, action.toString(), mainImagePath, mainImageOffset, mainImageSimilarity));
                if (isLeftClick) {
                    if (region == null) {
                        screen.click(pattern1);
                    } else {
                        region.click(pattern1);
                    }
                } else {
                    if (region == null) {
                        screen.rightClick(pattern1);
                    } else {
                        region.rightClick(pattern1);
                    }
                }
                if (this.shouldWaitAfterClick)
                    waitAfterClickAction(this.delayAfterSingleClick);
                break;

            case DOUBLE_CLICK:
                testLog.logIt(String.format(SINGLE_DOUBLE_CLICK_LOG, action.toString(), mainImagePath, mainImageOffset, mainImageSimilarity));
                if (region == null) {
                    screen.doubleClick(pattern1);
                } else {
                    region.doubleClick(pattern1);
                }
                if (this.shouldWaitAfterClick)
                    waitAfterClickAction(this.delayAfterSingleClick);
                break;

            case DRAG_AND_DROP:
                testLog.logIt(String.format(DRAG_AND_DROP_LOG, action.toString(), pattern1.getFilename(), pattern2.getFilename()));
                this.screen.dragDrop(pattern1, pattern2);
                break;

            case FIND:
                testLog.logIt(String.format(FIND_LOG, action.toString(), mainImagePath, mainImageSimilarity));
                if (region == null) {
                    try {
                        return screen.find(pattern1);
                    } catch (Exception e) {
                        if (this.takeFullScreenshotIfPatternNotFound) {
                            this.testLog.logIt(String.format("The pattern was not found! ###The expected master is:[%1$s] ### It was not found at: [%2$s]", pattern1.getFilename(), saveDesktopScreenshot()));
                        }
                        throw new Exception(e);
                    }
                } else {
                    try {
                        return region.find(pattern1);
                    } catch (Exception e) {
                        if (this.takeRegionScreenshotIfPatternNotFound) {
                            this.testLog.logIt(String.format("The pattern [%1$s] inside the given region was not found! Instead this was found inside the given region:[%2$s].", pattern1.getFilename(), saveScreenshotFromRegion(region)));
                        }
                        throw new Exception(e);
                    }
                }

            case WAIT_FOR:
                testLog.logIt(String.format(WAIT_EXISTS_LOG, action.toString(), mainImagePath, mainImageSimilarity, String.valueOf(timeoutSec)));
                if (region == null) {
                    screen.wait(pattern1, timeoutSec);
                } else {
                    region.wait(pattern1, timeoutSec);
                }
                break;

            default:
                throw new Exception(String.format("The action %1$s is not supported by SuperAction method yet!", action.toString()));
        }
        return null;
    }

    /***
     * Simple helper that use thread sleep´.
     * @param timeoutMs: How long it should wait until click again.
     * @throws InterruptedException
     */
    private void waitAfterClickAction(double timeoutMs) throws InterruptedException {
        this.waitHelper.waitMilliseconds(timeoutMs);
    }

    /***
     * Simple helper that evaluates the min score required to consider a match successful.
     * @param currentScore: The score found on a match pattern.
     * @return
     */
    private boolean evaluateScore(double currentScore) {
        this.testLog.logIt(String.format("###Image Recognition match evaluation:[#Found pattern score:%1$s][#Minimum acceptance score:%2$s]", String.valueOf(currentScore), String.valueOf(this.sureMatchMinimumScore)));
        return (currentScore >= this.sureMatchMinimumScore);
    }
}