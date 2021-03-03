package com.github.jeansantos38.stf.framework.ui;

import com.github.jeansantos38.stf.data.classes.ui.Element;
import com.github.jeansantos38.stf.data.classes.ui.Navigator;
import com.github.jeansantos38.stf.data.classes.ui.NavigatorArea;
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

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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

    public static void performKeyCombination(Object screen, String key, int... modifiers) throws InterruptedException {
        int finalModifiers = 0;
        for (int modifier : modifiers) {
            finalModifiers += modifier;
        }
        if (UiAutomationUtils.isVncScreen(screen)) {
            ((VNCScreen) screen).type(key, finalModifiers);
        } else {
            new WaitHelper().waitMilliseconds(5000);
            ((Screen) screen).type(key, finalModifiers);
        }
    }

    public static String getContentFromClipboard() {
        Clipboard cb = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
        try {
            Transferable t = cb.getContents(null);
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor))
                return (String) t.getTransferData(DataFlavor
                        .stringFlavor);
        } catch (IOException | UnsupportedFlavorException ex) {
            System.out.println("ZICOU");
        }
        return null;
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

    public static Element retrievePatternFromNavigatorString(String navigatorFileFullPath, String areaId, String selector) throws Exception {
        String navigatorContent = InputOutputHelper.readContentFromFile(navigatorFileFullPath);
        Navigator navigator = DeserializeHelper.deserializeStringToObject(Navigator.class, SerializationType.YAML, navigatorContent);
        NavigatorArea masterImageDetails = Arrays.stream(navigator.navigatorAreas).filter(x -> x.areaId.equals(areaId)).findAny().orElse(null);
        if (masterImageDetails == null)
            throw new Exception(String.format("The master image details for area '%1$s' was not retrieved from navigator, pls review the given parameters.", areaId));
        Element element = Arrays.stream(masterImageDetails.elements).filter(x -> x.details.selector.equals(selector)).findFirst().orElse(null);
        if (element == null || element.details == null)
            throw new Exception(String.format("The element for selector '%1$s' was not retrieved from navigator, pls review the given parameters.", selector));
        new TestLog().logIt(String.format("Searching for the image '%1$s' related to the selector '%2$s'.", element.details.imagePath, selector));
        return element;
    }
}
