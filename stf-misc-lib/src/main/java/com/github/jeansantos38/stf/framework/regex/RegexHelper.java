package com.github.jeansantos38.stf.framework.regex;

import com.github.jeansantos38.stf.framework.logger.TestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class RegexHelper {

    private TestLog testLog;
    public static final String REGEX_GUID = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String HTTP_ERROR_CODES_REGEX = "([4||5][0-9][0-9])";
    public static final String STRING_FORMAT_PATTERN_TBR_REGEX = "[%][0-9]*[$][a-z]";
    public static final String REGEX_FACEBOOK_AUTHENTICATION_CODE = "([^=])\\w+(?=&)";
    public static final String REGEX_FIRST_UUID_OCTET = "[0-9a-f]{8}(?=-)";
    public static final String CONTENT_OR_REGEX_PATTERN_NULL_OR_EMPTY = "The content to be searched or the REGEX pattern cannot be null\\empty!";

    /**
     * Default Constructor
     */
    public RegexHelper(TestLog testLog) {
        this.testLog = testLog;
    }

    /**
     * Simple helper that check if a string does match with a given regex pattern.
     *
     * @param regexPattern
     * @param value
     * @return
     */
    public static boolean isMatch(String regexPattern, String value) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    /***
     * Helper to find a match using a given pattern.
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern: The regex pattern.
     * @return
     */
    public static Matcher returnMatch(String fullContent, String pattern) throws Exception {
        evaluatePatternAndFullContent(fullContent, pattern);
        Pattern finalPattern = Pattern.compile(pattern);
        Matcher matcher = finalPattern.matcher(fullContent);
        return matcher;
    }

    /***
     * Helper to find a string using a given pattern.
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern: The regex pattern.
     * @return
     */
    public static String returnFirstMatchFrom(String fullContent, String pattern) throws Exception {
        evaluatePatternAndFullContent(fullContent, pattern);
        List<String> matches = returnAllMatchesFrom(fullContent, pattern);
        if (matches.size() == 0) {
            return "";
        }
        return matches.get(0);
    }

    /***
     * Helper to find all matching string using a give pattern.
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern: The regex pattern.
     * @return
     */
    public static List<String> returnAllMatchesFrom(String fullContent, String pattern) throws Exception {
        Matcher matcher = returnMatch(fullContent, pattern);
        List<String> allReferences = new ArrayList<>();
        // Find all matches
        while (matcher.find()) {
            // Get the matching string
            allReferences.add(matcher.group());
        }
        return allReferences;
    }


    /**
     * Helper to find and replace the first match found.
     *
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern:     The regex pattern.
     * @param replacement: The new value.
     * @return
     * @throws Exception
     */
    public static String findReplaceFirstMatch(String fullContent, String pattern, String replacement) throws Exception {
        return returnMatch(fullContent, pattern).replaceFirst(replacement);
    }

    /**
     * Helper to find and replace all matches found.
     *
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern:     The regex pattern.
     * @param replacement: The new value.
     * @return
     * @throws Exception
     */
    public static String findReplaceAllMatches(String fullContent, String pattern, String replacement) throws Exception {
        return returnMatch(fullContent, pattern).replaceAll(replacement);
    }

    /**
     * Helper that returns the match for a given group.
     *
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern:     The regex pattern.
     * @param groupIndex:  Remember that group index starts from 1.
     * @return
     * @throws Exception
     */
    public static String returnMatchFromGroup(String fullContent, String pattern, int groupIndex) throws Exception {
        Matcher matcher = returnMatch(fullContent, pattern);
        String targetMatch = "";
        // Find all matches
        while (matcher.find()) {
            // Get the matching string
            targetMatch = (matcher.group(groupIndex));
            break;
        }
        return targetMatch;
    }

    /***
     * Evaluate if pattern and content meets the minimum requirements.
     * @param fullContent: The content where the helper will be searching for a match.
     * @param pattern: The regex pattern.
     * @throws Exception
     */
    private static void evaluatePatternAndFullContent(String fullContent, String pattern) throws Exception {
        if (fullContent == null || fullContent.isEmpty() || pattern == null || pattern.isEmpty()) {
            throw new Exception(CONTENT_OR_REGEX_PATTERN_NULL_OR_EMPTY);
        }
    }
}