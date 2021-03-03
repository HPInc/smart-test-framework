package com.github.jeansantos38.stf.framework.misc;

import com.github.jeansantos38.stf.framework.wait.WaitHelper;

import javax.swing.*;
import java.util.List;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class VisualWarningsHelper {

    /***
     * Show a pop up with the given info.
     * Used in semi-automated tests.
     * @param title: A title for the dialog.
     * @param message: A message for dialog's content.
     */
    public static void showDialogInfo(String title, String message) throws InterruptedException {
        showDialog(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    /***
     * Show a pop up with the given info.
     * Used in semi-automated tests.
     * @param title: A title for the dialog.
     * @param message: A message for dialog's content.
     */
    public static void showDialogInfo(String title, String message, int waitSecAfterDismiss) throws InterruptedException {
        showDialog(title, message, JOptionPane.INFORMATION_MESSAGE, waitSecAfterDismiss);
    }

    /***
     * Show a pop up with the given info.
     * Used in semi-automated tests.
     * @param title: A title for the dialog.
     * @param message: A message for dialog's content.
     * @param jOptionPane: The type of the dialog.
     */
    public static void showDialog(String title, String message, int jOptionPane) throws InterruptedException {
        showDialog(title, message, jOptionPane, 0);
    }

    /***
     * Show a pop up with the given info.
     * Used in semi-automated tests.
     * @param title: A title for the dialog.
     * @param message: A message for dialog's content.
     * @param jOptionPane: The type of the dialog.
     */
    public static void showDialog(String title, String message, int jOptionPane, int waitSecAfterDismiss) throws InterruptedException {
        JOptionPane.showMessageDialog(null, message, title, jOptionPane);
        if (waitSecAfterDismiss > 0)
            new WaitHelper().waitSeconds(waitSecAfterDismiss);
    }


}