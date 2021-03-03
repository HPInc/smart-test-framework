package com.github.jeansantos38.stf.framework.ui;


public class UiVisualFeedback {

    boolean enableHighlight;

    public boolean highlightEnabled() {
        return enableHighlight;
    }

    public void setEnableHighlight(boolean enableHighlight) {
        this.enableHighlight = enableHighlight;
    }

    String masterHighlightColor;
    String coordinateHighlightColor;
    String areaHighlightColor;
    double highlightTimeSec;
    int relHighlightH;
    int relHighlightW;

    public UiVisualFeedback() {
        this(true, "green", "red", "orange", 0.2, 0, 0);
    }

    public UiVisualFeedback(
            String masterHighlightColor,
            String coordinateHighlightColor,
            String areaHighlightColor) {
        this(true, masterHighlightColor, coordinateHighlightColor, areaHighlightColor, 0.2, 0, 0);

    }

    public UiVisualFeedback(
            String masterHighlightColor,
            String coordinateHighlightColor,
            String areaHighlightColor,
            double highlightTimeSec) {
        this(true, masterHighlightColor, coordinateHighlightColor, areaHighlightColor, highlightTimeSec, 0, 0);

    }

    public UiVisualFeedback(
            boolean enableHighlight,
            String masterHighlightColor,
            String coordinateHighlightColor,
            String areaHighlightColor,
            double highlightTimeSec,
            int relHighlightH,
            int relHighlightW) {
        this.enableHighlight = enableHighlight;
        this.masterHighlightColor = masterHighlightColor;
        this.coordinateHighlightColor = coordinateHighlightColor;
        this.areaHighlightColor = areaHighlightColor;
        this.highlightTimeSec = highlightTimeSec;
        int HIGHLIGHT_DEFAULT_SIZE = 1;
        this.relHighlightH = relHighlightH == 0 ? HIGHLIGHT_DEFAULT_SIZE : relHighlightH;
        this.relHighlightW = relHighlightW == 0 ? HIGHLIGHT_DEFAULT_SIZE : relHighlightW;
    }
}