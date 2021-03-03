package com.github.jeansantos38.stf.data.classes.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

@JsonIgnoreProperties(ignoreUnknown = true)
public class NavigatorArea {
    public String areaId;
    public Element[] elements;
    public NavigatorArea() {
    }
}