package com.github.jeansantos38.stf.dataclasses.datadriven;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statement {

    public String agent; //sikuli, appium, selenium
    public String purpose; //set, assert
    public String selector; //The selector unique id (99% of the cases)
    public String locator; //The locator mechanism for the selector. It's not required for Sikuli.
    public String method; //What will be the method used by sikuli to execute a action. It's not required for WebDrivers.
    public String innerSelector; //The selector unique id of a web element inside a main web element (combo-boxes of AngularJS).
    public String innerLocator;
    public Object delayActionInMs; //Time to wait before executing the statement.
    public String areaId; //Applicable only for Sikuli. It says what is the navigator area id to look for.
    public String action; //The action and the target type (usually setField for Web Driver usage, or (99% of the cases), but it could be setWhatever -> that could be a new type of field or specific method.)
    public String innerActionType; //The action and selector type of a web element hidden inside a main web element (combo-boxes of AngularJS).
    public String contentType; //string, int, double, etc.
    public String operator; //AssertTrue, Exists, False, Equals, NotNull, etc.
    public Object[] content; //The value to be set or asserted.
    public String contentModifier; //stringFormat, generateRandom, sortValueFromArray (just one value to be replaced).
    public String randomValueCompositionForContent; //numeric, alphabetic, alphaNumeric
    public int randomValueMaxLengthForContent; //1-n
    public int xCoordinate;
    public int yCoordinate;
    public int rectangleTopLeftX;
    public int rectangleTopLeftY;
    public int rectangleBottomRightX;
    public int rectangleBottomRightY;

    public Statement() {
    }
}