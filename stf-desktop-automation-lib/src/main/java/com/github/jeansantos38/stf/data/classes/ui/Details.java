package com.github.jeansantos38.stf.data.classes.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

@JsonIgnoreProperties(ignoreUnknown = true)
public class Details {

    public String selector;
    public String imagePath;
    public String ctrlType;
    public String interactionType;
    public int xCoordinate;
    public int yCoordinate;
    public int regionTopLeftX;
    public int regionTopLeftY;
    public int regionBottomRightX;
    public int regionBottomRightY;
    public double similarity;

    public Details() {
    }

    public Details( String imagePath) {
        this.imagePath = imagePath;
    }

    public Details(String imagePath, int xCoordinate, int yCoordinate, double similarity) {
        this.imagePath = imagePath;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.similarity = similarity;
    }

    public Details(String imagePath, double similarity) {
        this.imagePath = imagePath;
        this.similarity = similarity;
    }

    public Details(String selector, String imagePath, String ctrlType, String interactionType, int xCoordinate, int yCoordinate, int regionTopLeftX, int regionTopLeftY, int regionBottomRightX, int regionBottomRightY, double similarity) {
        this.selector = selector;
        this.imagePath = imagePath;
        this.ctrlType = ctrlType;
        this.interactionType = interactionType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.regionTopLeftX = regionTopLeftX;
        this.regionTopLeftY = regionTopLeftY;
        this.regionBottomRightX = regionBottomRightX;
        this.regionBottomRightY = regionBottomRightY;
        this.similarity = similarity;
    }
}