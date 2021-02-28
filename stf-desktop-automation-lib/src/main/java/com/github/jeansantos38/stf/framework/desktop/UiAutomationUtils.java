package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.dataclasses.web.datadriven.DataDrivenNavigator;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.Elements;
import com.github.jeansantos38.stf.dataclasses.web.datadriven.MasterImageDetails;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.enums.wait.ThreadWait;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class UiAutomationUtils {

    public static boolean isVncScreen(Object screen) {
        return !screen.getClass().equals(Screen.class);
    }

    public static void type(Object screen, String content) {
        if (UiAutomationUtils.isVncScreen(screen)) {
            ((VNCScreen) screen).type(content);
        } else {
            ((Screen) screen).type(content);
        }
    }

    public static void paste(Object screen, String content) {
        if (UiAutomationUtils.isVncScreen(screen)) {
            ((VNCScreen) screen).paste(content);
        } else {
            ((Screen) screen).paste(content);
        }
    }

    public static String saveDesktopScreenshot(Object screen, String folderPath) throws IOException {
        String filename_prefix = RandomValuesHelper.generateAlphabetic(10);
        String filename_suffix = CalendarHelper.getCurrentTimeAndDate();
        return saveDesktopScreenshot(screen, folderPath, filename_prefix + filename_suffix);
    }

     static String saveDesktopScreenshot(Object screen, String folderPath, String filename) throws IOException {
        String path = InputOutputHelper.createDirectory(folderPath);
        if (UiAutomationUtils.isVncScreen(screen)) {
            return ((VNCScreen) screen).capture().save(path, filename);
        } else {
            return ((Screen) screen).capture().save(path, filename);
        }
    }

     public static VNCScreen connectToVncScreen(String vncServerIpAddress, int vncServerPort, String vncServerPassword, int connectionTimeoutSec, int operationTimeoutMs, int retryAttempts) throws Exception {
        int counter = 0;
        VNCScreen vncScreen;
        while (counter < retryAttempts) {
            vncScreen = VNCScreen.start(vncServerIpAddress, vncServerPort, vncServerPassword, connectionTimeoutSec, operationTimeoutMs);
            if (vncScreen.getClient() == null) {
                counter++;
                new WaitHelper().wait(ThreadWait.WAIT_3_SEC);
            } else {
                return vncScreen;
            }
        }
        throw new Exception(String.format("It was not possible to establish a connection to VNC server %s:%s in %s attempts!", vncServerIpAddress, vncServerPort, retryAttempts));
    }

    public static Elements retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String
            selector, boolean retrieveMasterLocOnly) throws Exception {
        String navigatorContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
        DataDrivenNavigator dataDrivenNavigator = DeserializeHelper.deserializeStringToObject(DataDrivenNavigator.class, SerializationType.JSON, navigatorContent);
        MasterImageDetails masterImageDetails = Arrays.stream(dataDrivenNavigator.MasterImageDetails).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
        if (masterImageDetails == null)
            throw new Exception(String.format("The master image details for area '%1$s' was not retrieved from navigator, pls review the given parameters.", areaId));

        Elements elements = Arrays.stream(masterImageDetails.elements).filter(x -> x.element.selector.equals(selector)).findFirst().orElse(null);
        if (elements == null || elements.element == null)
            throw new Exception(String.format("The element for selector '%1$s' was not retrieved from navigator, pls review the given parameters.", selector));


        new TestLog().logIt(String.format("Searching for the image '%1$s' related to the selector '%2$s'.", retrieveMasterLocOnly ? elements.element.masterLoc : elements.element.refMasterLoc, selector));
        return elements;
    }

}
