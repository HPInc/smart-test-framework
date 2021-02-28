package com.github.jeansantos38.stf.framework.desktop;

import com.github.jeansantos38.stf.enums.wait.ThreadWait;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.misc.CalendarHelper;
import com.github.jeansantos38.stf.framework.misc.RandomValuesHelper;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.sikuli.script.Screen;
import org.sikuli.vnc.VNCScreen;

import java.io.IOException;

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
}
