package com.github.jeansantos38.stf.framework.logger;

import java.util.List;
import java.util.logging.Logger;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class TestLog {

    private static final String LOGGER_STRING_NAME = "TestLog";
    private Logger testLog;
    private boolean enableLog;

    /***
     * Constructor
     */
    public TestLog() {
        this.testLog = Logger.getLogger(LOGGER_STRING_NAME);
        this.enableLog = true;
    }

    /***
     * Constructor
     * @param enableLogs: Enable logs or not.
     */
    public TestLog(boolean enableLogs) {
        this.testLog = Logger.getLogger(LOGGER_STRING_NAME);
        this.enableLog = enableLogs;
    }

    /***
     * Use this setter for enabling logs.
     */
    public void setEnableLog() {
        this.enableLog = true;
    }

    /***
     * Use this setter for disabling logs.
     */
    public void setDisableLog() {
        this.enableLog = false;
    }

    /***
     * Log content - or not depending on enableLog property.
     * @param content: Value to be logged.
     */
    public void logIt(String content) {
        logIt(content, true);
    }

    /***
     * Log content - or not depending on enableLog property.
     * @param addLineBreakAtEnd Add a new line at the end of string, this will make easier to read logs.
     * @param content: Value to be logged.
     */
    public void logIt(String content, boolean addLineBreakAtEnd) {
        if (enableLog) {
            this.testLog.info(addLineBreakAtEnd ? content + "\n" : content);
        }
    }

    /***
     * Simple helper to log all entries from a given list, line per line.
     * @param list: A list of strings.
     */
    public void logAllEntries(List<String> list) {
        logAllEntries(list, true);
    }

    /***
     * Simple helper to log all entries from a given list, line per line.
     * @param addLineBreakAtEnd Add a new line at the end of string, this will make easier to read logs.
     * @param list: A list of strings.
     */
    public void logAllEntries(List<String> list, boolean addLineBreakAtEnd) {
        logAllEntries(list, false, addLineBreakAtEnd);
    }

    /***
     * Simple helper to log all entries from a given list, line per line.
     * @param addLineBreakAtEnd Add a new line at the end of string, this will make easier to read logs.
     * @param oneLogPerEntry Each entry will be logged individually.
     * @param list: A list of strings.
     */
    public void logAllEntries(List<String> list, boolean oneLogPerEntry, boolean addLineBreakAtEnd) {
        if (oneLogPerEntry) {
            for (String entry : list) {
                logIt(addLineBreakAtEnd ? entry + "\n" : entry);
            }
        } else {
            String finalLog = "";
            for (String entry : list) {
                finalLog += addLineBreakAtEnd ? entry + "\n" : entry;
            }
            logIt(finalLog);
        }
    }
}