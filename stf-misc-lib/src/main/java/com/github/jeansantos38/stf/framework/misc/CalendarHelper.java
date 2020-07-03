package com.github.jeansantos38.stf.framework.misc;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class CalendarHelper {

    private final static String DEFAULT_DATE_PATTERN = "MM/dd/yyyy' 'HH:mm:ss";

    /***
     * Helper to extract a human readable date from a timestamp using a given date/time format.
     * @param timestamp: The long timestamp to be converted in human readable date.
     * @return A human readable date time.
     */
    public static String convertTimestampToDate(long timestamp) {
        return convertTimestampToDate(timestamp, DEFAULT_DATE_PATTERN);
    }

    /***
     * Helper to extract a human readable date from a timestamp using a given date/time format.
     * @param timestamp: The long timestamp to be converted in human readable date.
     * @param dateTimeFormat: Date Time format to be used.
     * @return A human readable date time.
     */
    public static String convertTimestampToDate(long timestamp, String dateTimeFormat) {
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat(dateTimeFormat);
        return format.format(date);
    }

    /***
     * Helper to create dates with high accuracy in order to be used specially along with Activity Scope.
     * @param date: A date...
     * @param hour: A hour...
     * @param minutes: Minutes...
     * @throws Exception
     */
    public static Long convertDateToTimestamp(Date date, Integer hour, Integer minutes, Integer seconds) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new GregorianCalendar(year, month, day, hour, minutes, seconds).getTime().getTime();
    }

    public static String getCurrentTimeAndDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        return dateFormat.format(date);
    }
}