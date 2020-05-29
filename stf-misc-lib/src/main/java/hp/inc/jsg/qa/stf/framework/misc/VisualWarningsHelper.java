package hp.inc.jsg.qa.stf.framework.misc;

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
    public static void showDialogInfo(String title, String message) {
        showDialog(title, message, 3);
    }

    /***
     * Show a pop up with the given info.
     * Used in semi-automated tests.
     * @param title: A title for the dialog.
     * @param message: A message for dialog's content.
     * @param jOptionPane: The type of the dialog.
     */
    public static void showDialog(String title, String message, int jOptionPane) {
        JOptionPane.showMessageDialog(null, message, title, jOptionPane);
    }
}