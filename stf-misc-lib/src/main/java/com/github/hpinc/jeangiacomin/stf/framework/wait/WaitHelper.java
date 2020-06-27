package com.github.hpinc.jeangiacomin.stf.framework.wait;

import com.github.hpinc.jeangiacomin.stf.enums.wait.ThreadWait;
import com.github.hpinc.jeangiacomin.stf.framework.misc.RandomValuesHelper;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class WaitHelper {

    /**
     * Default constructor.
     */
    public WaitHelper() {
    }

    /**
     * Helper that will wait during a random period of time of a give threshold.
     *
     * @param minWaitSec Min value to wait.
     * @param maxWaitSec Max value to wait.
     * @throws InterruptedException
     */
    public void randomWait(double minWaitSec, double maxWaitSec) throws InterruptedException {
        Double random = RandomValuesHelper.generateRandomDouble(minWaitSec, maxWaitSec);
        random = random * 1000;
        long timeToWait = random.longValue();
        Thread.sleep(timeToWait);
    }

    /**
     * Simple helper that will perform a thread sleep.
     *
     * @param waitSec: How many seconds.
     * @throws InterruptedException
     */
    public void waitSeconds(double waitSec) throws InterruptedException {
        Double finalWait = waitSec;
        finalWait = finalWait * 1000;
        long timeToWait = finalWait.longValue();
        Thread.sleep(timeToWait);
    }

    /**
     * Simple helper that will perform a thread sleep.
     *
     * @param waitMs: How many milliseconds.
     * @throws InterruptedException
     */
    public void waitMilliseconds(double waitMs) throws InterruptedException {
        Double finalWait = waitMs;
        long timeToWait = finalWait.longValue();
        Thread.sleep(timeToWait);
    }

    /**
     * Simple helper that performs thread sleep using a fix range of values.
     *
     * @param wait: How long should wait.
     * @throws Exception
     */
    public void wait(ThreadWait wait) throws Exception {
        switch (wait) {
            case WAIT_1_SEC:
                Thread.sleep(1000);
                break;
            case WAIT_2_SEC:
                Thread.sleep(2000);
                break;
            case WAIT_3_SEC:
                Thread.sleep(3000);
                break;
            case WAIT_5_SEC:
                Thread.sleep(5000);
                break;
            case WAIT_10_SEC:
                Thread.sleep(10000);
                break;
            case WAIT_15_SEC:
                Thread.sleep(15000);
                break;
            case WAIT_30_SEC:
                Thread.sleep(30000);
                break;
            case WAIT_45_SEC:
                Thread.sleep(45000);
                break;
            case WAIT_60_SEC:
                Thread.sleep(60000);
                break;
            case WAIT_2_MIN:
                Thread.sleep(120000);
                break;
            case WAIT_3_MIN:
                Thread.sleep(180000);
                break;
            case WAIT_5_MIN:
                Thread.sleep(300000);
                break;
            case WAIT_10_MIN:
                Thread.sleep(600000);
                break;
            default:
                throw new Exception(String.format("%1$s is not supported yet!", wait.toString()));
        }
    }
}