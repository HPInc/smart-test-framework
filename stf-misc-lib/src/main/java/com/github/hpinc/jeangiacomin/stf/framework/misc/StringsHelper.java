package com.github.hpinc.jeangiacomin.stf.framework.misc;

import java.util.List;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class StringsHelper {

    /***
     * Helper that return all list content in a row separate by a given csv separator.
     * @param content: The list.
     * @return
     */
    public static String returnAllListContentInRow(List<String> content) {
        return returnAllListContentInRow(content, "");
    }

    /***
     * Helper that return all list content in a row separate by a given csv separator.
     * @param content: The list.
     * @param separator: A separator that will be added among each list entry when assembling everything in a single row.
     * @return
     */
    public static String returnAllListContentInRow(List<String> content, String separator) {
        String finalString = "";
        for (int i = 0; i < content.size(); i++) {
            finalString += (i + 1) < content.size() ? content.get(i) + separator : content.get(i);
        }
        return finalString;
    }

    /**
     * Simple helper to mask string, in order to prevent it getting exposed in logs, such as passwords or any other sensitive info.
     *
     * @param content
     * @return
     */
    public static String maskString(String content) {
        return maskString(content, "");
    }

    /**
     * Simple helper to mask string, in order to prevent it getting exposed in logs, such as passwords or any other sensitive info.
     *
     * @param content:  The content to be replaced
     * @param replaceBy
     * @return
     */
    public static String maskString(String content, String replaceBy) {
        String defaultReplace = "*";
        int passLength = content.length();
        String maskedString = "";
        for (Integer i = 0; i < passLength; i++) {
            maskedString += replaceBy.isEmpty() ? defaultReplace : replaceBy;
        }
        return maskedString;
    }
}