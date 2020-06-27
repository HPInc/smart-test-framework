package com.github.hpinc.jeangiacomin.stf.dataclasses.web.datadriven;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {

    public String selector;
    public String masterLoc;
    public String ctrlType;
    public String interactionType;
    public String refMasterLoc;
    public int xCoordinate;
    public int yCoordinate;
    public int rectangleTopLeftX;
    public int rectangleTopLeftY;
    public int rectangleBottomRightX;
    public int rectangleBottomRightY;
    public float similarity;
    public String[] shortcuts;

    public Element() {
    }
}