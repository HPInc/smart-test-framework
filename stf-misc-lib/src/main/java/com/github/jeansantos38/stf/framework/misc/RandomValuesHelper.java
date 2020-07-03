package com.github.jeansantos38.stf.framework.misc;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class RandomValuesHelper {

    /**
     * Helper to generate a random value given a threshold.
     *
     * @param minValue: Min value of threshold.
     * @param maxValue: Max value of threshold.
     * @return
     */
    public static Double generateRandomDouble(double minValue, double maxValue) {
        Random random = new Random();
        return (minValue + random.nextDouble() * (maxValue - minValue));
    }

    /**
     * Helper to generate a random value given a threshold.
     *
     * @param minValue: Min value of threshold.
     * @param maxValue: Max value of threshold.
     * @return
     */
    public static Integer generateRandomInt(int minValue, int maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
    }

    /**
     * Simple helper that generates a random value.
     *
     * @param length
     * @return
     */
    public static String generateAlphabetic(int length) {
        return generateAlphabetic(length, false);
    }

    /**
     * Simple helper that generates a random value.
     *
     * @param length
     * @param upperCase
     * @return
     */
    public static String generateAlphabetic(int length, boolean upperCase) {
        return upperCase ? RandomStringUtils.randomAlphabetic(length).toUpperCase() : RandomStringUtils.randomAlphabetic(length).toLowerCase();
    }

    /**
     * Simple helper that generates a random value.
     *
     * @param length
     * @return
     */
    public static String generateRandomAlphanumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Simple helper that generates a random value.
     *
     * @param length
     * @return
     */
    public static String generateRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    //region To Be Deprecate


    //endregion
}